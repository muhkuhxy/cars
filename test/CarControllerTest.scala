import controllers.CarController
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test._
import Specs._
import play.api.libs.json.Json

class CarControllerTest extends PlaySpec with Results {
  "A CarController" when {
    "POSTing to /car" must {
      "create a new car advert" in {
        val controller = new CarController
        val result: Result = await(controller.create().apply(FakeRequest().withBody(
          Json.parse(
            """{
              |"title": "some car",
              |"fuel": "Gasoline",
              |"price": 12345,
              |"new": true
              |}
            """.stripMargin))))

        result.header.status mustBe 201
        result.header.headers("location") mustBe "/car/1"
      }
    }
  }
}