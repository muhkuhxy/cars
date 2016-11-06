package controllers

import java.time.LocalDate

import models.{BrandNewCar, Car, Fuel, UsedCar}
import play.api.libs.json._

trait JsonConversions {

  implicit val fuelReads: Reads[Fuel.Type] = new Reads[Fuel.Type] {
    override def reads(json: JsValue): JsResult[Fuel.Type] = json match {
      case JsString("Gasoline") => JsSuccess(Fuel.Gasoline)
      case JsString("Diesel") => JsSuccess(Fuel.Diesel)
      case _ => JsError(s"invalid fuel type. valid values are Diesel and Gasoline")
    }
  }

  implicit val carFormReads = Json.reads[CarForm]

  implicit val fuelWrite = new Writes[Fuel.Type] {
    override def writes(fuel: Fuel.Type): JsValue = JsString(fuel.toString)
  }

  val newCarWrite = Json.writes[BrandNewCar]

  val usedCarWrite = Json.writes[UsedCar]

  implicit val carWrites: Writes[Car] = new Writes[Car] {
    override def writes(car: Car): JsValue = car match {
      case bnc: BrandNewCar => Json.toJson(bnc)(newCarWrite)
      case uc: UsedCar => Json.toJson(uc)(usedCarWrite)
    }
  }

}

case class CarForm(title: String,
                   fuel: Fuel.Type,
                   price: Int,
                   `new`: Boolean,
                   mileage: Option[Int],
                   registrationDate: Option[LocalDate])