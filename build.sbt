ThisBuild / version      := "0.0.1"
ThisBuild / organization := "io.github.iozh"

val settings = compiler.settings ++ Seq()

lazy val `iozh-core` = project
  .settings( settings: _* )
  .settings(libraryDependencies ++= deps.iozhCore)

lazy val `iozh-main` = project
  .dependsOn(`iozh-core`)
  .settings( settings: _* )
  .settings(
    Compile / run / mainClass := Some("iozh.App")
  )
  .settings(libraryDependencies ++= deps.iozhMain)

lazy val root = (project in file("."))
  .settings(
    name := "IOzh"
  )
  .aggregate(
    `iozh-core`,
    `iozh-main`,
  )
