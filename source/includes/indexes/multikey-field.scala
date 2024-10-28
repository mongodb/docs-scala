package quickstart

import com.mongodb.{ServerApi, ServerApiVersion}
import org.mongodb.scala.{ConnectionString, MongoClient, MongoClientSettings}
import org.mongodb.scala.bson.Document
import Helpers._

// start-multikey-index-imports
import org.mongodb.scala._
import org.mongodb.scala.model.Indexes
import org.mongodb.scala.model.IndexOptions._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Filters._
// end-multikey-index-imports

object MulitkeyFieldIndex {

    def main(args: Array[String]): Unit = {

    // Replace the placeholder with your Atlas connection string
    val connectionString = "mongodb+srv://user:123@atlascluster.spm1ztf.mongodb.net/?authSource=admin";

    // Create a new client and connect to the server
    val mongoClient = MongoClient(connectionString)

    // start-db-coll
    val database: MongoDatabase = mongoClient.getDatabase("sample_mflix")
    val collection: MongoCollection[Document] = database.getCollection("movies")
    // end-db-coll

    // start-index-multikey
    collection.createIndex(Indexes.ascending("cast")).printResults()
    // end-index-multikey

    // start-index-multikey-query
    val filter1 = and(equal("cast", "Aamir Khan"), equal("cast", "Kajol"))

    collection.find(filter).first().subscribe((doc: Document) => println(doc.toJson()),
                            (e: Throwable) => println(s"There was an error: $e"))
    // end-index-multikey-query

    // start-check-multikey-index
    collection.listIndexes().printResults()
    // end-check-multikey-index

  }
}