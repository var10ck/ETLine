package com.etline.metaDataManagement.IncrementsLogic

import com.etline.engine.dataTypes.TableToWrite
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col

import scala.concurrent.{ExecutionContext, Future}

object IncrementLoad {

  def load(dataFrame: DataFrame, waterMarkField: String)(implicit
      dataBase: DataBaseImpl,
      ec: ExecutionContext
  ): Future[DataFrame] =
    dataBase.getWatermark(waterMarkField) map {
      case Some(value) => dataFrame.where(col(waterMarkField) > value.waterMark)
      case None        => dataFrame
    }

  def load(
      table: TableToWrite
  )(implicit dataBase: DataBaseImpl, ec: ExecutionContext): Future[DataFrame] = {
    dataBase.getWatermark(table.hwmColumnName) map {
      case Some(value) => table.df.where(col(table.hwmColumnName) > value.waterMark)
      case None        => table.df
    }
  }
}
