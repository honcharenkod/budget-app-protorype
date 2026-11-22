package app.budget.mono

import app.budget.config.Configuration
import app.budget.domain.DateRange
import app.budget.mono.model.api.{MonoClientInfo, StatementItem}
import zio.*
import zio.http.*

class MonoClientLive(client: Client, token: String) extends MonoClient:
  private val api =
    client
      .url(url"https://api.monobank.ua")
      .addHeader("X-Token", token)

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
  val layer: ZLayer[Client & Configuration, Nothing, MonoClient] =
    ZLayer.fromFunction((client: Client, conf: Configuration) =>
      new MonoClientLive(client, conf.monoToken)
    )
