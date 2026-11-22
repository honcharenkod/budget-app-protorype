package app.budget.mono

import app.budget.db.Transactor
import Transactor.transactional
import app.budget.config.Configuration
import app.budget.mono.model.api.StatementItem
import app.budget.mono.model.domain.{NewRawMonoStatement, RawMonoStatement}
import app.budget.utils.TimeUtils
import app.budget.utils.TimeUtils.DateRange
import zio.*
import zio.json.EncoderOps

class MonoServiceLive(
    client: MonoClient,
    repo: MonoRepo,
    config: Configuration
)(using Transactor)
    extends MonoService:

  final case class MonoStatementHelper(
      payload: StatementItem,
      range: DateRange,
      accountId: String
  )

  def importUserData: Task[Long] =
    for
      clientInfo <- client.getClientInfo
      statements <- ZIO.foreach(clientInfo.accounts) { account =>
        val range = TimeUtils.generatePeriodInPast(config.monoStatementMaxDuration)
        for
          items <- client.getPersonalStatement(account.id, range)
          payload <- ZIO
            .fromEither(items.toJsonAST)
            .mapError(err => new RuntimeException(s"JSON encode failed: $err"))
        yield NewRawMonoStatement(
          accountId = account.id,
          periodFrom = range.from,
          periodTo = range.to,
          payload = payload
        )
      }
      accounts = ZIO.foreach(clientInfo.accounts)(repo.createUserAccount)
      rawStatement =
        ZIO
          .foreach(statements)(repo.saveRaw)
      res <- accounts.zipLeft(rawStatement).transactional
    yield res.sum

object MonoServiceLive:
  val layer: ZLayer[
    MonoClient & MonoRepo & Transactor & Configuration,
    Nothing,
    MonoService
  ] =
    ZLayer.fromFunction(
      (
          client: MonoClient,
          repo: MonoRepo,
          conf: Configuration,
          tx: Transactor
      ) => MonoServiceLive(client, repo, conf)(using tx)
    )
