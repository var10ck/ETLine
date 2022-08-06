package com.etline.config

import cats.syntax.functor._
import io.circe.Decoder
import io.circe.generic.auto._


sealed trait Source

final case class DbSource(connectionId: String, readOptions: Map[String, String], waterMarkField: String) extends Source

final case class FileSource(path: String, readOptions: Map[String, String]) extends Source

object SourceDecoder {
  implicit val decodeSource: Decoder[Source] = List[Decoder[Source]](
    Decoder[FileSource].widen,
    Decoder[DbSource].widen
  ).reduceLeft(_ or _)
}

