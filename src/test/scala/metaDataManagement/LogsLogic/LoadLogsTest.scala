package metaDataManagement.LogsLogic

import com.etline.metaDataManagement.LogsLogic.{DataBaseImpl, LoadLogs}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class LoadLogsTest extends AsyncFlatSpec with Matchers {

  implicit val dataBase: DataBaseImpl = DataBaseImpl("h2")
  implicit val sparkSession: SparkSession =
    SparkSession.builder().master("local[*]").getOrCreate()

  val df: DataFrame = sparkSession.read
    .options(Map("inferSchema" -> "true", "delimiter" -> ";", "header" -> "true"))
    .csv("testdata/data4test.csv")
    .cache()

  // TODO: fix load logs
  behavior of "load logs"

  it should "load logs" in {
    assert(true)
  }

}
