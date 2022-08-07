package com.etline.utils

import com.etline.config.{ConnectionStore, Parser}
import com.etline.metaDataManagement.IncrementsLogic.HwmDataBaseImpl
import com.etline.metaDataManagement.LogsLogic.{CustomListener, DataBaseImpl}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object ContextImplicits {

  implicit lazy val config: Parser.Config = Parser.fromSource("testdata/testconf.json") match {
    case Right(value) => value
    case Left(value) => throw value
  }

  implicit lazy val sparkSession: SparkSession = SparkSession.builder()
    .config(new SparkConf().setAll(config.sparkSessionConf))
    .getOrCreate()


  implicit lazy val connectionStore: ConnectionStore = ConnectionStore("testdata/connectionstestconf.json")

  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newCachedThreadPool)

//  implicit val waterMarkDateBase: DataBaseImpl = DataBaseImpl("postgres")
  implicit val waterMarkDateBase: HwmDataBaseImpl = HwmDataBaseImpl("h2")
  implicit val logsDb: DataBaseImpl = DataBaseImpl("h2")

  val customListener = new CustomListener(logsDb)
  sparkSession.sparkContext.addSparkListener(customListener)

}
