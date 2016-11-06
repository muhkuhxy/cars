import controllers.CarController
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test._

import Specs._

class CarControllerTest extends PlaySpec with Results {
  "A CarController" when {
    "POSTing to /car" must {
      "create a new car advert" in {
        val controller = new CarController
        val result: Result = await(controller.create().apply(FakeRequest()))

        result.header.status mustBe 201
      }
    }
  }
}