package com.etline.engine.IO

import com.etline.config.ConnectionStore
import com.etline.config.Parser.Task
import com.etline.engine.dataTypes.TableToWrite
import com.etline.metaDataManagement.IncrementsLogic.HwmDataBaseImpl
import com.etline.utils.ContextImplicits._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col

import scala.concurrent.Future

object Loader {
  def load(tableToWrite: TableToWrite, taskConfig: Task, layer: String) = {
    val targetConf = taskConfig.target
    val connStore = implicitly[ConnectionStore]
    val connection = connStore.getHdfsConnection(targetConf.connectionId) match {
      case Some(value) => value
      case None        => throw new Exception("connectionId not found")
    }
    val saveTo    = s"${connection.url}/$layer/${taskConfig.target.path}/${tableToWrite.targetName}"

    Future.successful(tableToWrite.df.write
      .mode(taskConfig.saveMode)
      .format(targetConf.format)
      .save(saveTo))
  }
}
