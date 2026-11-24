package app.budget.mono.api

import zio.http.Status

enum MonoApiError extends Exception:
  case RateLimited
  case Timeout
  case Unauthorized
  case Unexpected(status: Status, body: String) // 400, 404, 5xx
  case Transport(cause: Throwable)
  case Decode(body: String, error: String)
