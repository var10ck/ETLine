ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := Dependencies.V.scala

ThisBuild / organization := "com.etline"

lazy val root = (project in file("."))
  .settings(
    name := "ETLine"
  )
  .settings(BuildSettings.assemblySettings)
  .settings(libraryDependencies ++= Dependencies.all)
