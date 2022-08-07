package metaDataManagement.incrementsLogic

import com.etline.engine.dataTypes.TableToWrite
import com.etline.metaDataManagement.IncrementsLogic.{HwmDataBaseImpl, WaterMarkLoad}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class WaterMarkLoadTest extends AsyncFlatSpec with Matchers {

  implicit val dataBase: HwmDataBaseImpl = HwmDataBaseImpl("h2")
  val sparkSession: SparkSession = SparkSession.builder().master("local[*]").getOrCreate()

  val df: DataFrame = sparkSession.read.options(Map("inferSchema" -> "true", "delimiter" -> ";", "header" -> "true"))
    .csv("testdata/data4test.csv").cache()

  df.show()
  df.printSchema()

  behavior of "increment load"

  it should "load correct with inserting watermark" in {


    val dfSize = df.count()

    for {
      _ <- dataBase.dropTable
      _ <- dataBase.createTable()
      df <- WaterMarkLoad.load(TableToWrite(df, "version", "table1"))
      newWaterMark <- dataBase.getWatermark("table1")
    } yield assert(df.count() == dfSize && newWaterMark.get.waterMark == 3)

  }


  // TODO: FIX
  it should "load correct with updating watermark" in {


    val startedDf = df.where(col("version") === 1)
    val newDf = df.where(col("version") > 1)

    for {
      _ <- dataBase.dropTable
      _ <- dataBase.createTable()
      tableName = "table1"
      dataFrame <- WaterMarkLoad.load(TableToWrite(startedDf, "version", "table1"))
      _ <- Future.successful(dataFrame.show())
      oldWatermark <- dataBase.getWatermark(tableName)
      _ <- Future.successful(println("old watermark" + oldWatermark))
      nDf <- WaterMarkLoad.load(TableToWrite(df, "version", tableName))
      _ <- Future.successful(nDf.show())
      newWaterMark <- dataBase.getWatermark("table1")
      _ <- Future.successful(println("new watermark" + newWaterMark))
    } yield assert(nDf.count() == newDf.count() & newWaterMark.get.waterMark == 3)

  }

}
