package rescueme.com.backoffice.kstream

import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import io.circe.generic.auto._
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.serialization.Serdes

import java.time.Duration
import java.util.Properties

object ShelterStreaming {

  object Implicits {
    import io.circe.parser._
    import io.circe.syntax._
    import io.circe.{Decoder, Encoder}

    implicit def serde[A >: Null: Decoder: Encoder]: Serde[A] = {
      val serializer = (a: A) => a.asJson.noSpaces.getBytes
      val deserializer = (aAsBytes: Array[Byte]) => {
        val aAsString = new String(aAsBytes)
        val aOrError  = decode[A](aAsString)
        aOrError match {
          case Right(a) => Option(a)
          case Left(error) =>
            println(s"There was an error converting the message $aOrError, $error")
            println(s"Value was: $aAsString")
            Option.empty
        }
      }
      Serdes.fromFn[A](serializer, deserializer)
    }
  }

  object Domain {
    case class Shelter(id: Long, name: String, province: String)
  }

  import org.apache.kafka.streams.scala.ImplicitConversions._
  import org.apache.kafka.streams.scala.serialization.Serdes._
  import Domain._
  import Implicits._

  val properties: Properties = {
    val props = new Properties
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "rescueme-backoffice-2")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props
  }

  val builder: StreamsBuilder                 = new StreamsBuilder()
  val shelterStream: KStream[String, Shelter] = builder.stream[String, Shelter]("shelter")

  shelterStream
    .peek((key, value) => println(s"Processing key: $key, value $value"))
    .mapValues(value => s"processed value $value")
    .to("output-shelter")

  def main(args: Array[String]): Unit = {

    val stream = new KafkaStreams(builder.build(), properties)
    stream.cleanUp()
    stream.start()
    sys.ShutdownHookThread {
      stream.close(Duration.ofSeconds(10))
    }
  }
}
