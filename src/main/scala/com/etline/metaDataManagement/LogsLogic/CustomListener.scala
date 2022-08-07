package com.etline.metaDataManagement.LogsLogic

import org.apache.spark.scheduler.{
  JobSucceeded,
  SparkListener,
  SparkListenerJobEnd,
  SparkListenerJobStart
}

class CustomListener(dataBaseImpl: DataBaseImpl) extends SparkListener {

  override def onJobStart(jobStart: SparkListenerJobStart): Unit = {
    val logInfo = jobStart.stageInfos match {
      case s if s.nonEmpty => Some(jobStart.stageInfos.mkString("StageInfo: [", "\n", "]\n"))
      case _               => None
    }
    dataBaseImpl.insert(JobLog(jobStart.jobId.toString, "Started", logInfo))
  }

  override def onJobEnd(jobEnd: SparkListenerJobEnd): Unit = {
    val jobStatus = jobEnd.jobResult match {
      case JobSucceeded => "Succeed"
      case _            => "Failed"
    }
    dataBaseImpl.insert(
      JobLog(jobEnd.jobId.toString, jobStatus, Some(s"time taken: ${jobEnd.time}"))
    )
  }

}
