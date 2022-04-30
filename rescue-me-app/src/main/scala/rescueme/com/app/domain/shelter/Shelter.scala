package rescueme.com.app.domain.shelter

import rescueme.com.app.domain._

case class Shelter(id: Option[Identifier], name: Name, province: Province)
object Shelter {
  def apply(name: Name, province: Province): Shelter = new Shelter(None, name, province)
}
