import org.scalatestplus.play._
import models._

class CarTest extends PlaySpec {

  "A brand new car" must {
    "be constructable with all required fields" in {
      val car = new BrandNewCar("1", "super nice car", Fuel.Gasoline, 5000)
      car.fuel mustBe Fuel.Gasoline
      car.id mustBe "1"
      car.title mustBe "super nice car"
      car.price mustBe 5000
    }
  }

}