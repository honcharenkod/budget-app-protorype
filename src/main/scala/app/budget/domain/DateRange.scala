package app.budget.domain

import io.getquill.extras.InstantOps
import zio.Duration

import java.time.Instant
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

final case class DateRange private (from: Instant, to: Instant):
  require(!to.isBefore(from), s"to ($to) must not be before from ($from)")

  def shiftBack(duration: Duration): DateRange =
    DateRange(from.minusSeconds(duration.getSeconds), to.minusSeconds(duration.getSeconds))

object DateRange:
  def ending(
      at: Instant = Instant.now(),
      length: Duration
  ): DateRange =
    DateRange(
      from = at.minusSeconds(length.toSeconds),
      to = at
    )


  def split(start: Instant, end: Instant, delay: Duration): List[DateRange] =
    @tailrec
    def nextRange(from: Instant, acc: ListBuffer[DateRange] = ListBuffer.empty): List[DateRange] =
      if (start.minusSeconds(delay.getSeconds) <= end)
        acc.addOne(DateRange(from, end)).toList
      else
        nextRange(from.minusSeconds(delay.getSeconds), acc.addOne(DateRange(from, from.minusSeconds(delay.getSeconds))))
    end nextRange
    nextRange(start)


