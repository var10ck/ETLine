package metaDataManagement.incrementsLogic

import com.etline.metaDataManagement.IncrementsLogic.{HwmDataBaseImpl, WaterMark, WaterMarksTable}
import slick.jdbc.PostgresProfile.api._
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class HwmDataBaseTest extends AsyncFlatSpec with Matchers {

  val dataBase: HwmDataBaseImpl = HwmDataBaseImpl("h2")

  val increments: TableQuery[WaterMarksTable] = TableQuery[WaterMarksTable]

  behavior of "create table"

  it should "create table" in {

    dataBase.dropTable
    dataBase.createTable()
    val query = for {
      increment <- increments
    } yield increment
    dataBase.db.run(query.result).map(list => assert(list.isEmpty))
  }

  behavior of "insert"

  it should "return 1 because complete" in {
    dataBase.dropTable
    dataBase.createTable()
    dataBase.insert(WaterMark("table", 1)).map(value => assert(value == 1))

  }

  it should "return 1 because already exist" in {
    dataBase.insert(WaterMark("table", 2)).map(value => assert(value == 0))
  }

  behavior of "update"

  it should "return 1 because complete" in {
    dataBase.dropTable
    dataBase.createTable()

    val query = for {
      increment <- increments
    } yield increment

    val future = for {
      _ <- dataBase.insert(WaterMark("table", 1))
      result <- dataBase.updateWaterMark(WaterMark("table", 2))
      _ <- dataBase.db.run(query.result)
    } yield result
    future.map(value => assert(value == 1))
  }

  it should "return 0 because doesnt exist" in {
    dataBase.dropTable
    dataBase.createTable()

    dataBase.updateWaterMark(WaterMark("table", 2)).map(value => assert(value == 0))

  }

  behavior of "get"

  it should "return watermark" in {
    dataBase.dropTable
    dataBase.createTable()


    val future = for {
      _ <- dataBase.insert(WaterMark("table", 1))
      waterMark = dataBase.getWatermark("table")
    } yield waterMark

    future.flatten.map(optWatermark => optWatermark.map(waterMark => assert(waterMark == WaterMark("table", 1, Some(1)))).get)

  }

}
