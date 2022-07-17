package model

import play.api.libs.json.Json

case class ApiCrawlResponse(result: List[ResultSet], error: String)

case class ResultSet(url: String, data: String)

object ResultSet {
  implicit val resultSet = Json.format[ResultSet]
}

object ApiCrawlResponse {
  implicit val apiCrawlResponse = Json.format[ApiCrawlResponse]
}
