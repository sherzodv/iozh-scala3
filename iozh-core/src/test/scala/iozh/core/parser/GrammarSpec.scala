package iozh.core.parser

import cats.data.NonEmptyList

import org.scalatest._
import flatspec._
import matchers._
import cats.parse.Parser.Error

class GrammarSpec extends AnyFlatSpec with should.Matchers:

  import Model._
  type LeftAny = Left[?, ?]

  object Nel:
    def apply[A](a: A) = NonEmptyList(a, Nil)
    def apply[A](a: A, b: A*) = NonEmptyList(a, List(b: _*))

  "learning cats parse" must "be fast" in {

    Grammar.ws.parse(" ") shouldBe Right("" -> ())

    Grammar.idenstart.parse("boo") shouldBe Right("oo" -> 'b')
    Grammar.idenstart.parse("_boo") shouldBe Right("boo" -> '_')
    Grammar.idenstart.parse("1boo") shouldBe a [LeftAny]

    Grammar.identifier.parse("1boo") shouldBe a [LeftAny]
    Grammar.identifier.parse("_boo") shouldBe Right("" -> "_boo")

    Grammar.nameRef.parse(".b.c+") shouldBe a [LeftAny]
    Grammar.nameRef.parse(".a.b") shouldBe a [LeftAny]
    Grammar.nameRef.parse("a.b.c") shouldBe Right("", NameRef("c", List("b", "a")))
    Grammar.nameRef.parse("a.b.") shouldBe Right(".", NameRef("b", List("a")))

    Grammar.kind.parse("a") shouldBe Right("", Kind(NameRef("a")))
    Grammar.kind.parse("a.b") shouldBe Right("", Kind(NameRef("b", List("a"))))
    Grammar.kind.parse("a.b[x]") shouldBe Right("", Kind(NameRef("b", List("a")), List(Kind(NameRef("x")))))
  }
