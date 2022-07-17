package services

import javax.inject.Inject
import model.{ApiCrawlResponse, Response, ResultSet}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}

class CrawlService @Inject()(wsClient: WSClient) {

  def crawlUrls(urls: List[String]): Future[ApiCrawlResponse] = {
    Future.sequence(
      urls.map{ url => crawl(url) }
    ).map{ resultSetList =>
      createResponse(resultSetList)
    }
  }

  private def crawl(url: String) = {
    wsClient.url(url).get().map{resp =>
      resp.status match {
        case 200 => Response(url, resp.body.trim)
        case _ => Response(url, resp.body.trim , Some(s"Failed with status ${resp.status}"))
      }
    }
  }

  private def createResponse(resultList: List[Response[String, String, _<:String]]) = {
    val (success, failure) = resultList.partition(_.isSuccess)
    ApiCrawlResponse(
      success.map( resp =>
        ResultSet(resp.input, resp.data)
      ),
      failure.map(err => err.input).mkString(" ")
    )
  }

}
