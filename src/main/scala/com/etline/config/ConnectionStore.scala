package com.etline.config

import com.etline.config.datatypes.connection._
import com.etline.utils.Control
import io.circe.Error
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import java.net.URI

class ConnectionStore(pathToConfig: String) {
  // TODO: get path to json from args or app.conf
  val connections: Connections = fromSource(pathToConfig) match {
    case Right(value) => value
    case Left(_) => throw new Exception("Cannot parse connections")
  }

  def decodeJsonString(jsonString: String): Either[Error, Connections] =
    decode[Connections](jsonString)

  def fromSource(path: String, encoding: String = "UTF-8"): Either[Error, Connections] = {
    Control.using(scala.io.Source.fromFile(path, encoding)) { source =>
      decodeJsonString(source.mkString)
    }
  }

  def fromSource(url: URI, encoding: String): Either[Error, Connections] =
    fromSource(url.getPath, encoding)


  def getDbConnection(connectionId: String): Option[DbConnection] =
    connections.dbConnections.find(_.connectionId == connectionId)

  def getHdfsConnection(connectionId: String): Option[HdfsConnection] =
    connections.hdfsConnections.find(_.connectionId == connectionId)
}

object ConnectionStore {
  def apply(pathToConfig: String) = new ConnectionStore(pathToConfig)
}
