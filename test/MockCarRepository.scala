import com.google.inject.Inject
import controllers.AdvertForm
import models.{Car, CarRepository}

class MockCarRepository @Inject() (var cars: Seq[Car] = Seq()) extends CarRepository {
  private var maxId: Int = 0
  override def addNew(form: AdvertForm): Option[Long] = {
    maxId += 1
    Some(maxId)
  }

  override def addUsed(form: AdvertForm): Option[Long] = addNew(form)

  override def find(id: Long): Option[Car] = {
    cars.find(id == _.id)
  }

  override def exists(id: Long): Boolean = cars.find(id == _.id).nonEmpty

  override def remove(id: Long): Int = {
    val (removed, newCars) = cars.partition(id == _.id)
    cars = newCars
    removed.size
  }

  override def replace(car: Car): Int = 1

  override def findAll: Seq[Car] = ???
}
