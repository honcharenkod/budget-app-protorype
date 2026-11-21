package app.budget.mono

import config.Configuration
import zio.*
import zio.http.*
import zio.json.*

class MonoClientLive(client: Client, apiKey: String) extends MonoClient:
  val api =
    client.url(url"https://api.monobank.ua")
      .addHeader("X-Token", apiKey)

  def getClientInfo: Task[MonoClientInfo] =
    for
      response <- api.batched(Request.get(s"/personal/client-info"))
      body <-    response.body.asJson[MonoClientInfo]
    yield body

object MonoClientLive:
  val layer: ZLayer[Client & Configuration, Nothing, MonoClientLive] =
    ZLayer.fromFunction((client: Client, conf: Configuration) => new MonoClientLive(client, conf.monoToken))
