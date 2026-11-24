package app.budget.domain

import zio.Duration

import java.time.Instant

final case class DateRange private (from: Instant, to: Instant):
  require(!to.isBefore(from), s"to ($to) must not be before from ($from)")

object DateRange:
  def ending(
      at: Instant = Instant.now(),
      length: Duration
  ): DateRange =
    DateRange(
      from = at.minusSeconds(length.toSeconds),
      to = at
    )
