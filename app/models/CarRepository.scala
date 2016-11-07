package models

import controllers.CarForm

trait CarRepository {
  def addNew(form: CarForm): Option[Long]

  def addUsed(form: CarForm): Option[Long]

  def find(id: Long): Option[Car]

  def replace(car: Car): Int

  def exists(id: Long): Boolean

}
