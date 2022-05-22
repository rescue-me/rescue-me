package rescueme.com.app

import java.util.UUID

package object domain {
  type Province    = String
  type Name        = String
  type Breed       = String
  type Color       = String
  type Identifier  = UUID
  type Description = String
  type Tag         = String

  sealed trait Size
  object Size {
    case object Small  extends Size
    case object Medium extends Size
    case object Large  extends Size

    def fromString(value: String): Size = value.toLowerCase match {
      case "small" => Small
      case "large" => Large
      case _       => Medium
    }
  }

  sealed trait Gender
  object Gender {
    case object Male   extends Gender
    case object Female extends Gender
    def fromString(value: String): Gender = value.toLowerCase match {
      case "male" => Male
      case _      => Female
    }
  }

}
