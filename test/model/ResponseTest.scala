package model

import org.scalatest.{Matchers, WordSpecLike}

class ResponseTest extends WordSpecLike with Matchers {

  "Response" should {
    "be success if error is None" in {
      Response("input", "output", None).isSuccess should be(true)
    }

    "be success if error is not provided" in {
      Response("input", "output").isSuccess should be(true)
    }

    "not be success if there is Some error" in {
      Response("input", "error msg", Some("Error")).isSuccess should be(false)
    }
  }
}
