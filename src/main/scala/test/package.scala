import org.apache.log4j.PropertyConfigurator
import org.apache.spark.sql.SparkSession

package object test {
  val spark = SparkSession.builder()
    .config("spark.delta.logStore.class", "org.apache.spark.sql.delta.storage.HDFSLogStore")
    .getOrCreate()

  var target = "/tmp/concurrent_delta"

  def configureLog4J(): Unit = {
    PropertyConfigurator.configure(getClass.getResourceAsStream("/log4j.properties"))
  }
}
