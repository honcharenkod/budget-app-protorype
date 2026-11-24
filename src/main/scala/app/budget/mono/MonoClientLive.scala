package app.budget.mono

import app.budget.config.{Configuration, MonoConfig}
import app.budget.domain.DateRange
import app.budget.mono.model.api.{MonoClientInfo, StatementItem}
import zio.*
import zio.http.*

class MonoClientLive(client: Client, monoConfig: MonoConfig) extends MonoClient:
  private val api =
    client
      .url(monoConfig.apiUrl)
      .addHeader("X-Token", monoConfig.token)

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
        Request.get(s"/personal/statement/$accountId/${range.from.getEpochSecond}/${range.to.getEpochSecond}")
      )
      body <- response.body.asJson[List[StatementItem]]
    yield body

object MonoClientLive:
  val layer: ZLayer[Client & Configuration, Nothing, MonoClient] =
    ZLayer.fromFunction((client: Client, conf: Configuration) =>
      new MonoClientLive(client, conf.monobank)
    )
