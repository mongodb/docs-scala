// start-compound-index-imports
import org.mongodb.scala._
import org.mongodb.scala.model.Indexes
import org.mongodb.scala.model.IndexOptions._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Filters._
// end-compound-index-imports


object CompoundFieldIndex {

    def main(args: Array[String]): Unit = {

    // Replace the placeholder with your Atlas connection string
    val connectionString = "<connection string>";

    // Create a new client and connect to the server
    val mongoClient = MongoClient(connectionString)

    // start-db-coll
    val database: MongoDatabase = mongoClient.getDatabase("sample_mflix")
    val collection: MongoCollection[Document] = database.getCollection("movies")
    // end-db-coll

    // start-index-compound
    collection.createIndex(
              Indexes.compoundIndex(Indexes.descending("runtime"),
                                    Indexes.ascending("year")))
          .printResults()

    // end-index-compound

    // start-index-compound-query
    val filter = and(gt("runtime", 80), gt("year", 1999))

    collection.find(filter).first().subscribe((doc: Document) => println(doc.toJson()),
                            (e: Throwable) => println(s"There was an error: $e"))
    // end-index-compound-query

    // start-check-compound-index
    collection.listIndexes().printResults()
    // end-check-compound-index

  }
}
