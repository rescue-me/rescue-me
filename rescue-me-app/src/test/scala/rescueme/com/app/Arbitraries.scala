package rescueme.com.app

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import rescueme.com.app.domain.dog.Dog
import rescueme.com.app.domain.shelter.Shelter

import java.util.UUID

trait Arbitraries {

  implicit val dog: Arbitrary[Dog] = Arbitrary[Dog] {
    for {
      name        <- arbitrary[String]
      breed       <- arbitrary[String]
      description <- arbitrary[String]
    } yield Dog(Some(UUID.randomUUID()), name, breed, description, UUID.randomUUID())
  }

  val shelterGen: Gen[Shelter] = ???
  implicit val shelter: Arbitrary[Shelter] = ???
}
