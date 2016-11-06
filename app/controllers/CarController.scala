package controllers

import javax.inject.Inject

import models._
import play.api.libs.json._
import play.api.mvc._

import scala.util.{Failure, Success, Try}

class CarController @Inject()(private val service: CarService) extends Controller with JsonConversions {

  def create = Action(BodyParsers.parse.json) { request =>
    request.body.validate[CarForm].fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      form => {
        Try(service.add(form)) match {
          case Success(Some(id)) => Created(Json.obj("message" -> "created"))
            .withHeaders(("location", s"/car/$id"))
          case Success(None) => InternalServerError
          case Failure(e: IllegalArgumentException) =>
            BadRequest(Json.obj("message" -> e.getMessage))
          case Failure(_) => InternalServerError
        }
      })
  }

  def get(id: Long) = Action {
    service.find(id) match {
      case Some(car) => Ok(Json.toJson(car))
      case None => NotFound
    }
  }

  def update(id: Long) = Action(BodyParsers.parse.json) { request =>
    request.body.validate[Car].fold(
      errors => BadRequest(Json.obj("message" -> JsError.toJson(errors))),
      car => {
        if (service.replace(id, car)) {
          Ok(Json.obj("message" -> "advert replaced"))
        }
        else {
          InternalServerError
        }
      })
  }

}

