package com.chenxii.sparketl.service

import com.chenxii.sparketl.common.InitSpark

import java.io.FileInputStream
import scala.xml.XML

class ShieldTransactionInfoJob {

  def run(paramMap: Map[String, String]): Unit = {

    val xmlFilePath = "sql/se_transaction_info.xml"
    val xml = XML.load(new FileInputStream(xmlFilePath))

    val dropTransactionInfoPartitionSQL = (xml \ "drop_se_transaction_info_partition_step2")
      .map(x => x.text)
      .head
      .replaceAll("\\$\\{startDate}", paramMap.getOrElse("start_date", "20230801"))
      .replaceAll("\\$\\{endDate}", paramMap.getOrElse("end_date", "20230901"))

    val shieldTransactionInfoSQL = (xml \ "shield_se_transaction_info")
      .map(x => x.text)
      .head
      .replaceAll("\\$\\{startDate}", paramMap.getOrElse("start_date", "20230801"))
      .replaceAll("\\$\\{endDate}", paramMap.getOrElse("end_date", "20230901"))

    val sparkSession = InitSpark.init()

    sparkSession.sql("hive.exec.dynamic.partition=true;")
    sparkSession.sql("hive.exec.dynamic.partition.mode=nonstric;")

    // 删除分区
    sparkSession.sql(dropTransactionInfoPartitionSQL)

    // 写入数据
    sparkSession.sql(shieldTransactionInfoSQL)

    sparkSession.stop()

  }
}
