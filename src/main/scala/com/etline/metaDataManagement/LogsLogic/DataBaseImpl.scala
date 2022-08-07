package com.etline.metaDataManagement.LogsLogic

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

case class DataBaseImpl(config: String)(implicit val executionContext: ExecutionContext)
    extends DataBase[Future] {

  val db = Database.forConfig(config)

  val logs: TableQuery[JobsTable] = TableQuery[JobsTable]

  def dropTable: Future[Unit] = db.run(logs.schema.dropIfExists)

  /** Создает таблицу
    *
    * @return
    *   unit
    */
  override def createTable(): Future[Unit] = db.run(logs.schema.createIfNotExists)

  /** Добавление инкремента в БД
    *
    * @param log
    *   который необходимо добавить
    * @return
    *   единицу, если запись добавлена
    */
  override def insert(log: JobLog): Future[Int] = db.run(logs += log)

  def getLogs(limit:Long): Future[Seq[JobLog]] = db.run(logs.sortBy(_.date.desc).take(limit).result)
}