package com.etline.ods

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{current_timestamp, expr, lit, to_timestamp, when}
import com.etline.ods.ExtractId
import org.apache.spark.sql.types.TimestampType

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

object EnrichmentData {

  def enrichData(df: DataFrame): DataFrame = df
    .withColumn("PROCESSED_DTTM", current_timestamp())
    .withColumn("EXTRACT_ID", lit((new ExtractId).getId))
    .withColumn("CLOSED_EXTRACT_ID", lit((new ClosedExtractId).getId))
    .withColumnRenamed("UPDATE_DTTM", "VALID_FROM_DTTM")
    // TODO: tomporary solution
    .withColumn("VALID_TO_DTTM", lit(null: TimestampType))
    .withColumn("DELETED_FLG", lit(new FlagDelete(false).state))


}
