package models

import controllers.CarForm

trait CarRepository {
  def addNew(form: CarForm): Option[Long]

  def addUsed(form: CarForm): Option[Long]

  def find(id: Long): Option[Car]

  def findAll: Seq[Car]

  def replace(car: Car): Int

  def exists(id: Long): Boolean

  def remove(id: Long): Int
}
