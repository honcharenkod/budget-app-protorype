package app.budget.config

import app.budget.mono.api.MonoApi
import zio.*
import zio.config.magnolia.{DeriveConfig, deriveConfig}
import zio.config.{ConfigOps, toKebabCase}
import zio.http.URL

case class MonoConfig(
    apiUrl: URL,
    token: String,
    statementRequestDuration: Duration,
    statementRequestDelay: Duration
)

object MonoConfig:
  private given DeriveConfig[URL] =
    DeriveConfig[String].mapOrFail(str =>
      URL
        .decode(str)
        .left
        .map(e => Config.Error.InvalidData(message = e.getMessage))
    )

  val config: Config[MonoConfig] =
    deriveConfig[MonoConfig]
      .mapKey(toKebabCase)
      .validate(
        s"statement-request-duration must be <= ${MonoApi.MaxStatementPeriod.render}"
      )(
        _.statementRequestDuration <= MonoApi.MaxStatementPeriod
      )
      .validate(
        s"statement-request-delay must be >= ${MonoApi.MinRequestInterval.render}"
      )(
        _.statementRequestDelay >= MonoApi.MinRequestInterval
      )
