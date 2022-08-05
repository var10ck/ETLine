// SBT
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys.{assembly, assemblyJarName, assemblyMergeStrategy}
import sbtassembly.{MergeStrategy, PathList}

import scala.util.matching.Regex

/** To enable any of these you need to explicitly add Settings value to build.sbt */
object BuildSettings {

  lazy val buildSettings = Seq(
    addCompilerPlugin(
      "org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full
    )
  )

  // sbt-assembly settings
  lazy val meta: Regex = """META.INF(.)*""".r
  lazy val assemblySettings = Seq(
    assembly / assemblyJarName := {
      moduleName.value + "-" + version.value + ".jar"
    },
    // autorun tests on assembly(build)
//    assembly / test            := (Test / test).value,
    assembly / assemblyMergeStrategy := {
      case PathList("javax", "servlet", xs @ _*)           => MergeStrategy.first
      case PathList(ps @ _*) if ps.last endsWith ".html"   => MergeStrategy.first
      case n if n.contains("services")                     => MergeStrategy.concat
      case n if n.startsWith("reference.conf")             => MergeStrategy.concat
      case n if n.endsWith(".conf")                        => MergeStrategy.concat
      case meta(_)                                         => MergeStrategy.discard
      case _                                               => MergeStrategy.first
    },
    Compile / resourceDirectory := baseDirectory.value / "src" / "main" / "resources",
    Compile / run := Defaults
      .runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner)
      .evaluated,
    Compile / runMain := Defaults
      .runMainTask(Compile / fullClasspath, Compile / run / runner)
      .evaluated
  )
}
