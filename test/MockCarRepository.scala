import com.google.inject.Inject
import controllers.CarForm
import models.{Car, CarRepository}

class MockCarRepository @Inject() (val cars: Seq[Car] = Seq()) extends CarRepository {
  private var maxId: Int = 0
  override def add(form: CarForm): Option[Long] = {
    maxId += 1
    Some(maxId)
  }

  override def find(id: Long): Option[Car] = {
    cars.find(id == _.id)
  }

}
