package com.etline.metaDataManagement.LogsLogic

import java.sql.Timestamp
import java.time.LocalDateTime
import slick.jdbc.PostgresProfile.api._

case class JobLog(jobId: String,
                  jobStatus: String,
                  logInfo: Option[String] = None,
                  addDate: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
                  id: Option[Int] = Some(0))

class JobsTable(tag: Tag) extends Table[JobLog](tag, "JobsLogs") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def jobId = column[String]("jobId")

  def jobStatus = column[String]("logs")

  def logInfo = column[Option[String]]("logInfo")

  def date = column[Timestamp]("addDate")

  override def * = (jobId, jobStatus, logInfo, date, id.?) <> (JobLog.tupled, JobLog.unapply)
}


