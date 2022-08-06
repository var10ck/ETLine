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
    val spark = "2.4.7"

    // Unit tests
    val scalatest  = "3.2.12"
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
    "org.apache.spark" %% "spark-sql"  % V.spark % "provided",
    "org.apache.spark" %% "spark-hive" % V.spark % "provided"
  )

  val testDependencies = List(
    "org.scalactic" %% "scalactic" % V.scalatest,
    "org.scalatest" %% "scalatest" % V.scalatest % "test"
  )

  val all: List[ModuleID] = sparkDependencies ++
    testDependencies ++
    JsonParsingDependencies ++
    commonDependencies

}
