import Specs._
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Logger
import play.api.libs.json.Json

class IntegrationTest extends PlaySpec with OneServerPerSuite {

  "The car advert api" must {
    "create a new advert" in {
      val response = await(wsCall(controllers.routes.CarController.create()).post(
        Json.parse("""
           { "title": "test advert",
             "fuel": "Gasoline",
             "price": 123456,
             "new": true
           }
        """)))
      response.status mustBe 201
      response.header("location").get must endWith regex """/car/\d+"""
    }
  }



}