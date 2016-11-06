package controllers

import com.google.inject.Inject
import models.CarRepository

class CarService @Inject() (repo: CarRepository) {

  def add(form: CarForm): Option[Long] = {
    if(form.`new`) {
      require(form.registrationDate.isEmpty)
      require(form.mileage.isEmpty)
      repo.addNew(form)
    }
    else {
      require(form.registrationDate.nonEmpty)
      require(form.mileage.nonEmpty)
//      repo.addUsed(form)
      ???
    }
  }

}
