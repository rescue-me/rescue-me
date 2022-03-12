package rescueme.com.rescue_me_backoffice.system.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import rescueme.com.rescue_me_backoffice.system.domain.System

final case class Controller()(implicit ec: ExecutionContext, system: System[Future]) {

  val routes = new Routes()

  def run(serverConfig: HttpServerConfig)(implicit actorSystem: ActorSystem): Unit = {
    val host = serverConfig.host
    val port = serverConfig.port

    val bindingFuture = Http().newServerAt(host, port).bind(routes.all)


    bindingFuture.failed.foreach { t =>
      println(s"Failed to bind to http://$host:$port/:")
    }

    // let it run until user presses return
    println(s"Server online at http://$host:$port/\nPress RETURN to stop...")
    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())
  }
}
