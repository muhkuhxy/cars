package controllers

import javax.inject.Inject

import models._
import play.api.libs.json.{JsString, JsValue, Json, Writes}
import play.api.mvc._

class CarController @Inject() (private val repository: CarRepository) extends Controller with JsonConversions {

  def create = Action(BodyParsers.parse.json) { request =>
    request.body.validate[CarForm].fold(
      errors => {
        BadRequest("Invalid request")
      },
      form => {
        repository.add(form) match {
          case Some(id) => Created("Created").withHeaders(("location", s"/car/$id"))
          case None => InternalServerError
        }

      })
  }

  def get(id: Long) = Action {
    repository.find(id) match {
      case Some(car) => Ok(Json.toJson(car))
      case None => NotFound
    }
  }

}

