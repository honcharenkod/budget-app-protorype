package app.budget.mono.api

import zio.Duration
import zio.http.Status

enum MonoApiError(message: String, cause: Throwable | Null = null)
    extends Exception(message, cause):
  case RateLimited extends MonoApiError("rate limited")

  case Decode(body: String, error: String)
      extends MonoApiError(s"decode failed: $error")

  case Timeout(duration: Duration)
      extends MonoApiError(s"timeout: ${duration.toSeconds}")

  case Unauthorized extends MonoApiError(s"unauthorized")

  case Unexpected(status: Status, body: String)
      extends MonoApiError(
        s"unexpected error with status $status and body $body"
      ) // 400, 404, 5xx

  case Transport(cause: Throwable) extends MonoApiError(s"transport error", cause)
