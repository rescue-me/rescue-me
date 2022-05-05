package rescueme.com.app.domain.dog

import rescueme.com.app.domain._

import java.util.UUID

case class DogDetail(
    dogId: UUID,
    color: String,
    description: Description,
    nature: String,
    tags: List[Tag]
)
