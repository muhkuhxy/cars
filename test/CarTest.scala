import org.scalatestplus.play.PlaySpec
import models.Car

class CarTest extends PlaySpec {

  "A new car" must {
    "pass this dummy test" in {
      val json = ""
      new Car(json).json mustEqual json
    }
  }

}