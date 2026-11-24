package app.budget.mono.api

import zio.*

object MonoApi:
  val MaxStatementPeriod: Duration = 31.day + 1.hour
  val MinRequestInterval: Duration = 1.minute
  val RequestTimeout: Duration = 30.seconds
