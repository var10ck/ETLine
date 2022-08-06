package com.etline.metaDataManagement.LogsLogic

import scala.language.higherKinds

trait DataBase[F[_]] {

  /**
   * Создает таблицу
   *
   * @return unit
   */
  def createTable(): F[Unit]

  /**
   * Добавление инкремента в БД
   *
   * @param log который необходимо добавить
   * @return единицу, если запись добавлена
   */
  def insert(log: JobLog): F[Int]

}
