package db

import slick.dbio.DBIO
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

object DbHelper {

  // https://books.underscore.io/essential-slick/essential-slick-3.html
  def exec[T](action: DBIO[T]): T = {
    val db = Database.forConfig("dbConfig")
    Await.result(db.run(action), 2.seconds)
  }
}
