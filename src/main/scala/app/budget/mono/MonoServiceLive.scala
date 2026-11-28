package app.budget.mono

import app.budget.config.Configuration
import app.budget.db.Transactor
import app.budget.domain.DateRange
import app.budget.mono.api.{MonoClient, MonoError}
import app.budget.mono.api.MonoError.{OutOfHistory, RateLimited}
import app.budget.mono.model.db.RawMonoStatement
import io.getquill.JsonbValue
import zio.{Schedule, *}
import zio.json.ast.Json
import zio.stream.*

import java.time.Instant

class MonoServiceLive(
    client: MonoClient,
    repo: MonoRepo,
    config: Configuration
)(using Transactor)
    extends MonoService:

  private val period = config.monobank.statementRequestDuration
  private val delay = config.monobank.statementRequestDelay
  private val retryOnRateLimit =
    Schedule.spaced(delay) &&
      Schedule.recurs(3) &&
      Schedule.recurWhile(_ == RateLimited)

  def importAccountsAndStatements: Task[Long] =
    for
      clientInfo <- client.getClientInfo
      _ <- ZStream
        .fromIterable(clientInfo.accounts)
        .flatMap(account =>
          importFullHistory(account.id, startDate = Instant.now())
        )
        .runDrain
      res <- ZIO.foreach(clientInfo.accounts)(repo.insertAccountIfAbsent)
    yield res.sum

  private def importFullHistory(
      accountId: String,
      startDate: Instant
  ): Stream[Throwable, Json] =
    val range =
      DateRange.ending(startDate, period)
    ZStream
      .unfoldZIO(range) { range =>
        importStatement(accountId, range)
          .retry(retryOnRateLimit)
          .map(json => json -> range.shiftBack(period))
          .asSome
          .catchSome { case OutOfHistory => ZIO.none }
      }
      .schedule(Schedule.spaced(delay))

  private def importStatement(accountId: String, range: DateRange): Task[Json] =
    for
      statement <- client.getPersonalStatement(accountId, range)
      _ <- repo.insertRawStatement(
        RawMonoStatement.Insert(
          accountId = accountId,
          periodFrom = range.from,
          periodTo = range.to,
          payload = JsonbValue(statement)
        )
      )
    yield statement

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
