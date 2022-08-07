package com.etline.logic

import com.etline.config.{CommandLineParameters, ConnectionStore, Parser}
import com.etline.engine.IO.{Loader, Reader}
import com.etline.engine.dataTypes.TableToWrite
import com.etline.metaDataManagement.IncrementsLogic.WaterMarkLoad
import com.etline.metaDataManagement.LogsLogic.CustomListener
import com.etline.ods.EnrichmentData
import com.etline.utils.ContextImplicits._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object Run {
  def main(args: Array[String]):Unit = {
    val params = CommandLineParameters.parse(args)

    implicit lazy val connectionStore: ConnectionStore = ConnectionStore(
      params.connectionsConfigPath
    )
    implicit lazy val config: Parser.Config = Parser.fromSource(params.configPath) match {
      case Right(value) => value
      case Left(value)  => throw value
    }
    implicit lazy val sparkSession: SparkSession = SparkSession
      .builder()
      .config(new SparkConf().setAll(config.sparkSessionConf))
      .getOrCreate()

    val customListener = new CustomListener(logsDb)
    sparkSession.sparkContext.addSparkListener(customListener)

    val tasks = config.tasks
    sparkSession.sparkContext.setLogLevel("ERROR")
    val result = for {
      task  <- tasks
      table <- Reader.readAll(task)
    } yield waterMarkDateBase
      .createTable()
      .flatMap(u => logsDb.createTable())
      .flatMap(u =>
        WaterMarkLoad.makeIncrementalLoad(table) zip Future.successful(table, task)
      )
      .flatMap { case (df, (table, task)) =>
        Loader
          .load(TableToWrite(df, table.hwmColumnName, table.targetName), task, "raw")
          .map(savePath => (table, task, savePath))
      }
      .map { case (table, task, savePath) =>
        val rawDf = sparkSession.read
          .format(task.target.format)
          .load(savePath)
        val enrichedDf = EnrichmentData.enrichData(rawDf)
        Loader.load(TableToWrite(enrichedDf, table.hwmColumnName, table.targetName),
                    task,
                    "ODS"
        )
      }

    Await.result(Future.sequence(result), Duration.Inf)
  }
}
