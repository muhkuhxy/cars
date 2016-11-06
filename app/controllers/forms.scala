package controllers

import java.time.LocalDate

import models.Fuel
import play.api.libs.json._

trait Forms {

  implicit val fuelReads: Reads[Fuel.Type] = new Reads[Fuel.Type] {
    override def reads(json: JsValue): JsResult[Fuel.Type] = json match {
      case JsString("Gasoline") => JsSuccess(Fuel.Gasoline)
      case JsString("Diesel") => JsSuccess(Fuel.Diesel)
      case _ => JsError(s"invalid fuel type. valid values are Diesel and Gasoline")
    }
  }
  implicit val carFormReads = Json.reads[CarForm]

}

case class CarForm(title: String,
                   fuel: Fuel.Type,
                   price: Int,
                   `new`: Boolean,
                   mileage: Option[Int],
                   registrationDate: Option[LocalDate])
