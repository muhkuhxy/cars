package models

import anorm.SqlParser._
import anorm._
import controllers.CarForm
import play.api.db.DB
import play.api.Play.current

class AnormCarRepository extends CarRepository with DateConversions {

  override def addNew(form: CarForm): Option[Long] = DB.withConnection { implicit c =>
    SQL"insert into cars (title, fuel, price, new) values (${form.title}, ${form.fuel.toString}, ${form.price}, ${form.`new`});"
      .executeInsert()
  }

  override def find(id: Long): Option[Car] = DB.withConnection { implicit c =>
    SQL"select new from cars where id = $id".as(bool("new").singleOpt) map { isNew: Boolean =>
      if(isNew) {
        val tuple = SQL"select id, title, fuel, price from cars where id = $id"
          .as((int("id") ~ str("title") ~ (str("fuel") map Fuel.apply _) ~ int("price")).map(flatten).single)
        BrandNewCar.tupled(tuple)
      }
      else {
        ???
      }
    }
  }

}