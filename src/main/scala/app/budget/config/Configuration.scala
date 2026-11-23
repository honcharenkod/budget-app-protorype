package app.budget.config

import zio.*
import zio.config.magnolia.deriveConfig
import zio.config.{ConfigOps, toKebabCase}

case class PostgresConfig(url: String, user: String, password: String, poolSize: Int)

case class Configuration(
    monoToken: String,
    monoStatementMaxDuration: Duration,
    postgres: PostgresConfig
)

object Configuration:
  val config: Config[Configuration] =
    deriveConfig[Configuration].mapKey(toKebabCase)

  val postgres: ZLayer[Configuration, Config.Error, PostgresConfig] =
    ZLayer.fromFunction((conf: Configuration) => conf.postgres)

  val layer: ZLayer[Any, Config.Error, Configuration] =
    ZLayer.fromZIO(ZIO.config(config))
