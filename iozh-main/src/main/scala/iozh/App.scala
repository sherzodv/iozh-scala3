package iozh

import cats.effect.{IO, IOApp, ExitCode}

object App extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO("boo")
      _ <- IO("foo")
      _ <- IO(println("hello"))
    } yield ExitCode.Success

  def msg = "I was compiled by Scala 3. :)"
