package com.etline.metaDataManagement.LogsLogic

import org.apache.spark.SparkJobInfo
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object LoadLogs {

  def loadLogs[A](f: DataFrame => A)
                 (implicit sparkSession: SparkSession, logsDb: DataBaseImpl, ec: ExecutionContext): Future[List[Int]] =
    Future.sequence {
      Try(f(_)) match {
        case Failure(exception) => getSparkJobs(sparkSession) map {
          case Some(sparkJobInfo) =>
            logsDb.insert(JobLog(sparkJobInfo.jobId.toString, sparkJobInfo.status().toString, Some(exception.getMessage)))
          case None => Future.successful(0)
        }
        case Success(_) => getSparkJobs(sparkSession) map {
          case Some(sparkJobInfo) =>
            logsDb.insert(JobLog(sparkJobInfo.jobId.toString, sparkJobInfo.status().toString))
          case None => Future.successful(0)
        }
      }
    }

  private def getSparkJobs(sparkSession: SparkSession): List[Option[SparkJobInfo]] = {
    val sparkStatusTracker = sparkSession.sparkContext.statusTracker
    sparkStatusTracker.getActiveJobIds().toList.map(sparkStatusTracker.getJobInfo)
  }
}

