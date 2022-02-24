package rescueme.com.rescue_me_backoffice.domain.dogs

case class Dog(
                id: DogId,
                name: String
              )

object Dog {
  def apply(id: String, name: String): Dog = Dog(DogId(id), name)
}
