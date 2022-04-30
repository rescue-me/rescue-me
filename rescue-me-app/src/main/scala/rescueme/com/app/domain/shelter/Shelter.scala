package rescueme.com.app.domain.shelter

import rescueme.com.app.domain._
import rescueme.com.app.domain.dog.Dog

case class Shelter(id: Option[Long], name: Name, province: Province, dogs: List[Dog])
object Shelter {
  def apply(name: Name, province: Province): Shelter = new Shelter(None, name, province, List())
}
