package models

import java.time.LocalDate

//Car adverts should have the following fields:
//* **id** (_required_): **int** or **guid**, choose whatever is more convenient for you;
//* **title** (_required_): **string**, e.g. _"Audi A4 Avant"_;
//* **fuel** (_required_): gasoline or diesel, use some type which could be extended in the future by adding additional fuel types;
//* **price** (_required_): **integer**;
//* **new** (_required_): **boolean**, indicates if car is new or used;
//* **mileage** (_only for used cars_): **integer**;
//* **first registration** (_only for used cars_): **date** without time.

object Fuel {

  sealed trait Type

  case object Gasoline extends Type

  case object Diesel extends Type

}

sealed trait Car {

  require(id != null && !id.isEmpty)
  require(title != null && !title.isEmpty)
  require(fuel != null)
  require(price >= 0)

  def id: String

  def title: String

  def fuel: Fuel.Type

  def price: Int
}

case class BrandNewCar(val id: String,
                       val title: String,
                       val fuel: Fuel.Type,
                       val price: Int) extends Car {

}

case class UsedCar(val id: String,
                   val title: String,
                   val fuel: Fuel.Type,
                   val price: Int,
                   val mileage: Int,
                   val firstRegistration: LocalDate) extends Car {
  require(mileage >= 0)
  require(firstRegistration != null)
}
