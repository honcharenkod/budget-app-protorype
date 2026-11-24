package app.budget.mono

import app.budget.config.Configuration
import app.budget.db.Transactor
import app.budget.db.Transactor.transactional
import app.budget.domain.DateRange
import app.budget.mono.api.MonoClient
import app.budget.mono.model.db.RawMonoStatement
import io.getquill.JsonbValue
import zio.*

class MonoServiceLive(
    client: MonoClient,
    repo: MonoRepo,
    config: Configuration
)(using Transactor)
    extends MonoService:

  def importAccountsAndStatements: Task[Long] =
    for
      clientInfo <- client.getClientInfo
      range = DateRange.ending(length =
        config.monobank.statementRequestDuration
      )
      statements <- ZIO.foreach(clientInfo.accounts) { account =>
        for
          itemsJson <- client.getPersonalStatement(account.id, range)
        yield RawMonoStatement.Insert(
          accountId = account.id,
          periodFrom = range.from,
          periodTo = range.to,
          payload = JsonbValue(itemsJson)
        )
      }
      accounts = ZIO.foreach(clientInfo.accounts)(repo.insertAccountIfAbsent)
      rawStatement =
        ZIO
          .foreach(statements)(repo.insertRawStatement)
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
