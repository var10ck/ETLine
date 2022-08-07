package com.etline.logic

import com.etline.engine.IO.{Loader, Reader}
import com.etline.engine.dataTypes.TableToWrite
import com.etline.metaDataManagement.IncrementsLogic.WaterMarkLoad
import com.etline.utils.ContextImplicits._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object Run extends App {
  val tasks = config.tasks
  sparkSession.sparkContext.setLogLevel("ERROR")
  val result = for {
    task  <- tasks
    table <- Reader.readAll(task)
    _ = table.df.show()
  } yield waterMarkDateBase
    .createTable()
    .flatMap(u => logsDb.createTable())
    .flatMap(u => WaterMarkLoad.load(table) zip Future.successful(table, task))
    .map { case (df, (table, task)) =>
      Loader.load(TableToWrite(df, table.hwmColumnName, table.targetName), task, "raw")
    }

  Await.result(Future.sequence(result), Duration.Inf)
}
