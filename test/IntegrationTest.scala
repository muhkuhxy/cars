import models.{AnormCarRepository, CarRepository}
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Logger
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test.Helpers._

class IntegrationTest extends PlaySpec with OneServerPerSuite {

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[CarRepository].to[AnormCarRepository])
    .configure(inMemoryDatabase("default"))
    .build()

  "The car advert api" must {

    "support all required operations" in {
      val id = 4
      val advertJson: JsObject = Json.parse(
      """
         { "title": "test advert",
           "fuel": "Gasoline",
           "price": 123456,
           "new": true
         }
      """).as[JsObject]
      val advertUrl = createAdvert(advertJson)
      assertAdvertRetrievable(advertUrl, advertJson, id)

      val updatedAdvert = Json.parse(
        s"""
          |{ "id": $id,
          |"title": "i used the car :(",
          |"fuel": "Diesel",
          |"price": 12345,
          |"new": false,
          |"mileage": 17,
          |"firstRegistration": "2016-07-01"
          |}
        """.stripMargin
      ).as[JsObject]
      updateAdvert(updatedAdvert, id)

      assertAdvertRetrievable(advertUrl, updatedAdvert, id)

      deleteAdvert(advertUrl)
      assertUnavailable(advertUrl)
    }

    "support adding used car adverts" in {
      val advertJson: JsObject = Json.parse(
        """
         { "title": "test advert",
           "fuel": "Gasoline",
           "price": 123456,
           "new": false,
           "mileage": 75034,
           "firstRegistration": "1999-07-07"
         }
        """).as[JsObject]
      val advertUrl = createAdvert(advertJson)
      assertAdvertRetrievable(advertUrl, advertJson, id = 5)
    }

    def createAdvert(request: JsValue) = {
      val response = await(
        wsCall(
          controllers.routes.CarController.create()
        ).post(request))
      response.status mustBe 201
      val location = response.header("location")
      location.value must endWith regex """/car/\d+"""
      location.value
    }

    def assertAdvertRetrievable(url: String, advertJson: JsObject, id: Int = 1) {
      val carWithId = advertJson + ("id" -> JsNumber(id))
      val car = await(wsUrl(url).get).body
      Json.parse(car) mustEqual carWithId
    }

    def updateAdvert(request: JsValue, id: Long): Unit = {
      val response = await(
        wsCall(
          controllers.routes.CarController.update(id)
        ).put(request))
      response.status mustBe 200
    }

    def deleteAdvert(url: String) = {
      val response = await(wsUrl(url).delete)
      response.status mustBe 200
    }

    def assertUnavailable(url: String) = {
      val response = await(wsUrl(url).get)
      response.status mustBe 404
    }
  }


}
