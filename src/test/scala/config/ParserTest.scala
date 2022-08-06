package config

import com.etline.config.Parser.{DataTarget, Task}
import com.etline.config.Source
import io.circe.parser.{decode, parse}
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import io.circe.generic.auto._

class ParserTest extends AnyFlatSpecLike with Matchers{
  behavior of "DataTarget"

  it should "return Right when parsing json string" in {
    val jsonString =
      """
        |{
        |        "connectionId": "localData",
        |        "format": "csv",
        |        "path": "target",
        |        "writeOptions": {
        |          "delimiter": "|",
        |          "encoding": "UTF-8"
        |        }
        |      }
       |""".stripMargin

    val parsedJson = parse(jsonString)
    parsedJson.foreach(println)
    val source = decode[DataTarget](jsonString)
    println(source)

    assert(1 == 1)
  }

  behavior of "Task"
  it should "return Right when parsing json string" in {
    val jsonString =
      """
        |{
        |      "saveMode": "Overwrite",
        |      "source": {
        |        "connectionId": "localData",
        |        "files": [
        |          {
        |            "path": "data4test.csv",
        |            "hwmColumnName": "string",
        |            "targetName": "testDataToLoad.csv",
        |            "readOptions": {
        |              "delimiter":  ";"
        |            }
        |          }
        |        ]
        |      },
        |      "target": {
        |        "connectionId": "localData",
        |        "format": "csv",
        |        "path": "target",
        |        "writeOptions": {
        |          "delimiter": "|",
        |          "encoding": "UTF-8"
        |        }
        |      }
        |    }
        |""".stripMargin
    val parsedJson = parse(jsonString)
    parsedJson.foreach(println)
    val source = decode[Task](jsonString)
    println(source)

    assert(1 == 1)
  }
}
