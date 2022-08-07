package ods

import com.etline.ods.EnrichmentData
import org.apache.spark.sql.functions.current_timestamp
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class EnrichmentDataTest extends AsyncFlatSpec with Matchers {

  // TODO: ADD UPDATE_DTTM while write to raw data

  val sparkSession: SparkSession = SparkSession.builder().master("local[*]").getOrCreate()

  val df: DataFrame = sparkSession.read.options(Map("inferSchema" -> "true", "delimiter" -> ";", "header" -> "true"))
    .csv("testdata/data4test.csv").cache()

  df.show()
  df.printSchema()

  behavior of "enrichData"

  it should "enrich data correctly" in {
    val dfToRow = df.withColumn("UPDATE_DTTM", current_timestamp()).cache()
    val enrichedDf: DataFrame = EnrichmentData.enrichData(dfToRow).cache()
    enrichedDf.show()
    enrichedDf.printSchema()
    assert(1 == 1)
  }

}
