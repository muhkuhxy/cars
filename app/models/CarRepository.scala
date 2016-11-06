package models

import controllers.CarForm

trait CarRepository {
  def add(form: CarForm): Option[Long]
  def find(id: Long): Option[Car]
}
