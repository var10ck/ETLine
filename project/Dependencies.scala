import sbt._

object Dependencies {

  object V {
    // Scala
    val scala = "2.12.16"

    // read config
    val tsConfig = "1.4.2"

    //parse args
    val scopt = "4.0.1"

    //parse JSON
    val circe = "0.14.1"

    // spark
    val spark = "2.4.5"

    // Unit tests
    val scalatest = "3.2.12"
  }

  val commonDependencies = List(
    "com.typesafe" % "config" % V.tsConfig,
    "com.github.scopt" %% "scopt" % V.scopt
  )

  val JsonParsingDependencies = List(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % V.circe)

  val sparkDependencies = List(
    "org.apache.spark" %% "spark-core" % V.spark % "provided",
    "org.apache.spark" %% "spark-sql" % V.spark % "provided",
    "org.apache.spark" %% "spark-hive" % V.spark % "provided"
  )

  val testDependencies = List(
    "org.scalactic" %% "scalactic" % V.scalatest,
    "org.scalatest" %% "scalatest" % V.scalatest % "test"
  )

  val slickDependencies = List(
    "com.typesafe.slick" %% "slick" % "3.3.3",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
    "org.postgresql" % "postgresql" % "42.3.5",
    "com.h2database" % "h2" % "1.4.200",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
  )

  val all: List[ModuleID] = sparkDependencies ++
    testDependencies ++
    JsonParsingDependencies ++
    commonDependencies ++
    slickDependencies

}
