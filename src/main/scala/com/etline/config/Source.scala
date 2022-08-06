package com.etline.config

import cats.syntax.functor._
import io.circe.Decoder
import io.circe.generic.auto._

sealed trait Source

final case class DbSource(connectionId: String, tables: List[Table]) extends Source

final case class FileSource(connectionId: String, files: List[File]) extends Source

case class Table(name: String,
                 hwmColumnName: String,
                 targetName: Option[String],
                 readOptions: Map[String, String],
                 batchLoad: Option[BatchLoad]
)

case class BatchLoad(byColumn: String, partitionBy: String, interval: String)

case class File(path: String,
                hwmColumnName: String,
                targetName: Option[String],
                readOptions: Map[String, String],
                batchLoad: Option[BatchLoad]
)

object SourceDecoder {
  implicit val decodeSource: Decoder[Source] = List[Decoder[Source]](
    Decoder[FileSource].widen,
    Decoder[DbSource].widen
  ).reduceLeft(_ or _)
}
