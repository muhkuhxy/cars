package controllers

import javax.inject.Inject

import models._
import play.api.http.Writeable
import play.api.libs.json.{JsString, JsValue, Json, Writes}
import play.api.mvc._

class CarController @Inject() (private val repository: CarRepository) extends Controller with Forms {

  def create = Action(BodyParsers.parse.json) { request =>
    request.body.validate[CarForm].fold(
      errors => {
        BadRequest("Invalid request")
      },
      form => {
        val id = repository.add(form)
        Created("Created").withHeaders(("location", s"/car/$id"))
      })
  }

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

  def get(id: Int) = Action {
    repository.find(id) match {
      case Some(car) => Ok(Json.toJson(car))
      case None => NotFound
    }
  }

}

