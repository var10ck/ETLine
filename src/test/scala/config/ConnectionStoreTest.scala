package config

import com.etline.config.ConnectionStore
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ConnectionStoreTest extends AnyFlatSpec with Matchers {

  behavior of "decode json string"

  it should "return Config" in {
    val jsonString =
      """
        {
  "dbConnections": [
    {
      "connectionId": "c730433b-082c-4984-9d66-855c24ds4dsd44",
      "driver": "postgresql",
      "host": "localhost:5432",
      "dbName": "string",
      "user": "string",
      "password": "string"
    }
  ],
  "hdfsConnections": [
    {
      "connectionId": "c730433b-082c-4984-9d66-855c24ds4da9kj91",
      "url": "localhost:4444"
    }
  ]
}
        """
    val connectionStore = ConnectionStore("testdata/connectionstestconf.json")
    val connections = connectionStore.decodeJsonString(jsonString)

    connections.foreach(println)

    connections.isRight should be(true)
  }

}
