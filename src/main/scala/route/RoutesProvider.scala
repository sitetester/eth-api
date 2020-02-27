package route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import db.{Block, Transaction}
import repository.BlocksRepository
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val blockNotFound: RootJsonFormat[BlockNotFound] = jsonFormat2(
    BlockNotFound
  )

  implicit val transactionsNotFound: RootJsonFormat[TransactionsNotFound] =
    jsonFormat2(TransactionsNotFound)

  implicit val blockFormat: RootJsonFormat[Block] = jsonFormat4(Block)
  implicit val transactionsFormat: RootJsonFormat[Transaction] = jsonFormat4(
    Transaction
  )
}

final case class BlockNotFound(id: Int, message: String = "Block not found")

final case class TransactionsNotFound(
  id: Int,
  message: String = "There are no transactions for given block number."
)

object RoutesProvider extends Directives with JsonSupport {

  def getRoute: Route = {

    val route =
      concat(
        path("blocks" / IntNumber / IntNumber) {
          (fromBlockNumber: Int, toBlockNumber: Int) =>
            var to = toBlockNumber
            if (toBlockNumber > 50) {
              to = 50
            }
            get {
              complete(
                BlocksRepository
                  .getBlocksInRange(fromBlockNumber, to)
              )
            }
        },
        path("blocks" / IntNumber) { blockNumber: Int =>
          get {
            val block = BlocksRepository.getBlockByNumber(blockNumber)
            complete(block match {
              case Some(block) => block
              case None        => BlockNotFound(blockNumber)
            })
          }
        },
        path("blocks" / IntNumber / "transactions") { blockNumber: Int =>
          val block =
            BlocksRepository.getBlockByNumber(blockNumber).getOrElse(-1)
          if (block == -1) {
            complete(BlockNotFound(blockNumber))
          }

          val txs = BlocksRepository.getTransactionsBlockByNumber(blockNumber)
          if (txs.nonEmpty) {
            complete(txs)
          } else {
            complete(TransactionsNotFound(blockNumber))
          }
        },
      )

    route
  }
}
