package models

import controllers.AdvertForm

trait CarRepository {
  def addNew(form: AdvertForm): Option[Long]

  def addUsed(form: AdvertForm): Option[Long]

  def find(id: Long): Option[Car]

  def findAll: Seq[Car]

  def replace(car: Car): Int

  def exists(id: Long): Boolean

  def remove(id: Long): Int
}
