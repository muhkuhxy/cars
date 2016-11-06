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

    val advertJson: JsValue = Json.parse(
      """
         { "title": "test advert",
           "fuel": "Gasoline",
           "price": 123456,
           "new": true
         }
      """)

    "support all required operations" in {
      val advertUrl = createAdvert()
      assertAdvertRetrievable(advertUrl)
      // TODO: PUT
      // TODO: DELETE
    }

    def createAdvert() = {
      val response = await(
        wsCall(
          controllers.routes.CarController.create()
        ).post(advertJson))
      response.status mustBe 201
      val location = response.header("location")
      location.value must endWith regex """/car/\d+"""
      location.value
    }

    def assertAdvertRetrievable(url: String) {
      val JsSuccess(carWithId, _) = advertJson.transform(
        __.read[JsObject].map(o => o ++ Json.obj("id" -> 1))
          andThen ((__ \ 'new).json.prune)
      )

      val car = await(wsUrl(url).get).body
      Json.parse(car) mustEqual carWithId
    }
  }


}
