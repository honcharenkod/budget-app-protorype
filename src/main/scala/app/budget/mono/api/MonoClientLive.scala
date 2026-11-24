package app.budget.mono.api

import app.budget.config.{Configuration, MonoConfig}
import app.budget.domain.DateRange
import app.budget.mono.api.MonoApiError.*
import app.budget.mono.model.api.MonoClientInfo
import zio.*
import zio.http.*
import zio.json.*
import zio.json.ast.Json

class MonoClientLive(client: Client, monoConfig: MonoConfig) extends MonoClient:
  private val api =
    client
      .url(monoConfig.apiUrl)
      .addHeader("X-Token", monoConfig.token)

  def getClientInfo: IO[MonoApiError, MonoClientInfo] =
    for
      response <- api
        .batched(Request.get(s"/personal/client-info"))
        .mapError(Transport(_))
        .timeoutFail(Timeout(MonoApi.RequestTimeout))(MonoApi.RequestTimeout)
      body <- validateResponse(response)
      info <-
        ZIO
          .fromEither(body.fromJson[MonoClientInfo])
          .mapError(e => Decode(body, e))
    yield info

  def getPersonalStatement(
      accountId: String,
      range: DateRange
  ): IO[MonoApiError, Json] =
    for
      response <- api
        .batched(
          Request.get(
            s"/personal/statement/$accountId/${range.from.getEpochSecond}/${range.to.getEpochSecond}"
          )
        )
        .mapError(Transport(_))
        .timeoutFail(Timeout(MonoApi.RequestTimeout))(MonoApi.RequestTimeout)
      body <- validateResponse(response)
      info <-
        ZIO
          .fromEither(body.fromJson[Json])
          .mapError(e => Decode(body, e))
    yield info

  private def validateResponse(response: Response): IO[MonoApiError, String] =
    for
      body <- response.body.asString.mapError(Transport(_))
      _ <- response.status match
        case Status.Ok                              => ZIO.unit
        case Status.TooManyRequests                 => ZIO.fail(RateLimited)
        case Status.Unauthorized | Status.Forbidden => ZIO.fail(Unauthorized)
        case _ => ZIO.fail(Unexpected(response.status, body))
    yield body

object MonoClientLive:
  val layer: ZLayer[Client & Configuration, Nothing, MonoClient] =
    ZLayer.fromFunction((client: Client, conf: Configuration) =>
      new MonoClientLive(client, conf.monobank)
    )
