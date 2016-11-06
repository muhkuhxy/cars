package controllers

import com.google.inject.Inject
import models.{Car, CarRepository}

class CarService @Inject() (repo: CarRepository) {

  def add(form: CarForm): Option[Long] = {
    if(form.`new`) {
      require(form.firstRegistration.isEmpty)
      require(form.mileage.isEmpty)
      repo.addNew(form)
    }
    else {
      require(form.firstRegistration.nonEmpty)
      require(form.mileage.nonEmpty)
      repo.addUsed(form)
    }
  }

  def find(id: Long) = repo.find(id)

  def replace(id: Long, car: Car): Boolean = {
    require(id == car.id)
    repo.replace(car)
  }

}
