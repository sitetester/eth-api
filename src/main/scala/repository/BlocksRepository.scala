package repository

import db._
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

object BlocksRepository {
  val blocks = TableQuery[BlocksTable]
  val transactions = TableQuery[TransactionsTable]

  def getLastBlockNumber: Option[Int] = {
    val q = blocks.sortBy(_.number.desc).map(_.number).take(1)

    // Anything we run against a database is a DBIO[T] (or a DBIOAction, more generally).
    // https://books.underscore.io/essential-slick/essential-slick-3.html
    val action = q.result
    // println(action.statements.mkString)

    val result = DbHelper.exec(q.result)
    if (result.isEmpty) Some(-1) else Some(result.head)
  }

  def getBlocksInRange(from: Int, to: Int): Seq[Block] = {
    val q = blocks.drop(from).take(to)

    // Anything we run against a database is a DBIO[T] (or a DBIOAction, more generally).
    // https://books.underscore.io/essential-slick/essential-slick-3.html
    val action = q.result
    println(action.statements.mkString)

    DbHelper.exec(q.result)
  }

  def getBlockByNumber(blockNumber: Int): Option[Block] = {
    val q = blocks.filter(_.number === blockNumber)

    // Anything we run against a database is a DBIO[T] (or a DBIOAction, more generally).
    // https://books.underscore.io/essential-slick/essential-slick-3.html
    val action = q.result
    println(action.statements.mkString)

    val result = DbHelper.exec(q.result)
    result.headOption
  }

  def getTransactionsBlockByNumber(blockNumber: Int): Seq[Transaction] = {
    val q = transactions.filter(_.blockNumber === blockNumber)

    // Anything we run against a database is a DBIO[T] (or a DBIOAction, more generally).
    // https://books.underscore.io/essential-slick/essential-slick-3.html
    val action = q.result
    println(action.statements.mkString)

    DbHelper.exec(q.result)
  }
}
