package app.budget.mono

import db.Transactor
import db.Transactor.transactional
import zio.*

class MonoServiceLive(client: MonoClient, repo: MonoRepo)(using Transactor)
    extends MonoService:

  def importUserData: Task[Long] =
    for
      clientInfo <- client.getClientInfo
      res <- ZIO
        .foreach(clientInfo.accounts)(a => repo.createUserAccount(a))
        .transactional
    yield res.sum

object MonoServiceLive:
  val layer: ZLayer[MonoClient & MonoRepo & Transactor, Nothing, MonoService] =
    ZLayer.fromFunction((client: MonoClient, repo: MonoRepo, tx: Transactor) =>
      MonoServiceLive(client, repo)(using tx)
    )
