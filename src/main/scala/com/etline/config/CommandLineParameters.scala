package com.etline.config

import scopt.{OParser, OParserBuilder, OptionParser}

object CommandLineParameters{
  case class Parameters(configPath: String = "", connectionsConfigPath: String = "")

  val builder: OParserBuilder[Parameters] = OParser.builder[Parameters]

  val parser = new OptionParser[Parameters]("ETLine") {
    head("ETLine", "0.1.0")

    opt[String]('c', "config")
      .required()
      .valueName("path")
      .action((path, c) => c.copy(configPath = path))
      .text("path to config file")

    opt[String]('s', "connections")
      .required()
      .valueName("path_to_connections")
      .action((path, c) => c.copy(connectionsConfigPath = path))
      .text("path to file with connections")
  }

  def parse(args:Array[String]): Parameters = parser.parse(args, Parameters()) match {
    case Some(value) => value
    case None => Parameters("","")
  }
}
