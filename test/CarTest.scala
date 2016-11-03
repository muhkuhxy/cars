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

    "fail on null or empty id" in {
      List(null, "").foreach { id =>
        a [IllegalArgumentException] must be thrownBy {
          new BrandNewCar(id, "title", Fuel.Diesel, 0)
        }
      }
    }

    "fail on null or empty title" in {
      List(null, "").foreach { title =>
        a [IllegalArgumentException] must be thrownBy {
          new BrandNewCar("1", title, Fuel.Diesel, 0)
        }
      }
    }

    "fail on null fuel" in {
      a [IllegalArgumentException] must be thrownBy {
        new BrandNewCar("1", "title", null, 0)
      }
    }

    "fail on negative price" in {
      a [IllegalArgumentException] must be thrownBy {
        new BrandNewCar("1", "title", Fuel.Gasoline, -1)
      }
    }
  }

}