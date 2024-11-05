import org.bson.json.JsonWriterSettings
import org.mongodb.scala._
import org.mongodb.scala.bson.{BsonDateTime, Document}
import org.mongodb.scala.model._

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TimeSeries {

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("mongodb+srv://morisi:Wukong@testcluster.kmosy7d.mongodb.net/?retryWrites=true&w=majority&appName=TestCluster")
    val deleteDatabase = mongoClient.getDatabase("fall_weather")
    val deleteObservable = deleteDatabase.drop()
    Await.result(deleteObservable.toFuture(), Duration(10, TimeUnit.SECONDS))

    // start-create-time-series
    val database = mongoClient.getDatabase("fall_weather")

    val tsOptions = TimeSeriesOptions("timestamp").metaField("location")
    val collectionOptions = CreateCollectionOptions().timeSeriesOptions(tsOptions)

    val createObservable = database.createCollection("october2024", collectionOptions)
    Await.result(createObservable.toFuture(), Duration(10, TimeUnit.SECONDS))
    // end-create-time-series

    // start-print-time-series
    val listObservable = database.listCollections()
    val list = Await.result(listObservable.toFuture(), Duration(10, TimeUnit.SECONDS))
    val jsonSettings = JsonWriterSettings.builder().indent(true).build()

    list.foreach(collection => {
      println(collection.toJson(jsonSettings))
    })
    // end-print-time-series

    // start-insert-time-series-data
    val collection = database.getCollection("october2024")

    val temperatures = Seq(
      Document("timestamp" -> BsonDateTime(1727755200000L), "temperature" -> 54, "location" -> "New York City"),
      Document("timestamp" -> BsonDateTime(1727841600000L), "temperature" -> 55, "location" -> "New York City"),
    )

    val insertObservable = collection.insertMany(temperatures)
    Await.result(insertObservable.toFuture(), Duration(10, TimeUnit.SECONDS))
    // end-insert-time-series-data

    mongoClient.close()
  }
}
