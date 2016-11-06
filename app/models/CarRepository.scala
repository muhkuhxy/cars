package models

import controllers.CarForm

trait CarRepository {
  def addNew(form: CarForm): Option[Long]
  def find(id: Long): Option[Car]
}
