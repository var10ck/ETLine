package com.etline.config

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._

import java.net.URI
import scala.io.BufferedSource

object Parser extends App {
  case class Config(tasks: List[Task])

  case class Task(
      saveMode: String,
      batchLoad: Option[BatchLoad],
      source: DataSource,
      target: DataTarget,
      sparkSessionConf: Map[String, String]
  )

  case class BatchLoad(byColumn: String, partitionBy: String, interval: String)

  case class DataSource(connectionId: String, tables: List[Table])

  case class Table(name: String, columns: List[String], hwmColumnName: String)

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

  val parsed = fromSource("testdata/testconf.json")
  parsed.map(c => println(c.tasks))
}
