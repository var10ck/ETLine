package com.etline.engine.IO

import com.etline.config.Parser
import com.etline.config.Parser.Task
import org.apache.spark.sql.{DataFrame, SparkSession}

object Reader {

  def readDf(task: Task)(implicit sparkSession: SparkSession): DataFrame = task.source match {
    case Parser.DbSource(connectionId, tables) => ???
    case Parser.FileSource(path, readOptions) => ???
  }


}
