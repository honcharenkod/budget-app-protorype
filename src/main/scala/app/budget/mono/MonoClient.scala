package app.budget.mono

import app.budget.mono.model.MonoClientInfo
import zio.Task

trait MonoClient:
  def getClientInfo: Task[MonoClientInfo]
