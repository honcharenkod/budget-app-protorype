package app.budget.mono.api

import zio.*
import zio.test.*

class MonoClientSpec extends ZIOSpecDefault {

  def spec: Spec[TestEnvironment & Scope, Any] =
    suite("MonoClientSpec")()
}
