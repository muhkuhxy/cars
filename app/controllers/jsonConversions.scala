package controllers

import java.time.LocalDate

import models.{BrandNewCar, Car, Fuel, UsedCar}
import play.api.libs.json._

trait JsonConversions {

  implicit val fuelReads: Reads[Fuel.Type] = new Reads[Fuel.Type] {
    def error(input: Any) = JsError(
      s"""invalid fuel type "${input}".
         |valid values are ${Fuel.names.values.mkString(", ")}""".stripMargin)

    override def reads(json: JsValue): JsResult[Fuel.Type] = json match {
      case JsString(s) => Fuel.values.get(s) match {
        case Some(fuel) => JsSuccess(fuel)
        case None => error(s)
      }
      case _ => error(json)
    }
  }

  implicit val carFormReads = Json.reads[AdvertForm]

  implicit val fuelWrite = new Writes[Fuel.Type] {
    override def writes(fuel: Fuel.Type): JsValue = JsString(fuel.toString)
  }

  val newCarFormat = Json.format[BrandNewCar]

  val usedCarFormat = Json.format[UsedCar]

  implicit val carFormat: Format[Car] = new Format[Car] {
    override def reads(json: JsValue): JsResult[Car] = {
      json.transform((__ \ 'new).json.pick[JsBoolean]) match {
        case JsSuccess(JsBoolean(true), _) => newCarFormat.reads(json)
        case JsSuccess(JsBoolean(false), _) => usedCarFormat.reads(json)
        case _ => JsError("missing \"new\" information")
      }
    }

    override def writes(car: Car): JsValue = {
      val common = Json.obj(
        "id" -> car.id,
        "title" -> car.title,
        "fuel" -> car.fuel,
        "price" -> car.price
      )
      car match {
        case bnc: BrandNewCar => common + ("new" -> JsBoolean(true))
        case uc: UsedCar =>
          common ++ Json.obj("new" -> false,
            "mileage" -> uc.mileage,
            "firstRegistration" -> uc.firstRegistration
          )
      }
    }
  }

}

case class AdvertForm(title: String,
                      fuel: Fuel.Type,
                      price: Int,
                      `new`: Boolean,
                      mileage: Option[Int],
                      firstRegistration: Option[LocalDate]) {
  require(title != null && !title.isEmpty)
  require(fuel != null)
  require(price >= 0)

  require(mileage != null)
  require(firstRegistration != null)
  if(`new`) {
    require(firstRegistration.isEmpty, "new cars must not have firstRegistration set")
    require(mileage.isEmpty, "new cars must not have mileage set")
  }
  else {
    require(firstRegistration.nonEmpty, "used cars must have firstRegistration set")
    require(mileage.nonEmpty, "used cars must have mileage set")
    require(mileage.get >= 0, "mileage must be >= 0")
  }
}
