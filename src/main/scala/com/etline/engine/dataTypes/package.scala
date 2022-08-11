package com.etline.engine

import org.apache.spark.sql.DataFrame

package object dataTypes {
  case class TableToWrite (df: DataFrame, hwmColumnName: String, targetName: String)
}
