package rescueme.com.rescue_me_backoffice.system.akka

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import rescueme.com.rescue_me_backoffice.dog.infrastructure.endpoint.DogGetEndpoint
import rescueme.com.rescue_me_backoffice.system.domain.System

import scala.concurrent.{ExecutionContext, Future}

final class Routes()(implicit ec: ExecutionContext, system: System[Future]) {

  val dogGetEndpoint = new DogGetEndpoint(system.dogService)

  private val dog = path("admin" / "dog") {
    get {
      dogGetEndpoint.get()
    }
  }

  private val shelters = path("admin" / "shelter") {
    get {
      complete(StatusCodes.OK)
    }
  }

  val all: Route = dog ~ shelters
}
