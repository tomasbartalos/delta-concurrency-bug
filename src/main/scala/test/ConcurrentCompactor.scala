package test

import java.util.concurrent.atomic.AtomicBoolean

import org.apache.spark.sql.functions.{col, udf}
import spark.implicits._
import Mutex._

object Mutex {
  val mutex = new AtomicBoolean(true)
}

object ConcurrentCompactor extends BaseAppRunner {

  //sleep for 1 minute to let append start
  logInfo("Sleeping 1 minute")
  Thread.sleep(60000)

  //sleeper udf to slow down compactor execution
  val sleepUdf = udf((id: Long) => {
    if(mutex.getAndSet(false)) {
      Thread.sleep(20000)
    }
    id
  })
  logInfo("Starting compaction")
  val df = spark.read.format("delta").load(target)
    df
    .where("part = 0")
    .repartition('part)
    // sleep processing for a while, gives chance for concurrent appends to commit
    .select((sleepUdf(col(df.columns.head)) as df.columns.head) +: df.columns.tail.map(col):_*)
    .write.partitionBy("part").format("delta")
    .mode("overwrite")
    .option("replaceWhere", "part = 0")
    .option("dataChange", "false")
    .save(target)

  logInfo("Compaction finished")

  Thread.sleep(20)

  checkResults()
  spark.stop()

  def checkResults() = {
    val hasDuplicates = spark.read.format("delta").load(target)
      .groupBy('batchId).count.where('count > 700).collect.nonEmpty

    if(hasDuplicates) {
      logError("Test Result: FAILURE, Delta has duplicates:")
      spark.read.format("delta").load(target)
        .groupBy('batchId).count.where('count > 700)
        .withColumn("duplicate_count", 'count / 700).drop('count).show
    } else {
      logInfo("Test Result: SUCCESS, Delta has No Duplicates !")
    }
  }
}