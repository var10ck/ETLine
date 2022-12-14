package com.etline.metaDataManagement.IncrementsLogic

import com.etline.engine.dataTypes.TableToWrite
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, current_timestamp, max}

import scala.concurrent.{ExecutionContext, Future}

object WaterMarkLoad {
  def makeIncrementalLoad(
      table: TableToWrite
  )(implicit dataBase: HwmDataBaseImpl, ec: ExecutionContext): Future[DataFrame] = {
    val dataFrame      = table.df
    val waterMarkField = table.hwmColumnName
    val tableName      = table.targetName
    dataBase.getWatermark(tableName) flatMap {
      case Some(value) =>
        for {
          maxWaterMark <- Future.successful(
            dataFrame.agg(max(col(waterMarkField))).first().toSeq.head.toString.toInt
          )
          _  <- dataBase.updateWaterMark(tableName, maxWaterMark)
          df <- Future.successful(dataFrame.where(col(waterMarkField) > value.waterMark)
            .withColumn("UPDATE_DTTM", current_timestamp()))
        } yield df
      case None =>
        for {
          maxWaterMark <- Future.successful(
            dataFrame.agg(max(col(waterMarkField))).first().toSeq.head.toString.toInt
          )
          _  <- dataBase.insert(WaterMark(tableName, maxWaterMark))
          df <- Future.successful(dataFrame.withColumn("UPDATE_DTTM", current_timestamp()))
        } yield df
    }
  }
}
