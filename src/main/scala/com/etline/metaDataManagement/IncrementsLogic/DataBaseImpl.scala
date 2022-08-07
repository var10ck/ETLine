package com.etline.metaDataManagement.IncrementsLogic

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

case class DataBaseImpl(config: String)(implicit val executionContext: ExecutionContext) extends DataBase[Future] {

  val db = Database.forConfig(config)

  val waterMarks: TableQuery[WaterMarksTable] = TableQuery[WaterMarksTable]

  def dropTable: Future[Unit] = db.run(waterMarks.schema.dropIfExists)

  /**
   * Создает таблицу
   *
   * @return unit
   */
  override def createTable(): Future[Unit] = db.run(waterMarks.schema.createIfNotExists)

  /**
   * Добавление High Water Mark в БД
   *
   * @param waterMark наибольшее значение watermark-поля которое необходимо добавить
   * @return единицу, если запись добавлена
   */
  override def insert(waterMark: WaterMark): Future[Int] = getWatermark(waterMark.tableName) flatMap {
    case Some(_) => Future.successful(0)
    case None => db.run(waterMarks += waterMark)
  }

  /**
   * Поиск в БД по названию инкремента
   *
   * @param tableName название таблицы
   * @return опциональное значение high water mark
   */
  override def getWatermark(tableName: String): Future[Option[WaterMark]] = {
    val query = for {
      waterMark <- waterMarks if waterMark.forTable like tableName
    } yield waterMark
    db.run(query.result.headOption)
  }

  /**
   * Обновление значения high water mark в таблице
   *
   * @param waterMark новое значение
   * @return единицу, если значение обновлено
   */
  override def updateWaterMark(waterMark: WaterMark, newWaterMark: Int): Future[Int] =
    getWatermark(waterMark.tableName) flatMap {
      case Some(value) => val oldVal = value.waterMark
        val q = waterMarks.filter(_.forTable === waterMark.tableName).map(_.waterMark).update(newWaterMark)
        db.run(q)
      case None => Future.successful(0)
    }

}
