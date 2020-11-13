import org.apache.log4j.PropertyConfigurator
import org.apache.spark.sql.SparkSession

package object test {
  val spark = SparkSession.builder().config("spark.master", "local[*]").getOrCreate()

  val target = "/tmp/concurrent_delta"

  def configureLog4J() = {
    PropertyConfigurator.configure(getClass.getResourceAsStream("/log4j.properties"))
  }
}
