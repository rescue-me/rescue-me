package rescueme.com.backoffice

import cats.effect.{IO, IOApp}
import fs2.kafka._
import scala.concurrent.duration._

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {
    def processRecord(record: ConsumerRecord[String, String]): IO[(String, String)] =
      IO.println(s"processing record $record") *> IO.pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[IO, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId("rescueme-backoffice")

    val producerSettings =
      ProducerSettings[IO, String, String]
        .withBootstrapServers("localhost:9092")

    val stream =
      KafkaConsumer
        .stream(consumerSettings)
        .subscribeTo("pg-dev.public.shelter")
        .records
        .mapAsync(25) { committable =>
          processRecord(committable.record)
            .map {
              case (key, value) =>
                val record = ProducerRecord("output-shelter", key, value)
                ProducerRecords.one(record, committable.offset)
            }
        }
        .through(KafkaProducer.pipe(producerSettings))
        .map(_.passthrough)
        .through(commitBatchWithin(500, 15.seconds))

    IO.println("Stream started...") *> stream.compile.drain.handleErrorWith { thr =>
      IO.println(s"error occurred running stream $thr")
    }
  }

}
