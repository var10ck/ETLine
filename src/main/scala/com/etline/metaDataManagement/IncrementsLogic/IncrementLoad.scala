package com.etline.metaDataManagement.IncrementsLogic

import com.etline.engine.dataTypes.TableToWrite
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, max}

import scala.concurrent.{ExecutionContext, Future}

object IncrementLoad {

  def load(dataFrame: DataFrame, waterMarkField: String)(implicit
                                                         dataBase: DataBaseImpl,
                                                         ec: ExecutionContext): Future[DataFrame] =
    dataBase.getWatermark("table1") flatMap {
      case Some(value) => for {
        maxWaterMark <- Future.successful(dataFrame.agg(max(col(waterMarkField))).first().toSeq.head.toString.toInt)
        _ <- dataBase.updateWaterMark(WaterMark("table1", maxWaterMark), maxWaterMark)
        df <- Future.successful(dataFrame.where(col(waterMarkField) > value.waterMark))
      } yield df
      case None => for {
        maxWaterMark <- Future.successful(dataFrame.agg(max(col(waterMarkField))).first().toSeq.head.toString.toInt)
        _ <- dataBase.insert(WaterMark("table1", maxWaterMark))
        df <- Future.successful(dataFrame)
      } yield df
    }

  def load(
            table: TableToWrite
          )(implicit dataBase: DataBaseImpl, ec: ExecutionContext): Future[DataFrame] = {
    dataBase.getWatermark(table.hwmColumnName) map {
      case Some(value) => table.df.where(col(table.hwmColumnName) > value.waterMark)
      case None => table.df
    }
  }
}
