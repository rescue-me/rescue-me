package rescueme.com.app.config

final case class ServerConfig(host: String, port: Int)
final case class RescuemeConfig(db: DatabaseConfig, server: ServerConfig)
