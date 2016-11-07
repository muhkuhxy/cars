package controllers

import java.time.LocalDate

import models._
import play.api.Logger
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.libs.functional.syntax._

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

  implicit val fuelWrite = new Writes[Fuel.Type] {
    override def writes(fuel: Fuel.Type): JsValue = JsString(fuel.toString)
  }

  implicit val advertFormReads = new Reads[AdvertForm] {
    import Reads._

    def newnessReads: Reads[Option[(Int, LocalDate)]] = new Reads[Option[(Int, LocalDate)]] {
      override def reads(json: JsValue): JsResult[Option[(Int, LocalDate)]] = {
        val reader = (__ \ "mileage").readNullable[Int] and
          (__ \ "firstRegistration").readNullable[LocalDate] and
          (__ \ "new").read[Boolean]
        reader.apply(mapping _).reads(json).fold(
          err => JsError(err),
          s => s match {
            case Left(error) => error
            case Right(s: Some[_]) => JsSuccess(s)
            case Right(None) => JsSuccess(None)
          }
        )
      }

      def mapping(m: Option[Int], f: Option[LocalDate], n: Boolean): Either[JsError,Option[(Int, LocalDate)]] = (m, f, n) match {
        case (Some(mileage), Some(firstRegistration), false) if mileage >= 0 =>
          Right(Some(mileage, firstRegistration))
        case (Some(_), Some(_), false) =>
          Left(JsError(__ \ "mileage", "mileage must be >= 0"))
        case (None, None, true) => Right(None)
        case _ => Left(JsError(JsPath(), "invalid new/milage/firstRegistration combination"))
      }
    }

    override def reads(json: JsValue): JsResult[AdvertForm] = (
      (__ \ "title").read[String](minLength[String](1)) and
        (__ \ "fuel").read[Fuel.Type] and
        (__ \ "price").read[Int](min(0)) and
      __.read[Option[(Int, LocalDate)]](newnessReads)
      )((title: String, fuel: Fuel.Type, price: Int, newness: Option[(Int, LocalDate)]) => {
        newness match {
          case Some((mileage, firstRegistration)) => AdvertForm(title, fuel, price, false, Some(mileage), Some(firstRegistration))
          case None => AdvertForm(title, fuel, price, true, None, None)
        }
    }).reads(json)
  }

  implicit val advertWrites = Json.writes[CarAdvert]

  implicit val advertReads: Reads[CarAdvert] = (
    (__ \ "id").read[Int] and __.read[AdvertForm]
    )((id: Int, form: AdvertForm) => {
    form match {
      case AdvertForm(title, fuel, price, newness, mileage, firstRegistration) =>
        CarAdvert(id, title, fuel, price, newness, mileage, firstRegistration)
    }
  })

}
