package com.etline.engine.IO

import com.etline.config.Parser.Task
import com.etline.engine.dataTypes.TableToWrite
import com.etline.utils.ContextImplicits.connectionStore
import org.apache.spark.sql.DataFrame

object Loader {
  def load(tableToWrite: TableToWrite, taskConfig: Task) ={
    val targetConf = taskConfig.target
    val connection = connectionStore.getHdfsConnection(targetConf.connectionId) match {
      case Some(value) => value
      case None => throw new Exception("connectionId not found")
    }
    val saveTo = s"${connection.url}/${taskConfig.target.path}/${tableToWrite.targetName}"
    tableToWrite.df.write
      .mode(taskConfig.saveMode)
      .format(targetConf.format)
      .options(targetConf.writeOptions)
      .save(saveTo)
  }
}
