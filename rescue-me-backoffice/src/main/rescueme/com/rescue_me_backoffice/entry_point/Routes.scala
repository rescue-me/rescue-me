package rescueme.com.rescue_me_backoffice.entry_point

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext

final class Routes()(implicit ec: ExecutionContext) {

  import akka.http.scaladsl.server.Directives._

  private val dog = path("admin" / "user") {
    get {
      complete(StatusCodes.OK)
    }
  }

  private val shelters = path("admin" / "shelter") {
    get {
      complete(StatusCodes.OK)
    }
  }

  val all: Route = dog ~ shelters
}
