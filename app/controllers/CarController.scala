package controllers

import play.api.mvc._

class CarController extends Controller {

  def create = Action { request =>
    Created("created")
  }

}