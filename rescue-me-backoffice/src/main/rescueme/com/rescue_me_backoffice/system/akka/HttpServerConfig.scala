package rescueme.com.rescue_me_backoffice.system.akka

import com.typesafe.config.Config

final case class HttpServerConfig(host: String, port: Int) {
  def addPath(path: String): String =
    s"$host:$port/$path"
}

object HttpServerConfig {
  def apply(serverConfig: Config): HttpServerConfig =
    HttpServerConfig(serverConfig.getString("http-server.host"), serverConfig.getInt("http-server.port"))
}
