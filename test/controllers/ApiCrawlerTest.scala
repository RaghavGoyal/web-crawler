package controllers

import play.api.inject.guice.GuiceApplicationBuilder
import akka.util.Timeout
import org.scalatest.{FunSpec, Matchers}
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.{FakeRequest, Helpers}

import scala.concurrent.duration.FiniteDuration

class ApiCrawlerTest extends FunSpec with Matchers with GuiceOneAppPerTest{

  implicit val timeout: Timeout = Timeout(FiniteDuration(3,java.util.concurrent.TimeUnit.SECONDS))

  describe("ApiCrawler should"){
    it("be mapped to correct request method"){
      routes.ApiCrawler.start().method should be("POST")

    }
    it("be mapped to correct route"){
      routes.ApiCrawler.start().url should be("/api/crawl")
    }

    it("return bad request if urls are not provided in request body"){
      val response = controller.start(
        FakeRequest("POST", routes.ApiCrawler.start().url)
          .withHeaders(("Content-Type", "application/json"))
          .withJsonBody(Json.parse(
            """
              |{
              |"urla": ["https://google.com", "https://github.com"]
              |}
              |""".stripMargin))
      )
      Helpers.status(response) should be(400)
      val emptyUrlsJsonResponse = Json.parse("""{"result":[],"error":"urls not found in request body"}""")
      Helpers.contentAsJson(response) should be(emptyUrlsJsonResponse)
    }

    it("should send response with correct json if urls are provided in request body"){
      val response = controller.start(
        FakeRequest("POST", routes.ApiCrawler.start().url)
          .withHeaders(("Content-Type", "application/json"))
          .withJsonBody(Json.parse(
            """
              |{
              |"urls": ["https://google.com", "https://github.com"]
              |}
              |""".stripMargin))
      )
      Helpers.status(response) should be(200)
    }
  }

  val controller = new GuiceApplicationBuilder()
                      .configure(
                        Map(
                          "play.ws.cache.enabled" -> false
                        )
                      )
                      .injector()
                      .instanceOf[ApiCrawler]

}
