package controllers

import javax.inject.Inject
import model.ApiCrawlResponse
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{BaseController, ControllerComponents}
import services.CrawlService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ApiCrawler @Inject()(val controllerComponents: ControllerComponents,
                           crawlService: CrawlService
                          ) extends BaseController {

  def start = Action.async { request =>

    val requestId = request.id
    val body = request.body.asJson.get

    (body \ "urls").asOpt[List[String]] match {
      case Some(urlsList) =>
        crawlService.crawlUrls(urlsList).map{ resp =>
          Ok(Json.toJson(resp))
        }
      case None =>
        Future(
          BadRequest(
            Json.toJson(ApiCrawlResponse(List.empty, "urls not found in request body"))
          )
        )
    }
  }
}
