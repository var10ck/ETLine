package com.etline.engine.IO

import com.etline.config.ConnectionStore
import com.etline.config.Parser.Task
import com.etline.engine.dataTypes.TableToWrite
import com.etline.metaDataManagement.IncrementsLogic.HwmDataBaseImpl
import com.etline.utils.ContextImplicits._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col

object Loader {
  def load(tableToWrite: TableToWrite, taskConfig: Task, layer: String) = {
    val targetConf = taskConfig.target
    val connStore = implicitly[ConnectionStore]
    val connection = connStore.getHdfsConnection(targetConf.connectionId) match {
      case Some(value) => value
      case None        => throw new Exception("connectionId not found")
    }
    val saveTo    = s"${connection.url}/$layer/${taskConfig.target.path}/${tableToWrite.targetName}"
    val hwmDb = implicitly[HwmDataBaseImpl]
    val watermark = hwmDb.getWatermark(tableToWrite.targetName)
    watermark
      .map {
        case Some(value) =>
          tableToWrite.df
            .filter(col(tableToWrite.hwmColumnName) > value.waterMark)
        case None => tableToWrite.df
      }
      .map {
        _.write
          .mode(taskConfig.saveMode)
          .format(targetConf.format)
          .save(saveTo)
      }
  }

  def write(tableToWrite: TableToWrite) ={

  }
}
