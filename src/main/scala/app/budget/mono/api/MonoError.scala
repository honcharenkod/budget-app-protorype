package app.budget.mono.api

import zio.Duration
import zio.http.{Status, trailing}
import zio.json.JsonDecoder

case class MonoApiError(errorDescription: String) derives JsonDecoder
object MonoApiError extends Throwable:
  val OutOfHistoryMessage = "Value field 'to' out of bounds"

enum MonoError(message: String, cause: Throwable | Null = null)
    extends Exception(message, cause):
  case RateLimited extends MonoError("rate limited")

  case Decode(body: String, error: String)
      extends MonoError(s"decode failed: $error")

  case Timeout(duration: Duration)
      extends MonoError(s"timeout: ${duration.toSeconds}")

  case Unauthorized extends MonoError("unauthorized")

  case OutOfHistory extends MonoError("the limit of history has been reached")

  case Unexpected(status: Status, body: String)
      extends MonoError(
        s"unexpected error with status $status"
      ) // 400, 404, 5xx

  case Transport(cause: Throwable)
      extends MonoError(s"transport error", cause)
