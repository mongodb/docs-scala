import org.bson.json.JsonWriterSettings
import org.mongodb.scala._
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model._

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TimeSeries {

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("<connection string URI>")

    // Create a time series collection
    // start-create-time-series
    val database = mongoClient.getDatabase("fall_weather")

    val tsOptions = TimeSeriesOptions("timestamp").metaField("location")
    val collectionOptions = CreateCollectionOptions().timeSeriesOptions(tsOptions)

    val createObservable = database.createCollection("october2024", collectionOptions)
    Await.result(createObservable.toFuture(), Duration(10, TimeUnit.SECONDS))
    // end-create-time-series

    // Print the details of the collections in the database, including the time series collection
    // start-print-time-series
    val listObservable = database.listCollections()
    val list = Await.result(listObservable.toFuture(), Duration(10, TimeUnit.SECONDS))
    val jsonSettings = JsonWriterSettings.builder().indent(true).build()

    list.foreach(collection => {
      println(collection.toJson(jsonSettings))
    })
    // end-print-time-series

    // Insert data into the time series collection
    // start-insert-time-series-data
    val collection = database.getCollection("october2024")

    val temperatures = Seq(
      Document("timestamp" -> "2024-10-01T00:00:00Z", "temperature" -> 54, "location" -> "New York City"),
      Document("timestamp" -> "2024-10-01T01:00:00Z", "temperature" -> 55, "location" -> "New York City"),
    )
    // end-insert-time-series-data

    Thread.sleep(1000)
    mongoClient.close()
  }
}