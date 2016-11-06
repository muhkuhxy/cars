import java.time.{LocalDate, Month}

import controllers.{CarForm, CarService}
import models.Fuel
import org.scalatestplus.play.PlaySpec

class CarServiceTest extends PlaySpec {

  "The car service" must {
    "make sure car forms are consistent" in {
      val service = new CarService(new MockCarRepository())
      a[IllegalArgumentException] must be thrownBy {
        service.add(CarForm("title",Fuel.Gasoline,123,false,Some(123),None))
      }
      a[IllegalArgumentException] must be thrownBy {
        service.add(CarForm("title",Fuel.Gasoline,123,false,None,Some(LocalDate.of(2016, Month.DECEMBER, 13))))
      }
      a[IllegalArgumentException] must be thrownBy {
        service.add(CarForm("title",Fuel.Gasoline,123,true,Some(123),Some(LocalDate.of(2016, Month.DECEMBER, 13))))
      }
      a[IllegalArgumentException] must be thrownBy {
        service.add(CarForm("title",Fuel.Gasoline,123,true,None,Some(LocalDate.of(2016, Month.DECEMBER, 13))))
      }
      service.add(CarForm("title",Fuel.Gasoline,123,false,Some(123),Some(LocalDate.of(2016, Month.DECEMBER, 13)))) mustBe Some(1)
      service.add(CarForm("title",Fuel.Gasoline,123,true,None,None)) mustBe Some(2)
    }
  }

}