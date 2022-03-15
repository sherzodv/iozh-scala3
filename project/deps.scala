import sbt._

object deps {

  object V {
    val catsParse  = "0.3.6"
    val catsEffect = "3.3.5"
  }


  val catsParse = Seq(
    "org.typelevel" %% "cats-parse" % V.catsParse,
  )

  val catsEffect = Seq(
    "org.typelevel" %% "cats-effect" % V.catsEffect,
  )

  val scalaTest = Seq(
    "org.scalactic" %% "scalactic" % "3.2.11" % "test",
    "org.scalatest" %% "scalatest" % "3.2.11" % "test",
  )


  val common =
    catsParse ++
    catsEffect ++
    scalaTest ++
    Seq()

  val iozhCore: Seq[ModuleID] = common
  val iozhMain: Seq[ModuleID] = common

}
