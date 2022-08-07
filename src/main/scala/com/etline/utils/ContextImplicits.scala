package com.etline.utils

import com.etline.config.{ConnectionStore, Parser}
import com.etline.metaDataManagement.IncrementsLogic.HwmDataBaseImpl
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object ContextImplicits {

  implicit val config: Parser.Config = Parser.fromSource("testdata/testconf.json") match {
    case Right(value) => value
    case Left(value) => throw value
  }

  implicit val sparkSession: SparkSession = SparkSession.builder()
    .config(new SparkConf().setAll(config.sparkSessionConf))
//    .master("local[*]")
//    .appName("ddddd")
    .getOrCreate()

  implicit lazy val connectionStore: ConnectionStore = ConnectionStore("testdata/connectionstestconf.json")

  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newCachedThreadPool)

//  implicit val waterMarkDateBase: DataBaseImpl = DataBaseImpl("postgres")
  implicit val waterMarkDateBase: HwmDataBaseImpl = HwmDataBaseImpl("h2")

}
