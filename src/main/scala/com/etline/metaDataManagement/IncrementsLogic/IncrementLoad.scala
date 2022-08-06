package com.etline.metaDataManagement.IncrementsLogic

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col

import scala.concurrent.{ExecutionContext, Future}

object IncrementLoad {

  def load(dataFrame: DataFrame, waterMarkField: String)
          (implicit dataBase: DataBaseImpl, ec: ExecutionContext): Future[DataFrame] =
    dataBase.getIncrement(waterMarkField) map {
      case Some(value) => dataFrame.where(col(waterMarkField) > value.waterMark)
      case None => dataFrame
    }
}
