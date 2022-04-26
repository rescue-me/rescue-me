package rescueme.com.app

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import rescueme.com.app.domain.dog.Dog

trait Arbitraries {

  implicit val dog: Arbitrary[Dog] = Arbitrary[Dog] {
    for {
      name        <- arbitrary[String]
      breed       <- arbitrary[String]
      description <- arbitrary[String]
      id          <- Gen.option(Gen.long)
    } yield Dog(id, name, breed, description)
  }
}
