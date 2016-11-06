package models

import controllers.CarForm

trait CarRepository {
  def add(form: CarForm): Int
}
