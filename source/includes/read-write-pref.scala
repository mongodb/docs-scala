import org.mongodb.scala._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import com.mongodb.{ TransactionOptions, ReadConcern, ReadPreference, WriteConcern }

object ReadWritePref {

  def main(args: Array[String]): Unit = {

    // start-client-settings
    val mongoClient = MongoClient(MongoClientSettings.builder()
        .applyConnectionString(ConnectionString("mongodb://localhost:27017/"))
        .readPreference(ReadPreference.secondary())
        .readConcern(ReadConcern.LOCAL)
        .writeConcern(WriteConcern.W2)
        .build())
    // end-client-settings

    // start-client-settings-uri
    val mongoClient = MongoClient("mongodb://localhost:27017/?readPreference=secondary&w=2&readConcernLevel=local")
    // end-client-settings-uri

    // start-session-settings
    val clientSession: ClientSession = mongoClient.startSession()
    clientSession
        .setReadPreference(ReadPreference.primaryPreferred())
        .setReadConcern(ReadConcern.LOCAL)
        .setWriteConcern(WriteConcern.MAJORITY)
    // end-session-settings

    // start-transaction-settings
    val tOptions: TransactionOptions = TransactionOptions.builder()
        .readPreference(ReadPreference.primary())
        .readConcern(ReadConcern.MAJORITY)
        .writeConcern(WriteConcern.W1)
        .build()
    clientSession.startTransaction(tOptions)
    // end-transaction-settings

    // start-database-settings
    val database = mongoClient.getDatabase("test_database")
        .withReadPreference(ReadPreference.primaryPreferred())
        .withReadConcern(ReadConcern.AVAILABLE)
        .withWriteConcern(WriteConcern.MAJORITY)
    // end-database-settings

    // start-collection-settings
    val collection = database.getCollection("test_collection")
        .withReadPreference(ReadPreference.secondaryPreferred())
        .withReadConcern(ReadConcern.AVAILABLE)
        .withWriteConcern(WriteConcern.UNACKNOWLEDGED)
    // end-collection-settings

    // Instructs the driver to prefer reads from secondary replica set members
    // located in New York, followed by a secondary in San Francisco, and
    // lastly fall back to any secondary.
    // start-tag-set
    val tagList = List(Tag("dc", "ny"), Tag("dc", "sf"), TagSet())
    val readPreference = ReadPreference.secondary(List(TagSet(tagList)))

    val database = mongoClient.getDatabase("test_database")
        .withReadPreference(readPreference)
    // end-tag-set

    // Instructs the library to distribute reads between members within 35 milliseconds
    // of the closest member's ping time
    // start-local-threshold
    val connectionString = "mongodb://localhost:27017/?replicaSet=repl0&localThresholdMS=35"
    val mongoClient = MongoClient(connectionString)
    // end-local-threshold

    // Keep the main thread alive long enough for the asynchronous operations to complete
    Thread.sleep(5000)

    // Close the MongoClient connection
    mongoClient.close()
    
  }

}