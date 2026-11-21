package app.budget.mono

import zio.json.JsonDecoder
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
  implicit val personSchema: Schema[MonoClientInfo] = DeriveSchema.gen[MonoClientInfo]

