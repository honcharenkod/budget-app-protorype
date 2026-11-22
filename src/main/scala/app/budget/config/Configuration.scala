package app.budget.config

import zio.*
import zio.config.magnolia.deriveConfig
import zio.config.{ConfigOps, toKebabCase}


case class Configuration(
    monoToken: String,
    monoStatementMaxDuration: Duration
)

object Configuration:
  val config: Config[Configuration] =
    deriveConfig[Configuration].mapKey(toKebabCase)

  val layer: ZLayer[Any, Config.Error, Configuration] =
    ZLayer.fromZIO(ZIO.config(config))
