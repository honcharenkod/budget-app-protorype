package app.budget.utils

import zio.Duration

import java.time.{Instant, LocalDate}

object TimeUtils:
  final case class DateRange(from: Instant, to: Instant):
    require(!to.isBefore(from), s"to ($to) must not be before from ($from)")

  def generatePeriodInPast(
      duration: Duration,
      startTime: Instant = Instant.now()
  ): DateRange =
    DateRange(
      from = startTime.minusNanos(duration.toNanos),
      to = startTime
    )
