package iozh.core.parser

import cats.parse.Parser._
import cats.parse.{Parser, Parser0}
import cats.parse.Rfc5234.{char => asciiChar, _}

import cats.data.NonEmptyList

import cats.syntax.apply._
import cats.syntax.functor._
import cats.syntax.foldable._
import cats.instances.list._
import cats.instances.tuple._
import cats.instances.string._

import scala.language.implicitConversions
import scala.annotation.unchecked.uncheckedVariance

object Grammar:

  import Model._

  lazy val ws: Parser[Unit]             = wsp
  lazy val optWs: Parser0[Unit]         = wsp.rep0.void
  lazy val idenstart: Parser[Char]      = alpha | char('_').map(_ => '_')
  lazy val identifier: Parser[String]   = (idenstart.rep ~ (idenstart | digit).rep0).map(_ ++ _).string
  lazy val version: Parser[String]      = (charIn(".v") | digit).rep.string
  lazy val stringLit: Parser[String]    = string("\"") *> repUntil0(anyChar, string("\"")).string
  lazy val mComment: Parser[String]     = string("/*") *> repUntil0(anyChar, string("*/")).string
  lazy val lComment: Parser[String]     = string("--") *> repUntil0(anyChar, char('\n') | end).string
  lazy val comments: Parser0[String]    = (ws *> (mComment | lComment)).rep.map(_.intercalate("\n")).?.map(_.getOrElse(""))

  lazy val nameRef: Parser[NameRef] = repSep(identifier, char('.')).map {
    case NonEmptyList(head, Nil) => NameRef(head, Nil)
    case NonEmptyList(head, tail) =>
      val tr = tail.reverse.toList
      NameRef(tr.head, tr.tail :+ head)
  }

  lazy val minVer: Parser[Version] = (
    ws *> string(">=").as(true) | string(">").as(false),
    version
  ).mapN(Version.apply)

  lazy val maxVer: Parser[Version] = (
    ws *> (string("<=").as(true) | string("<").as(false)),
    version
  ).mapN(Version.apply)

  lazy val kind: Parser[Kind] = Parser.recursive[Kind] { kindp =>
    (nameRef.surroundedBy(optWs) ~
      repSep(kindp.surroundedBy(optWs), char(','))
        .between(char('['), char(']'))
        .?.map(_.map(_.toList).getOrElse(Nil))
    ).map(Kind.apply)
  }

  lazy val alias: Parser[Alias] = (
    comments.with1 <* string("type").surroundedBy(ws),
    kind <* char('=').surroundedBy(ws),
    kind
  ).mapN(Alias.apply)

  lazy val exter: Parser[Exter] = (
    comments.with1 <* string("provided").surroundedBy(ws),
    kind
  ).mapN(Exter.apply)

  lazy val impor: Parser[Impor] = (
    comments.with1 <* string("import").surroundedBy(ws),
    kind
  ).mapN(Impor.apply)

  lazy val options: Parser[NonEmptyList[String]] = (
    repSep(identifier, ws).between(char('('), char(')'))
  )

  lazy val enumItem: Parser[EnumStr] = (
    comments <* string("item").surroundedBy(ws),
    kind,
    opt(options).map(_.getOrElse(List.empty[String])),
    ws ~> string("=") ~> ws ~> stringLit
  ).mapN(EnumStr)

