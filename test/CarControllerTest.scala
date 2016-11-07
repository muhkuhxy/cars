import controllers.{CarController, CarService}
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test._
import models.{BrandNewCar, Fuel}
import play.api.Logger
import play.api.libs.json.Json
import play.api.test.Helpers._

class CarControllerTest extends PlaySpec with Results {
  "A CarController" when {
    val controller = new CarController(new CarService(new MockCarRepository()))
    "POSTing /car" must {
      "create a new car advert" in {
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
        val result: Result = await(controller.create().apply(FakeRequest().withBody(
          Json.parse(
            """{
              |"title": "some car",
              |"fuel": "xxx",
              |"price": 12345,
              |"new": true
              |}
            """.stripMargin))))

        result.header.status mustBe 400
      }
    }

    "GETting a /car/{id}" must {
      "respond with the car advert" in {
        val car: BrandNewCar = BrandNewCar(1, "car", Fuel.Gasoline, 123)
        val repo = new MockCarRepository(Seq(car))
        val controller = new CarController(new CarService(repo))
        val result = contentAsJson(controller.get(1).apply(FakeRequest()))

        result mustEqual Json.parse("""
          {
            "id": 1,
            "title": "car",
            "fuel": "Gasoline",
            "price": 123,
            "new": true
          }
        """)
      }
    }

    "PUTting an existing advert" must {
      "refuse inconsistent requests" in {
        val result: Result = await(controller.update(1).apply(FakeRequest().withBody(
          Json.parse(
            """{
              |"id": 1,
              |"title": "some car",
              |"fuel": "Gasoline",
              |"price": 12345,
              |"new": false,
              |"mileage": 10
              |}
            """.stripMargin))))

        result.header.status mustBe 400
      }
    }


    "DELETEing an advert" must {
      "inform user of nonexisting adverts" in {
        val result: Result = await(controller.remove(1).apply(FakeRequest()))

        result.header.status mustBe 404
      }

      "remove an existing advert" in {
        val car: BrandNewCar = BrandNewCar(1, "car", Fuel.Gasoline, 123)
        val repo = new MockCarRepository(Seq(car))
        val controller = new CarController(new CarService(repo))
        val result: Result = await(controller.remove(1).apply(FakeRequest()))

        result.header.status mustBe 200
        repo.cars.size mustBe 0
      }
    }
  }
}
