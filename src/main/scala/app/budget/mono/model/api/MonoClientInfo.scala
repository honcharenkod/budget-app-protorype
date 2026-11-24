package app.budget.mono.model.api

import app.budget.mono.model.*
import app.budget.mono.model.api.MonoAccount
import zio.json.*

case class MonoClientInfo(
    clientId: String,
    name: String,
    webHookUrl: String,
    permissions: String,
    accounts: List[MonoAccount],
    jars: List[MonoJar] = Nil
) derives JsonDecoder
