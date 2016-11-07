package models

import java.time.LocalDate

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

sealed trait Car {

  require(title != null && !title.isEmpty)
  require(fuel != null)
  require(price >= 0)

  val id: Int

  val title: String

  val fuel: Fuel.Type

  val price: Int
}

case class BrandNewCar(val id: Int,
                       val title: String,
                       val fuel: Fuel.Type,
                       val price: Int) extends Car


case class UsedCar(val id: Int,
                   val title: String,
                   val fuel: Fuel.Type,
                   val price: Int,
                   val mileage: Int,
                   val firstRegistration: LocalDate) extends Car {
  require(mileage >= 0)
  require(firstRegistration != null)
}
