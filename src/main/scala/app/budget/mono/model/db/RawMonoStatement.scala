package app.budget.mono.model.db

import zio.json.ast.Json
import zio.schema.{DeriveSchema, Schema}

import java.time.Instant

case class RawMonoStatement(
    id: Long,
    accountId: String,
    periodFrom: Instant,
    periodTo: Instant,
    fetchedAt: Instant,
    payload: Json
)
