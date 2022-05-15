package rescueme.com.app.domain.dog

import rescueme.com.app.domain._

import java.util.{Date, UUID}

case class DogDetail(
    dogId: Identifier,
    name: Name,
    breed: Breed,
    color: String,
    description: Description,
    gender: String,
    size: String,
    dateOfBirth: Option[Date] = None,
    since: Option[Date] = None
)
object DogDetail {
  def apply(
      dogId: Identifier,
      name: Name,
      breed: Breed,
      color: String,
      description: Description,
      gender: String,
      size: String,
      dateOfBirth: Option[Date] = None,
      since: Option[Date] = None
  ) =
    new DogDetail(dogId = dogId,
                  name = name,
                  breed = breed,
                  color = color,
                  description = description,
                  gender = gender,
                  size = size,
                  dateOfBirth,
                  since)
}
