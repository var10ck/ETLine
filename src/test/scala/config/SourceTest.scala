package config

import com.etline.config.Source
import com.etline.config.SourceDecoder.decodeSource
import io.circe.parser._
import org.scalatest.flatspec.AsyncFlatSpec

class SourceTest extends AsyncFlatSpec {

  behavior of "parse Source"

  it should "return right dbSource" in {
    val json: String =
      """
      {
        "connectionId": "c730433b-082c-4984-9d66-855c243266f0",
        "waterMarkField": "Foo",
        "readOptions": {
          "bar": "Foo",
          "baz": "Foo",
          "qux": "Bar"
        }
      }
    """

    val parsedJson = parse(json)
    parsedJson.foreach(println)
    val source = decode[Source](json)
    println(source)

    assert(1 == 1)
  }

  it should "return right fileSource" in {
    val json: String =
      """
      {
        "path": "c730433b-082c-4984-9d66-855c243266f0",
        "readOptions": {
          "bar": "Foo",
          "baz": "Foo",
          "qux": "Bar"
        }
      }
    """

    val parsedJson = parse(json)
    parsedJson.foreach(println)
    val source = decode[Source](json)
    println(source)

    assert(1 == 1)

  }

}
