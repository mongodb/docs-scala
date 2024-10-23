mport org.mongodb.scala._
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.UpdateOptions
import org.mongodb.scala.model.Updates.{combine, rename, set}
import org.mongodb.scala.result.UpdateResult

object Aggregation {

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("<connection string URI>")

    val database: MongoDatabase = mongoClient.getDatabase("sample_restaurants")
    val collection: MongoCollection[Document] = database.getCollection("restaurants")

    // Retrieves documents with a cuisine value of "Bakery", groups them by "borough", and
    // counts each borough's matching documents
    // start-match-group
    val pipeline = Seq(Aggregates.filter(Filters.equal("cuisine", "Bakery")),
                       Aggregates.group("$borough", Accumulators.sum("count", 1))
    )

    collection.aggregate(pipeline)
              .subscribe((doc: Document) => println(doc.toJson()),
                        (e: Throwable) => println(s"There was an error: $e"))
    // end-match-group

    // Performs the same aggregation operation as above but asks MongoDB to explain it
    // start-explain
    val pipeline = Seq(Aggregates.filter(Filters.equal("cuisine", "Bakery")),
                       Aggregates.group("$borough", Accumulators.sum("count", 1))
    )

    collection.aggregate(pipeline)
              .explain(ExplainVerbosity.EXECUTION_STATS)
              .subscribe((doc: Document) => println(doc.toJson()),
                        (e: Throwable) => println(s"There was an error: $e"))
    // end-explain

    Thread.sleep(1000)
    mongoClient.close()
  }
}
