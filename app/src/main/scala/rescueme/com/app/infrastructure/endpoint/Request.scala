package rescueme.com.app.infrastructure.endpoint

import rescueme.com.app.domain._
import rescueme.com.app.domain.dog.DogDetail

object Request {

  case class DogDetailsRequest(
      name: String,
      breed: String,
      color: String,
      description: String,
      gender: String,
      size: String
  ) {
    def toDogDetails(id: Identifier): DogDetail =
      DogDetail(
        id,
        name,
        breed,
        color,
        description,
        Gender.fromString(gender),
        Size.fromString(size)
      )
  }
}

object Response {
  case class DogDetailsResponse(
      dogId: String,
      breed: String,
      color: String,
      description: String,
      gender: String,
      size: String
  )
  implicit class DogDetailsResponseMapper(detail: DogDetail) {
    def toResponse: DogDetailsResponse = DogDetailsResponse(
      detail.dogId.toString,
      detail.breed,
      detail.color,
      detail.description,
      detail.gender.toString,
      detail.size.toString
    )
  }
}
