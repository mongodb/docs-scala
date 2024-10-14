
import org.mongodb.scala._
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result._
import org.mongodb.scala.model.Updates._

object WriteCodeExamples {

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("<connection string URI>")
    val database: MongoDatabase = mongoClient.getDatabase("<database name>")
    val collection: MongoCollection[Document] = database.getCollection("<collection name>")

    // Start example code here

    // End example code here

    mongoClient.close()
  }
}
