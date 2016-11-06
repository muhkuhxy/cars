import models.{AnormCarRepository, CarRepository}
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Logger
import play.api.db.Databases
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test.FakeApplication
import play.api.test.Helpers._
import play.api.db.evolutions._

class IntegrationTest extends PlaySpec with OneServerPerSuite {

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[CarRepository].to[AnormCarRepository])
    .configure(inMemoryDatabase("default", Map("MODE" -> "MYSQL")))
    .build()

  "The car advert api" must {
    "create a new advert and make it available" in {

      val newCarJson: JsValue = Json.parse(
        """
           { "title": "test advert",
             "fuel": "Gasoline",
             "price": 123456,
             "new": true
           }
        """)
      val response = await(wsCall(controllers.routes.CarController.create()).post(
        newCarJson))
      response.status mustBe 201
      val location = response.header("location")
      location.value must endWith regex """/car/\d+"""

      val JsSuccess(carWithId,_) = newCarJson.transform(
        __.read[JsObject].map( o => o ++ Json.obj("id" -> 1))
          andThen ((__ \ 'new).json.prune)
      )

      val car = await(wsUrl(location.value).get).body
      Json.parse(car) mustEqual carWithId
    }
  }


}
