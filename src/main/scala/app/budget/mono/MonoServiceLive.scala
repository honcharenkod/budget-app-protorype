package app.budget.mono

import app.budget.config.Configuration
import app.budget.db.Transactor
import app.budget.domain.DateRange
import app.budget.mono.api.MonoClient
import app.budget.mono.model.db.RawMonoStatement
import io.getquill.JsonbValue
import zio.*
import zio.json.ast.Json
import zio.stream.*

import java.time.Instant

class MonoServiceLive(
    client: MonoClient,
    repo: MonoRepo,
    config: Configuration
)(using Transactor)
    extends MonoService:

  def importAccountsAndStatements: Task[Long] =
    for
      clientInfo <- client.getClientInfo
      statements <- ZStream
        .fromIterable(clientInfo.accounts)
        .mapZIO(account => importHistory(account.id).runCollect)
        .runCollect
      res <- ZIO.foreach(clientInfo.accounts)(repo.insertAccountIfAbsent)
    yield res.sum

  def importHistory(
      accountId: String,
      startDate: Instant = Instant.now(),
      endDate: Option[Instant] = None
  ) =
    endDate match
      case Some(end) =>
        ZStream
          .fromIterable(
            DateRange
              .seq(startDate, end, config.monobank.statementRequestDuration)
          )
          .mapZIO(importStatement(accountId, _))
          .schedule(Schedule.spaced(config.monobank.statementRequestDelay))
      case None =>
        val range =
          DateRange.ending(startDate, config.monobank.statementRequestDuration)
        ZStream
          .unfoldZIO(range) { range =>
            importStatement(accountId, range).fold(
              e => None,
              s =>
                Some(
                  s -> range.shiftBack(config.monobank.statementRequestDuration)
                )
            )
          }
          .schedule(Schedule.spaced(config.monobank.statementRequestDelay))
          .take(10)

  def importStatement(accountId: String, range: DateRange): Task[Json] =
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
