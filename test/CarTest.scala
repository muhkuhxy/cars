import org.scalatestplus.play._
import models._

class CarTest extends PlaySpec {

  type CarArgs = Tuple4[String, String, Fuel.Type, Int]
  type CarBuilder = CarArgs => BrandNewCar
  type UnconstructableTestCase = Tuple2[String, CarArgs]

  val validInput = ("1", "super nice car", Fuel.Gasoline, 5000)

  "A brand new car" when {

    val carBuilder = BrandNewCar.tupled

    "input is valid" must {
      "be constructable" in {
        val car = carBuilder(validInput)
        car.fuel mustBe Fuel.Gasoline
        car.id mustBe "1"
        car.title mustBe "super nice car"
        car.price mustBe 5000
      }
    }

    notConstructable(carBuilder)
  }

  def notConstructable(maker: CarBuilder) = {
    Seq(
      ("id is null", validInput.copy(_1 = null)),
      ("id is empty", validInput.copy(_1 = "")),
      ("title is null", validInput.copy(_2 = null)),
      ("title is empty", validInput.copy(_2 = "")),
      ("fuel is null", validInput.copy(_3 = null)),
      ("price is negative", validInput.copy(_4 = -1))
    ).foreach { case (condition, args) =>
      condition must {
        "not NOT be constructable" in {
          a[IllegalArgumentException] must be thrownBy {
            maker(args)
          }
        }
      }
    }
  }

}