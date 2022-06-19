package rescueme.com.app.domain.dog

import rescueme.com.app.domain._

import java.util.Date

case class DogDetail(
    dogId: Identifier,
    name: Name,
    breed: Breed,
    color: String,
    description: Description,
    gender: Gender,
    size: Size,
    dateOfBirth: Option[Date] = None,
    since: Option[Date] = None
)
