package app.budget

import app.budget.config.Configuration
import app.budget.db.TransactorLive
import app.budget.mono.*
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.typesafe.TypesafeConfigProvider
import zio.http.ZClient

object Main extends ZIOAppDefault:

  override val bootstrap: ZLayer[Any, Nothing, Unit] =
    Runtime.setConfigProvider(TypesafeConfigProvider.fromResourcePath())

  val run: Task[Unit] =
    val program =
      for
        monoService <- ZIO.service[MonoService]
        _ <- monoService.importAccountsAndStatements
      yield ()

    program.provide(
      Configuration.layer,
      Quill.DataSource.fromPrefix("postgres"),
      Quill.Postgres.fromNamingStrategy(SnakeCase),
      MonoRepoLive.layer,
      ZClient.default,
      MonoClientLive.layer,
      MonoServiceLive.layer,
      TransactorLive.layer
    )
