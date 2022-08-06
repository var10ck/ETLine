package com.etline.engine.IO

import com.etline.config.{DbSource, FileSource, Parser}
import com.etline.config.Parser.Task
import org.apache.spark.sql.{DataFrame, SparkSession}

object Reader {
  def readDf(task: Task)(implicit sparkSession: SparkSession): (DataFrame, String) = task.source match {
    case DbSource(connectionId, readOptions, waterMarkField) => (sparkSession.read.format("jdbc")
      .option("url", s"$connectionId")
      .options(readOptions)
      .load(), waterMarkField)
    case FileSource(path, readOptions, waterMarkField) =>
      (sparkSession.read.options(readOptions).csv(path), waterMarkField)
  }
}
