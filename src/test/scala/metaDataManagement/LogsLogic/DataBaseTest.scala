package metaDataManagement.LogsLogic

import com.etline.metaDataManagement.LogsLogic.{DataBaseImpl, JobLog, JobsTable}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.lifted.TableQuery

class DataBaseTest extends AsyncFlatSpec with Matchers {

  val dataBase: DataBaseImpl = DataBaseImpl("h2")

  val increments: TableQuery[JobsTable] = TableQuery[JobsTable]

  behavior of "insert log"

  it should "insert log" in {

    for {
      _ <- dataBase.dropTable
      _ <- dataBase.createTable()
      value <- dataBase.insert(JobLog("first", "RUN"))
    } yield assert(value == 1)
  }

}
