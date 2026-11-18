import config.Configuration
import zio.config.typesafe.TypesafeConfigProvider
import zio.{ZIOAppDefault, ZLayer}
import zio.ZIO

object Main extends ZIOAppDefault {
  val run = for {
    config <- Configuration.configProvider
  } yield Console.println(config.monoToken)
}
