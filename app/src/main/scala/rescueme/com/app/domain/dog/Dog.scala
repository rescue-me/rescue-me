package rescueme.com.app.domain.dog

import rescueme.com.app.domain._

case class Dog(
    id: Option[Identifier] = None,
    name: Name,
    breed: Breed,
    description: Description,
    shelterId: Identifier
)
object Dog {
  def apply(name: Name, breed: Breed, description: Description, shelterId: Identifier) =
    new Dog(None, name, breed, description, shelterId)
}
