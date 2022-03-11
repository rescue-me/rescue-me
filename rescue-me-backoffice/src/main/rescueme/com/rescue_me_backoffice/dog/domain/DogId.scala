package rescueme.com.rescue_me_backoffice.dog.domain

import java.util.UUID

case class DogId(id: UUID)

object DogId {
  def apply(id: String): DogId = DogId(UUID.fromString(id))
}
