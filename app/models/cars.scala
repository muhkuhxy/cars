package models

import java.time.LocalDate

import models.Fuel.Type

object Fuel {

  val names: Map[Type, String] = Map(
    Gasoline -> Gasoline.toString,
    Diesel -> Diesel.toString
  )

  val values = names map { case (k, v) => v -> k }

  sealed trait Type

  case object Gasoline extends Type

  case object Diesel extends Type

  def apply(value: String) = values(value)
}

case class AdvertForm(val title: String,
                      val fuel: Fuel.Type,
                      val price: Int,
                      val `new`: Boolean,
                      val mileage: Option[Int],
                      val firstRegistration: Option[LocalDate]) {
  require(title != null && !title.isEmpty)
  require(fuel != null)
  require(price >= 0)

  require(mileage != null)
  require(firstRegistration != null)
  if (`new`) {
    require(firstRegistration.isEmpty, "new cars must not have firstRegistration set")
    require(mileage.isEmpty, "new cars must not have mileage set")
  }
  else {
    require(firstRegistration.nonEmpty, "used cars must have firstRegistration set")
    require(mileage.nonEmpty, "used cars must have mileage set")
    require(mileage.get >= 0, "mileage must be >= 0")
  }
}

class CarAdvert(val id: Int,
                title: String,
                fuel: Fuel.Type,
                price: Int,
                `new`: Boolean,
                mileage: Option[Int],
                firstRegistration: Option[LocalDate])
  extends AdvertForm(title, fuel, price, `new`, mileage, firstRegistration) {

  override def toString = s"CarAdvert($id, $title, $fuel, $price, ${`new`}, $mileage, $firstRegistration)"

}

object CarAdvert {

  def unapply(arg: CarAdvert): Option[(Int, String, Type, Int, Boolean, Option[Int], Option[LocalDate])] =
    Some((arg.id, arg.title, arg.fuel, arg.price, arg.`new`, arg.mileage, arg.firstRegistration))

  def apply(id: Int,
            title: String,
            fuel: Type,
            price: Int,
            `new`: Boolean,
            mileage: Option[Int],
            firstRegistration: Option[LocalDate]): CarAdvert =
    new CarAdvert(id, title, fuel, price, `new`, mileage, firstRegistration)
}

