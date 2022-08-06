package com.etline.utils

import com.etline.config.ConnectionStore
import org.apache.spark.sql.SparkSession

object ExecutionContexts {

  implicit val sparkSession: SparkSession = SparkSession.builder()
    .master("spark://master:7077").getOrCreate()

  implicit val connectionStore: ConnectionStore = ConnectionStore("testdata/connectionstestconf.json")

}
