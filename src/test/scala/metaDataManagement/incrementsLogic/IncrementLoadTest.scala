package metaDataManagement.incrementsLogic

import com.etline.metaDataManagement.IncrementsLogic.{DataBaseImpl, IncrementLoad}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class IncrementLoadTest extends AsyncFlatSpec with Matchers {

  implicit val dataBase: DataBaseImpl = DataBaseImpl("h2")
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
      df <- IncrementLoad.load(df, "version")
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
      dataFrame <- IncrementLoad.load(startedDf, "version")
      _ <- Future.successful(dataFrame.show())
      oldWatermark <- dataBase.getWatermark("table1")
      _ <- Future.successful(println("old watermark" + oldWatermark))
      nDf <- IncrementLoad.load(df, "version")
      _ <- Future.successful(nDf.show())
      newWaterMark <- dataBase.getWatermark("table1")
      _ <- Future.successful(println("new watermark" + newWaterMark))
    } yield assert(nDf.count() == newDf.count() && newWaterMark.get.waterMark == 3)

  }

}
