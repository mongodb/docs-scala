import org.mongodb.scala._
import org.mongodb.scala.model.SearchIndexModel

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object AtlasSearchIndexes {

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("<connection string URI>")

    val database = mongoClient.getDatabase("sample_mflix")
    val collection = database.getCollection("movies")

    {
      // start-create-search-index
      val index = Document("mappings" -> Document("dynamic" -> true))
      val observable = collection.createSearchIndex("<index name>", index)
      Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))
      // end-create-search-index
    }

    {
      // start-create-search-indexes
      val indexOne = SearchIndexModel("<first index name>", Document("mappings" -> Document("dynamic" -> true, "fields" -> Document("field1" -> Document("type" -> "string")))))
      val indexTwo = SearchIndexModel("<second index name>", Document("mappings" -> Document("dynamic" -> false, "fields" -> Document("field2" -> Document("type" -> "string")))))
      val observable = collection.createSearchIndexes(List(indexOne, indexTwo))
      Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))
      // end-create-search-indexes
    }

    {
      // start-list-search-indexes
      val observable = collection.listSearchIndexes()
      observable.subscribe(new Observer[Document] {
        override def onNext(index: Document): Unit = println(index.toJson())
        override def onError(e: Throwable): Unit = println("Error: " + e.getMessage)
        override def onComplete(): Unit = println("Completed")
      })
      // end-list-search-indexes
    }

    {
      // start-update-search-indexes
      val updateIndex = Document("mappings" -> Document("dynamic" -> false))
      val observable = collection.updateSearchIndex("<index to update>", updateIndex)
      Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))
      // end-update-search-indexes
    }

    // start-drop-search-index
    val observable = collection.dropSearchIndex("<index name>")
    Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))
    // end-drop-search-index

    Thread.sleep(1000)
    mongoClient.close()
  }
}
