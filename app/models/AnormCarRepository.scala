package models

import java.time.LocalDate
import java.util.Date

import anorm.SqlParser._
import anorm.{~, _}
import play.api.Play.current
import play.api.db.DB

class AnormCarRepository extends CarRepository with DateConversions {

  override def addNew(form: AdvertForm): Option[Long] = DB.withConnection { implicit c =>
    SQL"insert into cars (title, fuel, price, new) values (${form.title}, ${form.fuel.toString}, ${form.price}, ${form.`new`});"
      .executeInsert()
  }

  override def addUsed(form: AdvertForm): Option[Long] = DB.withConnection { implicit c =>
    SQL"""insert into cars (title, fuel, price, new, mileage, firstRegistration)
         values (${form.title}, ${form.fuel.toString}, ${form.price}, ${form.`new`},
        ${form.mileage}, ${form.firstRegistration map toDate _});
       """.executeInsert()
  }

  private val carParser = (int("id") ~ str("title") ~ (str("fuel") map Fuel.apply _) ~ int("price") ~
    bool("new") ~ get[Option[Int]]("mileage") ~ (get[Option[Date]]("firstRegistration"))) map {
    case id ~ title ~ fuel ~ price ~ isNew ~ mileage ~ date =>
      CarAdvert(id, title, fuel, price, isNew, mileage, date map toLocalDate _)
  }

  override def find(id: Long): Option[CarAdvert] = DB.withConnection { implicit c =>
    SQL"select id, title, fuel, price, new, mileage, firstRegistration from cars where id = $id"
      .as(carParser.singleOpt)
  }

  override def replace(car: CarAdvert): Int = DB.withConnection { implicit c =>
    SQL"""update cars set title = ${car.title}, fuel = ${car.fuel.toString}, price = ${car.price},
          new = ${car.`new`}, mileage = ${car.mileage},
          firstRegistration = ${car.firstRegistration map toDate _}
          where id = ${car.id}""".executeUpdate()
  }

  override def exists(id: Long): Boolean = DB.withConnection {
    implicit c =>
      SQL"select id from cars where id = $id".as(int("id").singleOpt).nonEmpty
  }

  override def remove(id: Long): Int = DB.withConnection {
    implicit c =>
      SQL"delete from cars where id = $id".executeUpdate()
  }

  override def findAll: Seq[CarAdvert] = DB.withConnection { implicit c =>
      SQL"select new, id, title, fuel, price, new, mileage, firstRegistration from cars"
        .as(carParser.*)
  }
}