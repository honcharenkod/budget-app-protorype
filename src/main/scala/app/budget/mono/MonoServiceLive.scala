package app.budget.mono

import zio.*
import zio.http.Client

import java.sql.SQLException

class MonoServiceLive(client: MonoClient, repo: MonoRepo) extends MonoService:
  def importUserData: Task[Long] =
    for
      clientInfo <- client.getClientInfo
      res <- ZIO.foreach(clientInfo.accounts)(a => repo.createUserAccount(a))
    yield res.sum

object MonoServiceLive:
  val layer: ZLayer[MonoClient & MonoRepo, Nothing, MonoServiceLive] =
    ZLayer.fromFunction((monoClient: MonoClient, monoRepo: MonoRepo) => new MonoServiceLive(monoClient, monoRepo))
