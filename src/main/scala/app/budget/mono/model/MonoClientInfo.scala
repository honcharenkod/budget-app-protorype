package app.budget.mono.model

import app.budget.mono.model.*
import zio.schema.{DeriveSchema, Schema}

case class MonoClientInfo(
    clientId: String,
    name: String,
    webHookUrl: String,
    permissions: String,
    accounts: List[MonoAccount],
    jars: List[MonoJar]
)

object MonoClientInfo:
  implicit val schema: Schema[MonoClientInfo] =
    DeriveSchema.gen[MonoClientInfo]
