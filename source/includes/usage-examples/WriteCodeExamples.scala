
import org.mongodb.scala._
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result._
import org.mongodb.scala.model.Updates._

object WriteCodeExamples {

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClient("mongodb+srv://morisi:Wukong@testcluster.kmosy7d.mongodb.net/?retryWrites=true&w=majority&appName=TestCluster")
    val database: MongoDatabase = mongoClient.getDatabase("sample_scala")
    val collection: MongoCollection[Document] = database.getCollection("insert")

    // start-insert-one
    val doc: Document = Document("<field name>" -> "<value>")
    val observable: Observable[InsertOneResult] = collection.insertOne(doc)

    observable.subscribe(new Observer[InsertOneResult] {
      override def onNext(result: InsertOneResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
    // end-insert-one

    // start-insert-many
    val docs: Seq[Document] = Seq(
      Document("<field name>" -> "<value>"),
      Document("<field name>" -> "<value>")
    )
    val observable: Observable[InsertManyResult] = collection.insertMany(docs)

    observable.subscribe(new Observer[InsertManyResult] {
      override def onNext(result: InsertManyResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
    // end-insert-many

    // start-update-one
    val filter = equal("<field to match>", "<value to match>")
    val update = set("<field name>", "<value>")
    val observable: Observable[UpdateResult] = collection.updateOne(filter, update)

    observable.subscribe(new Observer[UpdateResult] {
      override def onNext(result: UpdateResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
    // end-update-one

    // start-update-many
    val filter = equal("<field to match>", "value to match")
    val update = set("<field name>", "<value>")
    val observable: Observable[UpdateResult] = collection.updateMany(filter, update)

    observable.subscribe(new Observer[UpdateResult] {
      override def onNext(result: UpdateResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
    // end-update-many

    // start-replace-one
    val replacementDoc: Document = Document("<field name>" -> "<value>")
    val filter = equal("<field to match>", "<value to match>")
    val observable: Observable[UpdateResult] = collection.replaceOne(filter, replacementDoc)

    observable.subscribe(new Observer[UpdateResult] {
      override def onNext(result: UpdateResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
    // end-replace-one

    // start-delete-one
    val filter = equal("<field to match>", "<value to match>")
    val observable: Observable[DeleteResult] = collection.deleteOne(filter)

    observable.subscribe(new Observer[DeleteResult] {
      override def onNext(result: DeleteResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
    // end-delete-one

    // start-delete-many
    val filter = equal("<field to match>", "<value to match>")
    val observable: Observable[DeleteResult] = collection.deleteMany(filter)

    observable.subscribe(new Observer[DeleteResult] {
      override def onNext(result: DeleteResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
    // end-delete-many

    // start-bulk-write
    val operations = Seq(
      InsertOneModel(Document("<field name>" -> "<value>")),
      UpdateManyModel(equal("<field to match>", "<value to match>"), set("<field name>", "<value>")),
      DeleteOneModel(equal("<field to match>", "<value to match>"))
    )
    val observable: Observable[BulkWriteResult] = collection.bulkWrite(operations)

    observable.subscribe(new Observer[BulkWriteResult] {
      override def onNext(result: BulkWriteResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = println("Completed")
    })
    // end-bulk-write

    mongoClient.close()
  }
}
