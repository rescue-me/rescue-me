package rescueme.com.rescue_me_backoffice.dog.infrastructure.endpoint

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import rescueme.com.rescue_me_backoffice.dog.domain.DogService

import scala.concurrent.Future

class DogGetEndpoint(service: DogService[Future]) extends SprayJsonSupport with DogJsonFormatMarshaller {
  def get(): StandardRoute = complete(service.all())

}
