package com.etline.config

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import com.etline.config.SourceDecoder._
import java.net.URI
import scala.io.BufferedSource

object Parser {
  case class Config(tasks: List[Task], sparkSessionConf: Map[String, String])

  case class Task(
      saveMode: String,
      source: Source,
      target: DataTarget
  )

  case class DataTarget(connectionId: String,
                        format: String,
                        path: String,
                        writeOptions: Map[String, String]
  )

  def decodeJson(jsonString: String): Either[Error, Config] = decode[Config](jsonString)

  def fromSource(path: String): Either[Error, Config] = {
    val source: BufferedSource        = scala.io.Source.fromFile(path)
    val config: Either[Error, Config] = decodeJson(source.mkString)
    source.close()
    config
  }

  def fromSource(uri: URI): Either[Error, Config] = fromSource(uri.getPath)
}
