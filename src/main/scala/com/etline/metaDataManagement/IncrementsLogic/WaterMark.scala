package com.etline.metaDataManagement.IncrementsLogic

import slick.jdbc.PostgresProfile.api._

case class WaterMark(tableName: String, waterMark: Int, id: Option[Int] = Some(0))

class WaterMarksTable(tag: Tag) extends Table[WaterMark](tag, "IncrementTable") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def incrementForTable = column[String]("tableName")

  def waterMark = column[Int]("watermark")

  override def * = (incrementForTable, waterMark, id.?) <> (WaterMark.tupled, WaterMark.unapply)
}

