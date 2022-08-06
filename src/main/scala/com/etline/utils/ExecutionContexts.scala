package com.etline.utils

import org.apache.spark.sql.SparkSession

object ExecutionContexts {

  implicit val sparkSession: SparkSession = SparkSession.builder().getOrCreate()

}
