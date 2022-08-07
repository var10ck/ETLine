package com.etline.metaDataManagement.IncrementsLogic

import scala.language.higherKinds

trait HwmDataBase[F[_]] {

  /**
   * Создает таблицу
   *
   * @return unit
   */
  def createTable(): F[Unit]

  /**
   * Добавление инкремента в БД
   *
   * @param increment который необходимо добавить
   * @return единицу, если запись добавлена
   */
  def insert(increment: WaterMark): F[Int]

  /**
   * Поиск в БД по названию инкремента
   *
   * @param tableName название таблицы
   * @return опциональное значение инкремента
   */
  def getWatermark(tableName: String): F[Option[WaterMark]]

  /**
   * Обновление значения инкремента в таблице
   *
   * @param incrementData новое значение
   * @return единицу, если значение обновлено
   */
  def updateWaterMark(incrementData: WaterMark): F[Int]


}
