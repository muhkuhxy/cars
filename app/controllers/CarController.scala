package controllers

import javax.inject.Inject

import models.CarRepository
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

}

