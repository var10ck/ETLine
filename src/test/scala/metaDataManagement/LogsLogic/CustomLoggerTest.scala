package metaDataManagement.LogsLogic

import com.etline.metaDataManagement.LogsLogic.{CustomListener, JobsTable, DataBaseImpl => LogsDataBaseImpl}

import org.apache.log4j.LogManager
import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import slick.lifted.TableQuery

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

class CustomLoggerTest extends AnyFlatSpecLike with Matchers{
    implicit val ec = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
  val logsDb: LogsDataBaseImpl = LogsDataBaseImpl("h2")

  val increments: TableQuery[JobsTable] = TableQuery[JobsTable]

  implicit val spark: SparkSession = SparkSession.builder().master("local[*]")
    .appName("CustomLoggerTest")
    .getOrCreate()

   val customListener = new CustomListener(logsDb)
    spark.sparkContext.addSparkListener(customListener)

  behavior of "CustomLogger"

  it should "write logs in database" in {
    val df = spark.read.csv("testdata/data4test.csv")
    spark.sparkContext.setLogLevel("ERROR")
    df.count()
    val logs = for{
      _ <- logsDb.dropTable
      _ <- logsDb.createTable()
      logs <- logsDb.getLogs(10)
    } yield logs
    logs.map(r => r.length should be > 0)
  }
}
