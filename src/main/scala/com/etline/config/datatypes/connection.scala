package com.etline.config.datatypes

object connection {
  case class Connections(dbConnections: List[DbConnection],
                         hdfsConnections: List[HdfsConnection]
                        )

  case class DbConnection(connectionId: String,
                          driver: String,
                          host: String,
                          dbName: String,
                          user: String,
                          password: String
                         )

  case class HdfsConnection(connectionId: String, url: String)
}
