package com.chenxii.sparketl.service

import com.chenxii.sparketl.utils.ParamUtil

object ShieldTransactionInfoService {

  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      println("用法: 请输入开始日期和结束日期(yyyyMMdd)")
      System.exit(0)
    }

    val paramMap = new ParamUtil().getParamMap(args)

    new ShieldTransactionInfoJob().run(paramMap)
  }

}
