import sbt._
import Keys._

object compiler {

  val settings = Seq(
    scalaVersion := "3.1.1",
    scalacOptions ++= options,
  )

  val options = Seq(
    "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
    "-encoding", "utf-8",                // Specify character encoding used by source files.
    "-explaintypes",                     // Explain type errors in more detail.
    "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
  )

}
