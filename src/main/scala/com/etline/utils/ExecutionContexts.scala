package com.etline.utils

import com.etline.metaDataManagement.IncrementsLogic.DataBaseImpl
import org.apache.spark.sql.SparkSession

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object ExecutionContexts {

  implicit val sparkSession: SparkSession = SparkSession.builder()
    .master("spark://master:7077").getOrCreate()

  implicit val connectionStore: ConnectionStore = ConnectionStore("testdata/connectionstestconf.json")

  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newCachedThreadPool)

  implicit val waterMarkDateBase: DataBaseImpl = DataBaseImpl("postgres")

}
