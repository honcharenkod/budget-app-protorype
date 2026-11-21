package config

import zio.*
import zio.config.typesafe.TypesafeConfigProvider
import zio.config.magnolia.deriveConfig
import zio.config.{ConfigOps, toKebabCase}

import java.io.File

case class Configuration(monoToken: String)

object Configuration:
  val config: Config[Configuration] =
    deriveConfig[Configuration].mapKey(toKebabCase)

  val layer: ZLayer[Any, Config.Error, Configuration] =
    ZLayer.fromZIO(ZIO.config(config))
