package rescueme.com.app

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import rescueme.com.app.domain.dog.Dog

import java.util.UUID

trait Arbitraries {

  implicit val dog: Arbitrary[Dog] = Arbitrary[Dog] {
    for {
      name        <- arbitrary[String]
      breed       <- arbitrary[String]
      description <- arbitrary[String]
    } yield Dog(Some(UUID.randomUUID()), name, breed, description, UUID.randomUUID())
  }
}
