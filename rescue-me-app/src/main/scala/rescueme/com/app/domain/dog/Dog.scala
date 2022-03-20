package rescueme.com.app.domain.dog

import java.util.UUID

case class Dog(name: String,
               breed: String,
               description: String,
               id: Option[UUID] = Some(UUID.randomUUID()))
