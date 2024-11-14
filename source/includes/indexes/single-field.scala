package quickstart

import org.mongodb.scala.{ConnectionString, MongoClient, MongoClientSettings}
import org.mongodb.scala.bson.Document

// start-single-index-imports
import org.mongodb.scala._
import org.mongodb.scala.model.Indexes
import org.mongodb.scala.model.IndexOptions._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Filters._
import Helpers._
// end-single-index-imports


object SingleFieldIndex {

    def main(args: Array[String]): Unit = {

    // Replace the placeholder with your Atlas connection string
    val connectionString = "<connection string>";

    // Create a new client and connect to the server
    val mongoClient = MongoClient(connectionString)
    val database = mongoClient.getDatabase("sample_training")
    val collection = database.getCollection("companies")

    // start-index-single
    collection.createIndex(Indexes.ascending("title")).printResults()
    // end-index-single

    // start-index-single-query
    val filter = equal("title", "Sweethearts")

    collection.find(filter).first().subscribe((doc: Document) => println(doc.toJson()),
                            (e: Throwable) => println(s"There was an error: $e"))
    // end-index-single-query

    // start-check-single-index
    collection.listIndexes().printResults()
    // end-check-single-index

  }
}
