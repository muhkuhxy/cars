import models.CarRepository
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._

class IntegrationTest extends PlaySpec with OneServerPerSuite {

//  override lazy val app = new GuiceApplicationBuilder().overrides(bind[CarRepository].to[MockCarRepository])
//    .build()
//
//  "The car advert api" must {
//     "create a new advert" in {
//      val response = await(wsCall(controllers.routes.CarController.create()).post(
//        Json.parse("""
//           { "title": "test advert",
//             "fuel": "Gasoline",
//             "price": 123456,
//             "new": true
//           }
//        """)))
//      response.status mustBe 201
//      val location = response.header("location")
//      location.value must endWith regex """/car/\d+"""
//
//      val car = await(wsUrl(s"http://localhost:$port/${location.value}").get)
//    }
//  }



}
