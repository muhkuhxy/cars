package modules

import com.google.inject.AbstractModule
import models.{AnormCarRepository, CarRepository}

class MyModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[CarRepository]).to(classOf[AnormCarRepository])
  }
}