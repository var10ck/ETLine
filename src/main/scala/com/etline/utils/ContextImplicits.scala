package com.etline.utils

import com.etline.config.CommandLineParameters.Parameters
import com.etline.config.{CommandLineParameters, ConnectionStore, Parser}
import com.etline.metaDataManagement.IncrementsLogic.HwmDataBaseImpl
import com.etline.metaDataManagement.LogsLogic.{CustomListener, DataBaseImpl}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object ContextImplicits {
  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newCachedThreadPool)

//  implicit val waterMarkDateBase: DataBaseImpl = DataBaseImpl("postgres")
  implicit val waterMarkDateBase: HwmDataBaseImpl = HwmDataBaseImpl("h2")
  implicit val logsDb: DataBaseImpl = DataBaseImpl("h2")

}
