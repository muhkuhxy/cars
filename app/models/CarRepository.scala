package models

trait CarRepository {
  def addNew(form: AdvertForm): Option[Long]

  def addUsed(form: AdvertForm): Option[Long]

  def find(id: Long): Option[CarAdvert]

  def findAll: Seq[CarAdvert]

  def replace(car: CarAdvert): Int

  def exists(id: Long): Boolean

  def remove(id: Long): Int
}
