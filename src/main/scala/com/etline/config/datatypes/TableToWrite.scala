package com.etline.config.datatypes

import org.apache.spark.sql.DataFrame

case class TableToWrite (df: DataFrame, hwmColumnName: String, targetName: String)
