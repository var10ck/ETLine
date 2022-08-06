package com.etline.engine.IO

import com.etline.config.{DbSource, FileSource, Parser}
import com.etline.config.Parser.Task
import org.apache.spark.sql.{DataFrame, SparkSession}

object Reader {

  def readDf(task: Task)(implicit sparkSession: SparkSession): DataFrame = task.source match {
    case DbSource(connectionId, readOptions, waterMarkField) => sparkSession.read.jdbc()
    case FileSource(path, readOptions) => ???
  }


}
