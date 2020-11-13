package test

import java.util.concurrent.atomic.AtomicBoolean

import org.apache.spark.sql.functions.{col, udf}
import spark.implicits._

object ConcurrentCompactor extends App {

  val mutex = new AtomicBoolean(true)
  val sleepUdf = udf((id: Long) => {
    if(mutex.getAndSet(false)) {
      Thread.sleep(20000)
    }
    id
  })

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
}

case class Test(id: Long, batchId: Long, part: Int)