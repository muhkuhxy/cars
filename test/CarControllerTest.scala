import controllers.CarController
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test._
import models.{BrandNewCar, Fuel}
import play.api.Logger
import play.api.libs.json.Json
import play.api.test.Helpers._

import scala.concurrent.Future

class CarControllerTest extends PlaySpec with Results {
  "A CarController" when {
    "POSTing /car" must {
      "create a new car advert" in {
        val carRepository = new MockCarRepository
        val controller = new CarController(carRepository)
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

      "refuse invalid fuel types" in {
        val controller = new CarController(new MockCarRepository)
        val eventualResult1: Future[Result] = controller.create().apply(FakeRequest().withBody(
          Json.parse(
            """{
              |"title": "some car",
              |"fuel": "xxx",
              |"price": 12345,
              |"new": true
              |}
            """.stripMargin)))
        val result: Result = await(eventualResult1)

        Logger.info(s"${contentAsString(eventualResult1)}")
        result.header.status mustBe 400
      }
    }

    "GETting a /car/{id}" must {
      "respond with the car advert" in {
        val car: BrandNewCar = BrandNewCar(1, "car", Fuel.Gasoline, 123)
        val repo = new MockCarRepository(Seq(car))
        val controller = new CarController(repo)
        val result = contentAsJson(controller.get(1).apply(FakeRequest()))

        result mustEqual Json.parse("""
          {
            "id": 1,
            "title": "car",
            "fuel": "Gasoline",
            "price": 123
          }
        """)
      }
    }
  }
}