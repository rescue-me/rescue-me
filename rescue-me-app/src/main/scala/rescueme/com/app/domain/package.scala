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
  case object Small  extends Size
  case object Medium extends Size
  case object Large  extends Size

  sealed trait Gender
  case object Male   extends Gender
  case object Female extends Gender

}
