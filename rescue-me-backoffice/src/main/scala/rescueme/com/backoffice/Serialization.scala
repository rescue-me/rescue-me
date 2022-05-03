package rescueme.com.backoffice

import cats.effect.IO
import cats.syntax.all._
import fs2.kafka.vulcan.{avroDeserializer, avroSerializer, AvroSettings, SchemaRegistryClientSettings}
import fs2.kafka.{RecordDeserializer, RecordSerializer}
import vulcan.Codec

object Serialization {

  final case class Shelter(id: Long, name: String)
  final case class ShelterComputed(id: Long, name: String, computation: String)
  implicit val ShelterDecoder: Codec[Shelter] = Codec.record(
    name = "Shelter",
    namespace = "com.rescueme"
  ) { field =>
    (
      field("id", _.id),
      field("name", _.name)
    ).mapN(Shelter)
  }

  implicit val ShelterComputedDecoder: Codec[ShelterComputed] = Codec.record(
    name = "Shelter",
    namespace = "com.rescueme"
  ) { field =>
    (
      field("id", _.id),
      field("name", _.name),
      field("computation", _.computation)
    ).mapN(ShelterComputed)
  }

  val avroSettings: AvroSettings[IO] = AvroSettings {
    SchemaRegistryClientSettings[IO]("http://localhost:8081")
  }

  implicit val shelterSerializer: RecordSerializer[IO, ShelterComputed] =
    avroSerializer[ShelterComputed].using(avroSettings)
  implicit val shelterDeserializer: RecordDeserializer[IO, Shelter] =
    avroDeserializer[Shelter].using(avroSettings)
}
