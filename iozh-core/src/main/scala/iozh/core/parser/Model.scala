package iozh.core.parser

import cats.data.{NonEmptyList => Nel}

object Model:

  case class NameRef(name: String, path: List[String] = Nil)
  case class Version(inclusive: Boolean, version: String)

  sealed trait StrucItem extends Product with Serializable
  sealed trait SpaceItem extends Product with Serializable
  sealed trait ServcItem extends Product with Serializable

  case class Exter(doc: String, name: Kind) extends SpaceItem with StrucItem
  case class Impor(doc: String, name: Kind) extends SpaceItem with StrucItem
  case class Alias(doc: String, alias: Kind, name: Kind) extends SpaceItem with StrucItem

  /** Represents a type, type constructor or a higher kind.
    *
    * Kind with empty params is a type.
    * Kind with non-empty params but with no nested kinds in them is a type constructor.
    * Any other kind is a real higher kind.
    */
  case class Kind(name: NameRef, params: List[Kind] = Nil)

  case class Mixin(doc: String, kind: Kind) extends StrucItem
  case class Using(doc: String, kind: Kind) extends StrucItem
  case class Embed(doc: String, name: String, struc: Struc) extends StrucItem
  case class Field(doc: String, name: String, opts: List[String], kind: Kind) extends StrucItem
  case class Typet(doc: String, name: String, opts: List[String], tag: String) extends StrucItem
  case class Wrapp(doc: String, name: Kind, opts: List[String], target: Kind) extends StrucItem
  case class EnumStr(doc: String, name: Kind, opts: List[String], target: String) extends StrucItem

  case class Struc(
    doc: String,
    kind: Option[Kind],
    isAbstract: Boolean = false,
    isEnum: Boolean = false,
    minVersion: Option[Version] = None,
    maxVersion: Option[Version] = None,
    opts: List[String] = Nil,
    items: List[StrucItem] = Nil,
  ) extends SpaceItem with StrucItem

  case class Defun(
    doc: String,
    kind: Kind,
    minVersion: Option[Version] = None,
    maxVersion: Option[Version] = None,
    opts: List[String] = Nil,
    dom: Either[Kind, Struc],
    cod: Either[Kind, Struc],
  ) extends ServcItem

  case class Servc(
    doc: String,
    kind: Kind,
    minVersion: Option[Version] = None,
    maxVersion: Option[Version] = None,
    opts: List[String] = Nil,
    items: List[ServcItem],
  ) extends SpaceItem

  case class Space(
    name: String,
    opts: List[String] = Nil,
    items: List[SpaceItem] = Nil,
  ) extends SpaceItem
