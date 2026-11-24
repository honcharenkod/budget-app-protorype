package app.budget.mono

import zio.*

object MonoApi:
  val MaxStatementPeriod: Duration = 31.day + 1.hour
  val MinRequestInterval: Duration = 1.minute
