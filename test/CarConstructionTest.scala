import java.time._

import models._
import org.scalatestplus.play._

class CarConstructionTest extends PlaySpec {
  val are = afterWord("are")

  "A car car advert" when {

    "created" must {
      val validNewCarInput = (1, "super nice car", Fuel.Gasoline, 5000, true, None, None)
      val validUsedCarInput = (2, "super used car", Fuel.Diesel, 2500, false, Some(120000), Some(LocalDate.now))
      val buildCar = (CarAdvert.apply _).tupled

      "succeed on valid values" in {
        val newCar = buildCar(validNewCarInput)
        val Some(values) = CarAdvert.unapply(newCar)
        values mustBe validNewCarInput

        val usedCar = buildCar(validUsedCarInput)
        val Some(usedValues) = CarAdvert.unapply(usedCar)
        usedValues mustBe validUsedCarInput
      }

      "fail on invalid values" which are {
        Seq(
          ("title is null", validNewCarInput.copy(_2 = null)),
          ("title is empty", validNewCarInput.copy(_2 = "")),
          ("fuel is null", validNewCarInput.copy(_3 = null)),
          ("price is negative", validNewCarInput.copy(_4 = -1)),
          ("used and mileage missing", validUsedCarInput.copy(_6 = None)),
          ("used and firstRegistration missing", validUsedCarInput.copy(_7 = None)),
          ("negative mileage", validUsedCarInput.copy(_6 = Some(-1)))
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
