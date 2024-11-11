
import org.mongodb.scala._
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result._
import org.mongodb.scala.model.Updates._

object Observables {

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("<connection string URI>")
    val database: MongoDatabase = mongoClient.getDatabase("<database name>")
    val collection: MongoCollection[Document] = database.getCollection("<collection name>")

    // start-read-observable
    val filter = equal("cuisine", "Ethiopian")
    val observable: Observable[Document] = collection.find(filter)

    observable.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed: " + e.getMessage)
      override def onComplete(): Unit = println("Completed")
    })
    // end-read-observable

    // start-write-observable
    val docs: Seq[Document] = Seq(
      Document("name" -> "Cafe Himalaya", "cuisine" -> "Nepalese"),
      Document("name" -> "Taste From Everest", "cuisine" -> "Nepalese")
    )
    val observable: Observable[InsertManyResult] = collection.insertMany(docs)

    observable.subscribe(new Observer[InsertManyResult] {
      override def onNext(result: InsertManyResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed: " + e.getMessage)
      override def onComplete(): Unit = println("Processed all results")
    })
    // end-write-observable

    // start-lambda
    collection.distinct("borough")
              .subscribe((value: String) => println(value),
                         (e: Throwable) => println(s"Failed: $e"),
                         () => println("Processed all results"))
    // end-lambda

    // Wait for the operations to complete before closing client
    Thread.sleep(1000)
    mongoClient.close()
  }
}
