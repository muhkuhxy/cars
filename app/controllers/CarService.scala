package controllers

import com.google.inject.Inject
import models.{Car, CarRepository}

class CarService @Inject()(repo: CarRepository) {

  def add(form: CarForm): Option[Long] = {
    if (form.`new`) {
      repo.addNew(form)
    }
    else {
      repo.addUsed(form)
    }
  }

  def find(id: Long) = repo.find(id)

  def findAll = repo.findAll

  def replace(id: Long, car: Car): Unit = {
    require(id == car.id, "id cannot be changed, remove and create new advert")
    if (!repo.exists(id)) {
      throw new NoSuchElementException
    }
    else {
      val rowsAffected = repo.replace(car)
      if(rowsAffected != 1) {
        throw new IllegalStateException(
          s"expected to update 1 row, but actually updated $rowsAffected on car $car")
      }
    }
  }

  def remove(id: Long): Unit = {
    if (!repo.exists(id)) {
      throw new NoSuchElementException
    }
    else {
      val rowsAffected = repo.remove(id)
      if(rowsAffected != 1) {
        throw new IllegalStateException(
          s"expected to remove 1 row, but actually removed $rowsAffected on id $id")
      }
    }
  }

}
