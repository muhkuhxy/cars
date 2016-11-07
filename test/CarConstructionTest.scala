import org.scalatestplus.play._
import models._
import java.time._

import controllers.AdvertForm

class CarConstructionTest extends PlaySpec {
  val are = afterWord("are")

  "A brand new car" when {

    "created" must {
      val validInput = (1, "super nice car", Fuel.Gasoline, 5000)
      val buildCar = BrandNewCar.tupled

      "succeed on valid values" in {
        val car = buildCar(validInput)
        car mustBe a[BrandNewCar]
        car.id mustBe validInput._1
        car.title mustBe validInput._2
        car.fuel mustBe validInput._3
        car.price mustBe validInput._4
      }

      "fail on invalid values" which are {
        Seq(
          ("title is null", validInput.copy(_2 = null)),
          ("title is empty", validInput.copy(_2 = "")),
          ("fuel is null", validInput.copy(_3 = null)),
          ("price is negative", validInput.copy(_4 = -1))
        ).foreach { case (condition, args) =>
          condition in {
            a[IllegalArgumentException] must be thrownBy {
              buildCar(args)
            }
          }
        }
      }
    }

  }

  "A used car" when {

    "created" must {
      val registration = LocalDate.of(2011, Month.DECEMBER, 13)
      val validInput = (1, "super nice car", Fuel.Gasoline, 5000, 12026, registration)
      val buildCar = UsedCar.tupled

      "succeed on valid values" in {
        val car = buildCar(validInput)
        car mustBe a[UsedCar]
        val usedCar = car.asInstanceOf[UsedCar]
        usedCar.id mustBe validInput._1
        usedCar.title mustBe validInput._2
        usedCar.fuel mustBe validInput._3
        usedCar.price mustBe validInput._4
        usedCar.mileage mustBe validInput._5
        usedCar.firstRegistration mustBe validInput._6
      }

      "fail on invalid values" which are {
        Seq(
          ("title is null", validInput.copy(_2 = null)),
          ("title is empty", validInput.copy(_2 = "")),
          ("fuel is null", validInput.copy(_3 = null)),
          ("price is negative", validInput.copy(_4 = -1)),
          ("mileage is negative", validInput.copy(_5 = -1)),
          ("registration is null", validInput.copy(_6 = null))
        ).foreach { case (condition, args) =>
          condition in {
            a[IllegalArgumentException] must be thrownBy {
              buildCar(args)
            }
          }
        }
      }
    }

  }

  "A form for a car" must {
    "accept valid values" in {
      val validNewCar = AdvertForm("new car", Fuel.Diesel, 10000, true, None, None)
      val validUsedCar = AdvertForm("used car", Fuel.Gasoline, 5000, false, Some(123000), Some(LocalDate.now()))
    }

    "refuse empty title" in {
      a[IllegalArgumentException] must be thrownBy {
        AdvertForm("", Fuel.Diesel, 10000, true, None, None)
      }
    }

    "refuse null fuel" in {
      a[IllegalArgumentException] must be thrownBy {
        AdvertForm("title", null, 10000, true, None, None)
      }
    }

    "refuse negative price" in {
      a[IllegalArgumentException] must be thrownBy {
        AdvertForm("new car", Fuel.Diesel, -1, true, None, None)
      }
    }

    "refuse null mileage" in {
      a[IllegalArgumentException] must be thrownBy {
        AdvertForm("new car", Fuel.Diesel, 10000, true, null, None)
      }
    }

    "refuse null registration date" in {
      a[IllegalArgumentException] must be thrownBy {
        AdvertForm("new car", Fuel.Diesel, 10000, true, None, null)
      }
    }

    "refuse used car without registration date" in {
      a[IllegalArgumentException] must be thrownBy {
        AdvertForm("used car", Fuel.Diesel, 10000, false, Some(123000), None)
      }
    }

    "refuse used car without mileage" in {
      a[IllegalArgumentException] must be thrownBy {
        AdvertForm("used car", Fuel.Diesel, 10000, false, None, Some(LocalDate.now))
      }
    }

    "refuse used car with negative mileage" in {
      a[IllegalArgumentException] must be thrownBy {
        AdvertForm("used car", Fuel.Diesel, 10000, false, Some(-1), Some(LocalDate.now))
      }
    }
  }

}
