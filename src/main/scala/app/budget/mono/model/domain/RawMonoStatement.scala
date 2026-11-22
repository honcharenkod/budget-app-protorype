package app.budget.mono.model.domain

import zio.json.ast.Json
import zio.schema.{DeriveSchema, Schema}

import java.time.Instant

case class RawMonoStatement(
    id: Long = 0,
    accountId: String,
    periodFrom: Instant,
    periodTo: Instant,
    fetchedAt: Instant = Instant.now(),
    payload: Json
)
