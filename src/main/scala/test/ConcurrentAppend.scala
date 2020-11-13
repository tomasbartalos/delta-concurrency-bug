package test

import java.util.concurrent.atomic.AtomicLong

import org.apache.hadoop.fs.Path
import org.apache.spark.internal.Logging
import org.apache.spark.sql.delta.DeltaTableUtils
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType
import spark.implicits._

object ConcurrentAppend extends App with Logging {
  configureLog4J()

  val id: AtomicLong = {
    val start = if (DeltaTableUtils.isDeltaTable(spark, new Path(target))) {
      spark.read.format("delta").load(target)
        .select(max('batchId)).head().getLong(0)
    } else {
      -1L
    }
    new AtomicLong(start + 1)
  }

  run()

  def run() = {
    0.to(100000).foreach { _ =>
      append()
    }
  }

  private def append(): Unit = {
    var batchId = 0L
    batchId = appendInternal()
    logInfo(f"Saved batchId: $batchId")
  }

  def appendInternal(): Long = {
    val batchId = id.getAndIncrement()
    spark.range(700)
      .select('id, lit(batchId) as "batchId")
      .withColumn("part", ('id / 100000).cast(IntegerType))
      .write.format("delta")
      .mode("append")
      .partitionBy("part")
      .save(target)
    batchId
  }
}
