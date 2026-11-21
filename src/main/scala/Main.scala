import app.budget.mono.{
  MonoClientLive,
  MonoRepo,
  MonoRepoLive,
  MonoService,
  MonoServiceLive
}
import config.Configuration
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.typesafe.TypesafeConfigProvider
import zio.http.ZClient

object Main extends ZIOAppDefault:

  override val bootstrap =
    Runtime.setConfigProvider(TypesafeConfigProvider.fromResourcePath())

  val run =
    val program =
      for
        monoService <- ZIO.service[MonoService]
        _ <- monoService.importUserData
      yield ()

    program.provide(
      Configuration.layer,
      Quill.DataSource.fromPrefix("postgres"),
      Quill.Postgres.fromNamingStrategy(SnakeCase),
      MonoRepoLive.layer,
      ZClient.default,
      MonoClientLive.layer,
      MonoServiceLive.layer
    )
