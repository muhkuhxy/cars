package controllers

import java.time.LocalDate
import javax.inject.Inject

import models.Fuel
import play.api.libs.json._
import play.api.mvc._

class CarController @Inject() extends Controller {

  implicit val fuelReads: Reads[Fuel.Type] = new Reads[Fuel.Type] {
    override def reads(json: JsValue): JsResult[Fuel.Type] = json match {
      case JsString("Gasoline") => JsSuccess(Fuel.Gasoline)
      case JsString("Diesel") => JsSuccess(Fuel.Diesel)
      case _ => JsError(s"invalid fuel type. valid values are Diesel and Gasoline")
    }
  }
  implicit val carFormReads = Json.reads[CarForm]

  def create = Action(BodyParsers.parse.json) { request =>
    request.body.validate[CarForm].fold(
      errors => {
        BadRequest("Invalid request")
      },
      form => {
        Created("Created").withHeaders(("location", "/car/1"))
      })
  }

}

case class CarForm(title: String,
                   fuel: Fuel.Type,
                   price: Int,
                   `new`: Boolean,
                   mileage: Option[Int],
                   registrationDate: Option[LocalDate])
