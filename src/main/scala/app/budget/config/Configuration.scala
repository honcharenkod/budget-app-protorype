package app.budget.config

import zio.*
import zio.config.*
import zio.config.magnolia.*

case class PostgresConfig(
    url: String,
    user: String,
    password: String,
    poolSize: Int
)

case class Configuration(
    monobank: MonoConfig,
    postgres: PostgresConfig
)

object Configuration:
  private given DeriveConfig[MonoConfig] = DeriveConfig(MonoConfig.config)

  val config: Config[Configuration] =
    deriveConfig[Configuration].mapKey(toKebabCase)

  val postgres: ZLayer[Configuration, Config.Error, PostgresConfig] =
    ZLayer.fromFunction((conf: Configuration) => conf.postgres)

  val layer: ZLayer[Any, Config.Error, Configuration] =
    ZLayer.fromZIO(ZIO.config(config))
