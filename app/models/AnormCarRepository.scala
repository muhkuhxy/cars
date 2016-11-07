package models

import java.util.Date

import anorm.SqlParser._
import anorm.{~, _}
import controllers.CarForm
import play.api.Logger
import play.api.db.DB
import play.api.Play.current

class AnormCarRepository extends CarRepository with DateConversions {

  override def addNew(form: CarForm): Option[Long] = DB.withConnection { implicit c =>
    SQL"insert into cars (title, fuel, price, new) values (${form.title}, ${form.fuel.toString}, ${form.price}, ${form.`new`});"
      .executeInsert()
  }

  override def addUsed(form: CarForm): Option[Long] = DB.withConnection { implicit c =>
    SQL"""insert into cars (title, fuel, price, new, mileage, firstRegistration)
         values (${form.title}, ${form.fuel.toString}, ${form.price}, ${form.`new`},
        ${form.mileage}, ${form.firstRegistration map toDate _});
       """.executeInsert()
  }

  private val newCarParser = int("id") ~ str("title") ~ (str("fuel") map Fuel.apply _) ~ int("price")
  private val usedCarParser = newCarParser ~ get[Option[Int]]("mileage") ~ (get[Option[Date]]("firstRegistration"))

  override def find(id: Long): Option[Car] = DB.withConnection { implicit c =>
    SQL"select new from cars where id = $id".as(bool("new").singleOpt) map { isNew: Boolean =>
      if(isNew) {
        val tuple = SQL"select id, title, fuel, price from cars where id = $id"
          .as(newCarParser.map(flatten).single)
        BrandNewCar.tupled(tuple)
      }
      else {
        val parser = usedCarParser map flatten map {
          case (id, title, fuel, price, Some(mileage), Some(firstRegistration)) =>
            UsedCar(id, title, fuel, price, mileage, toLocalDate(firstRegistration))
          case broken => throw new RuntimeException(s"stored advert with id $id is inconsistent $broken")
        }
        SQL"select id, title, fuel, price, mileage, firstRegistration from cars where id = $id"
          .as(parser.single)
      }
    }
  }

  override def replace(car: Car): Int = DB.withConnection { implicit c =>
    val query = car match {
      case BrandNewCar(id, title, fuel, price) =>
        SQL"""update cars set title = $title, fuel = ${fuel.toString}, price = $price,
              mileage = ${Option.empty[Int]}, firstRegistration = ${Option.empty[Date]},
              new = ${true}
              where id = $id"""
      case UsedCar(id, title, fuel, price, mileage, firstRegistration) =>
        SQL"""update cars set title = $title, fuel = ${fuel.toString}, price = $price,
              mileage = ${mileage}, firstRegistration = ${toDate(firstRegistration)},
              new = ${false}
              where id = $id"""
    }
    query.executeUpdate()
  }

  override def exists(id: Long): Boolean = DB.withConnection { implicit c =>
    SQL"select id from cars where id = $id".as(int("id").singleOpt).nonEmpty
  }

  override def remove(id: Long): Int = DB.withConnection { implicit c =>
    SQL"delete from cars where id = $id".executeUpdate()
  }

  override def findAll: Seq[Car] = DB.withConnection { implicit c =>
    val parser = usedCarParser ~ bool("new") map {
      case id ~ title ~ fuel ~ price ~ Some(mileage) ~ Some(date) ~ true =>
        UsedCar(id, title, fuel, price, mileage, toLocalDate(date))
      case id ~ title ~ fuel ~ price ~ None ~ None ~ false =>
        BrandNewCar(id, title, fuel, price)
      case broken =>
        throw new RuntimeException(s"stored advert is inconsistent: $broken")
    }
    SQL"select new, id, title, fuel, price, mileage, firstRegistration from cars"
      .as(parser.*)

  }
}