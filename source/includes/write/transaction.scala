import org.mongodb.scala._
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Filters._
import scala.concurrent.duration.Duration
import org.mongodb.scala.result._

object Transaction {
// begin-transaction-method
  def runTransaction(
      database: MongoDatabase,
      observable: SingleObservable[ClientSession]
  ): SingleObservable[ClientSession] = {
    observable.map(clientSession => {
      val moviesCollection = database.getCollection("movies")
      val usersCollection = database.getCollection("users")

      val transactionOptions = TransactionOptions
        .builder()
        .readPreference(ReadPreference.primary())
        .readConcern(ReadConcern.SNAPSHOT)
        .writeConcern(WriteConcern.MAJORITY)
        .build()

      // Starts the transaction with specified options
      clientSession.startTransaction(transactionOptions)

      // Inserts a document into the "movies" collection  
      moviesCollection
        .insertOne(
          clientSession,
          Document("name" -> "The Menu", "runtime" -> 107)
        )
        .subscribe((res: InsertOneResult) => println(res))

      // Updates a document in the "users" collection
      usersCollection
        .updateOne(clientSession, equal("name", "Amy Ryan"), set("name", "Amy Phillips"))
        .subscribe((res: UpdateResult) => println(res))

      clientSession
    })
  }
// end-transaction-method

  def main(args: Array[String]): Unit = {

    // start-perform-transaction
    val client = MongoClient("<connection string>")
    val database = client.getDatabase("sample_mflix")
    val session = client.startSession();

    val transactionObservable: SingleObservable[ClientSession] =
      runTransaction(database, session)

    val commitTransactionObservable: SingleObservable[Unit] =
      transactionObservable.flatMap(clientSession => clientSession.commitTransaction())

    Await.result(commitTransactionObservable.toFuture(), Duration(10, TimeUnit.SECONDS))
    // end-perform-transaction

    // Keep the main thread alive long enough for the asynchronous operations to complete
    Thread.sleep(1000)
    client.close()
  }

}