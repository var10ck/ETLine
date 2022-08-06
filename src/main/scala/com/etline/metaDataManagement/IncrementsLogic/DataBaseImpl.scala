package com.etline.metaDataManagement.IncrementsLogic

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

case class DataBaseImpl(config: String)(implicit val executionContext: ExecutionContext) extends DataBase[Future] {

  val db = Database.forConfig(config)

  val increments: TableQuery[WaterMarksTable] = TableQuery[WaterMarksTable]

  def dropTable: Future[Unit] = db.run(increments.schema.dropIfExists)

  /**
   * Создает таблицу
   *
   * @return unit
   */
  override def createTable(): Future[Unit] = db.run(increments.schema.createIfNotExists)

  /**
   * Добавление инкремента в БД
   *
   * @param increment который необходимо добавить
   * @return единицу, если запись добавлена
   */
  override def insert(increment: WaterMark): Future[Int] = getIncrement(increment.tableName) flatMap {
    case Some(_) => Future.successful(0)
    case None => db.run(increments += increment)
  }

  /**
   * Поиск в БД по названию инкремента
   *
   * @param tableName название таблицы
   * @return опциональное значение инкремента
   */
  override def getIncrement(tableName: String): Future[Option[WaterMark]] = {
    val query = for {
      increment <- increments if increment.incrementForTable like tableName
    } yield increment
    db.run(query.result.headOption)
  }

  /**
   * Обновление значения инкремента в таблице
   *
   * @param incrementData новое значение
   * @return единицу, если значение обновлено
   */
  override def updateIncrement(incrementData: WaterMark): Future[Int] =
    getIncrement(incrementData.tableName) flatMap {
      case Some(value) => val oldVal = value.waterMark
        val q = increments.filter(_.incrementForTable === incrementData.tableName).map(_.waterMark).update(oldVal + 1)
        db.run(q)
      case None => Future.successful(0)
    }

}
