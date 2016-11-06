import Specs._
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}

class IntegrationTest extends PlaySpec with OneServerPerSuite {

  "The car advert api" must {
    "create a new advert" ignore {
      val response = await(wsCall(controllers.routes.CarController.create()).post(
        """
           { "title": "test advert",
             "fuel": "Gasoline",
             "price": 123456,
             "new": true
           }
        """))
      response.status mustBe 201
      response.header("location").get must endWith regex """/cars/\d+"""
    }
  }



}
