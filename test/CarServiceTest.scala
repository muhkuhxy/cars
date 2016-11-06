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
    }
  }

}