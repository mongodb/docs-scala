import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.CountOptions
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object Sdam {
  // start-listener-class
  case class TestClusterListener(readPreference: ReadPreference) extends ClusterListener {
    var isWritable: Boolean = false
    var isReadable: Boolean = false

    override def clusterOpening(event: ClusterOpeningEvent): Unit =
        println(s"Cluster with unique client identifier ${event.getClusterId} opening")

    override def clusterClosed(event: ClusterClosedEvent): Unit =
        println(s"Cluster with unique client identifier ${event.getClusterId} closed")

    override def clusterDescriptionChanged(event: ClusterDescriptionChangedEvent): Unit = {
        if (!isWritable) {
        if (event.getNewDescription.hasWritableServer) {
            isWritable = true
            println("Writable server available")
        }
        } else {
        if (!event.getNewDescription.hasWritableServer) {
            isWritable = false
            println("No writable server available")
        }
        }

        if (!isReadable) {
        if (event.getNewDescription.hasReadableServer(readPreference)) {
            isReadable = true
            println("Readable server available")
        }
        } else {
        if (!event.getNewDescription.hasReadableServer(readPreference)) {
            isReadable = false
            println("No readable server available")
        }
        }
    }
  }
  // end-listener-class

  def main(args: Array[String]): Unit = {
    // start-configure-client
    val settings: MongoClientSettings = MongoClientSettings
        .builder()
        .applyToClusterSettings((builder: ClusterSettings.Builder) =>
                builder.addClusterListener(TestClusterListener(ReadPreference.secondary())))
        .applyConnectionString("<connection string>")
        .build()
    val client: MongoClient = MongoClient(settings)
    // end-configure-client
  }
}
