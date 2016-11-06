import controllers.CarForm
import models.CarRepository

class MockCarRepository extends CarRepository {
  private var maxId: Int = 0
  override def add(form: CarForm): Int = {
    maxId += 1
    maxId
  }

}
