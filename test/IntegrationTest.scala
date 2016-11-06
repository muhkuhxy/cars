import models.{AnormCarRepository, CarRepository}
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test.Helpers._

class IntegrationTest extends PlaySpec with OneServerPerSuite {

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[CarRepository].to[AnormCarRepository])
    .configure(inMemoryDatabase("default", Map("MODE" -> "MYSQL")))
    .build()

  "The car advert api" must {

    "support all required operations" in {
      val advertJson: JsValue = Json.parse(
      """
         { "title": "test advert",
           "fuel": "Gasoline",
           "price": 123456,
           "new": true
         }
      """)
      val advertUrl = createAdvert(advertJson)
      assertAdvertRetrievable(advertUrl, advertJson, id = 1)
      // TODO: PUT
      // TODO: DELETE
    }

    "support adding used car adverts" in {
      val advertJson: JsValue = Json.parse(
        """
         { "title": "test advert",
           "fuel": "Gasoline",
           "price": 123456,
           "new": false,
           "mileage": 75034,
           "firstRegistration": "1999-07-07"
         }
        """)
      val advertUrl = createAdvert(advertJson)
      assertAdvertRetrievable(advertUrl, advertJson, id = 2)
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

    def assertAdvertRetrievable(url: String, advertJson: JsValue, id: Int = 1) {
      val JsSuccess(carWithId, _) = advertJson.transform(
        __.read[JsObject].map(o => o ++ Json.obj("id" -> id))
          andThen ((__ \ 'new).json.prune)
      )

      val car = await(wsUrl(url).get).body
      Json.parse(car) mustEqual carWithId
    }
  }


}
