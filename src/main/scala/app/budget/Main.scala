package app.budget

import app.budget.config.{Configuration, PostgresConfig}
import app.budget.db.{Database, TransactorLive}
import app.budget.mono.*
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.typesafe.TypesafeConfigProvider
import zio.http.ZClient
import zio.logging.slf4j.bridge.Slf4jBridge

object Main extends ZIOAppDefault:

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.setConfigProvider(TypesafeConfigProvider.fromResourcePath()) >>>
      Runtime.removeDefaultLoggers >>>
      zio.logging.consoleLogger() >+>
      Slf4jBridge.initialize

  val run: Task[Unit] =
    val program =
      for
        monoService <- ZIO.service[MonoService]
        _ <- monoService.importAccountsAndStatements
      yield ()

    program.provide(
      Configuration.layer,
      Configuration.postgres,
      Database.dataSource,
      Quill.Postgres.fromNamingStrategy(SnakeCase),
      MonoRepoLive.layer,
      ZClient.default,
      MonoClientLive.layer,
      MonoServiceLive.layer,
      TransactorLive.layer
    )
