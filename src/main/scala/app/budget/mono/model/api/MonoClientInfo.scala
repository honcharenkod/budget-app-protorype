package app.budget.mono.model.api

import app.budget.mono.model.*
import app.budget.mono.model.api.MonoAccount
import zio.json.*
import zio.schema.{DeriveSchema, Schema}

case class MonoClientInfo(
    clientId: String,
    name: String,
    webHookUrl: String,
    permissions: String,
    accounts: List[MonoAccount],
    jars: List[MonoJar] = Nil
) derives JsonDecoder

object MonoClientInfo:
  implicit val schema: Schema[MonoClientInfo] =
    DeriveSchema.gen[MonoClientInfo]

  implicit val codec: JsonCodec[StatementItem] =
    DeriveJsonCodec.gen[StatementItem]
