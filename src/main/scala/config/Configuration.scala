package config

import zio.{Config, ConfigProvider, IO}
import zio.config.typesafe.TypesafeConfigProvider
import zio.config.magnolia.deriveConfig
import zio.config.{ConfigOps, toKebabCase}

import java.io.File

case class Configuration(monoToken: String)

object Configuration {
  private val config: Config[Configuration] =
    deriveConfig[Configuration].mapKey(toKebabCase)

  val configProvider: IO[Config.Error, Configuration] =
    TypesafeConfigProvider
      .fromHoconFile(
        new File("src/main/resources/application.conf")
      )
      .load(config)
}
