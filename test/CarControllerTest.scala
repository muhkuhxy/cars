import controllers.CarController
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration._

class CarControllerTest extends PlaySpec with Results {
  val timeout: Duration = 2.seconds

  "A CarController" when {
    "POSTing to /car" must {
      "create a new car advert" in {
        val controller = new CarController
        val result: Result = Await.result(controller.create().apply(FakeRequest()), timeout)
        result.header.status mustBe 201
      }
    }
  }
}