package rescueme.com.app.config

import cats.effect.{Async, Resource}
import doobie.hikari.HikariTransactor

import scala.concurrent.ExecutionContext

case class DatabaseConnectionsConfig(poolSize: Int)
case class DatabaseConfig(
    url: String,
    driver: String,
    user: String,
    password: String,
    connections: DatabaseConnectionsConfig
)

object DatabaseConfig {
  def dbTransactor[F[_]: Async](
      dbc: DatabaseConfig,
      connEc: ExecutionContext
  ): Resource[F, HikariTransactor[F]] =
    HikariTransactor
      .newHikariTransactor[F](dbc.driver, dbc.url, dbc.user, dbc.password, connEc)
}
