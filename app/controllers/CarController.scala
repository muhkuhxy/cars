package controllers

import java.time.LocalDate
import java.util.NoSuchElementException
import javax.inject.Inject

import models._
import play.api.Logger
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.mvc._

import scala.util.{Failure, Success, Try}

class CarController @Inject()(private val service: CarService) extends Controller with JsonConversions {

  val sortable = Set("id", "title", "fuel", "price", "new", "mileage", "firstRegistration")

  def list(sort: String, ascending: Boolean) = Action { request =>
    if (!sortable.contains(sort)) {
      BadRequest(message(s"cannot sort by $sort"))
    }
    else {
      val cars: Seq[CarAdvert] = service.findAll(sort, ascending)
      Ok(Json.toJson(cars))
    }
  }

  def create = Action(BodyParsers.parse.json) { request =>
    request.body.validate[AdvertForm].fold(
      errors => {
        BadRequest(message(errors))
      },
      form => {
        Try(service.add(form)) match {
          case Success(Some(id)) => Created(message("created"))
            .withHeaders(("location", s"/car/$id"))
          case Success(None) => InternalServerError
          case Failure(e: IllegalArgumentException) =>
            BadRequest(message(e.getMessage))
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
    request.body.validate[CarAdvert].fold(
      errors => BadRequest(message(errors)),
      car => {
        Try(service.replace(id, car)) match {
          case Success(()) => Ok(message("advert replaced"))
          case Failure(e: IllegalArgumentException) => BadRequest(message(e.getMessage))
          case Failure(e: NoSuchElementException) => NotFound(message(s"unknown advert $id"))
          case Failure(e: Exception) => {
            Logger.error(e.getMessage)
            InternalServerError
          }
        }
      })
  }

  def remove(id: Long) = Action { request =>
    Try(service.remove(id)) match {
      case Success(()) => Ok(message("advert removed"))
      case Failure(e: NoSuchElementException) => NotFound(message(s"unknown advert $id"))
      case Failure(_: Exception) => InternalServerError
    }
  }

  private def message(msg: String) = Json.obj("message" -> msg)

  private def message(errors: Seq[(JsPath, Seq[ValidationError])]) =
    Json.obj("message" -> JsError.toJson(errors))

}

