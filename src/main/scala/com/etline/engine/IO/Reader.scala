package com.etline.engine.IO

import com.etline.config.{ConnectionStore, DbSource, FileSource, Parser}
import com.etline.config.Parser.Task
import com.etline.config.datatypes.{connection, TableToWrite}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Reader {
  def readAll(task: Task)(implicit
      sparkSession: SparkSession,
      connectionStore: ConnectionStore
  ) = task.source match {
    case DbSource(connectionId, tables) =>
      val conn: connection.DbConnection = getDbConnection(connectionId)
      val url                           = s"jdbc:${conn.driver}:${conn.host}/${conn.dbName}"
      tables.map { t =>
        TableToWrite(
          sparkSession.read
            .format("jdbc")
            .option("url", url)
            .option("dtable", t.name)
            .option("user", conn.user)
            .option("password", conn.password)
            .load(),
          t.hwmColumnName,
          t.hwmColumnName
        )
      }

    case FileSource(connectionId, files) =>
      val conn = getHdfsConnection(connectionId)
      files.map { f =>
        val url = s"${conn.url}/${f.path}"
        TableToWrite(
          sparkSession.read
            .options(f.readOptions)
            .csv(url),
          f.hwmColumnName,
          f.hwmColumnName
        )
      }
  }

  def getDbConnection(connectionId: String)(implicit connectionStore: ConnectionStore) =
    connectionStore.getDbConnection(connectionId) match {
      case Some(value) => value
      case None =>
        throw new Exception(
          "Connection not found"
        ) // TODO: make rules to handle cases when something isn't found in config
    }

  def getHdfsConnection(connectionId: String)(implicit connectionStore: ConnectionStore) =
    connectionStore.getHdfsConnection(connectionId) match {
      case Some(value) => value
      case None =>
        throw new Exception(
          "Connection not found"
        )
    }
}
