package rescueme.com.app.domain.dog

import rescueme.com.app.domain._

import java.util.{Date, UUID}

case class DogDetail(
    dogId: UUID,
    name: Name,
    breed: Breed,
    color: String,
    description: Description,
    gender: Gender,
    size: Size,
    dateOfBirth: Option[Date] = None,
    since: Option[Date] = None
)
object DogDetail {
  def apply(
      dogId: UUID,
      name: Name,
      breed: Breed,
      color: String,
      description: Description,
      gender: Gender,
      size: Size
  ) =
    new DogDetail(dogId = dogId,
                  name = name,
                  breed = breed,
                  color = color,
                  description = description,
                  gender = gender,
                  size = size,
                  dateOfBirth = None,
                  since = None)
}
