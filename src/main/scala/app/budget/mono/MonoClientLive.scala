package app.budget.mono

import app.budget.config.Configuration
import app.budget.mono.model.api.StatementItem
import app.budget.mono.model.MonoClientInfo
import app.budget.mono.model.domain.RawMonoStatement
import app.budget.utils.TimeUtils.DateRange
import zio.*
import zio.http.*

class MonoClientLive(client: Client, apiKey: String) extends MonoClient:
  private val api =
    client
      .url(url"https://api.monobank.ua")
      .addHeader("X-Token", apiKey)

  def getClientInfo: Task[MonoClientInfo] =
    for
      response <- api.batched(Request.get(s"/personal/client-info"))
      body <- response.body.asJson[MonoClientInfo]
    yield body

  def getPersonalStatement(
      accountId: String,
      range: DateRange
  ): Task[List[StatementItem]] =
    for
      response <- api.batched(
        Request.get(s"/personal/statement/$accountId/${range.from.toEpochMilli}/${range.to.toEpochMilli}")
      )
      body <- response.body.asJson[List[StatementItem]]
    yield body

object MonoClientLive:
  val layer: ZLayer[Client & Configuration, Nothing, MonoClientLive] =
    ZLayer.fromFunction((client: Client, conf: Configuration) =>
      new MonoClientLive(client, conf.monoToken)
    )
