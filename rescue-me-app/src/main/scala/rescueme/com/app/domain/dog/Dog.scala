package rescueme.com.app.domain.dog

case class Dog(id: Option[Long] = None, name: String, breed: String, description: String)
object Dog {
  def apply(name: String, breed: String, description: String) = new Dog(None, name, breed, description)
}
