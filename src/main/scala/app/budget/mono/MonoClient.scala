package app.budget.mono

import zio.Task

trait MonoClient:
  def getClientInfo: Task[MonoClientInfo]
