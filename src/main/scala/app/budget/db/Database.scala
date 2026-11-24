package app.budget.db

import app.budget.config.PostgresConfig
import com.zaxxer.hikari.*
import zio.*

import javax.sql.DataSource

object Database:
  val dataSource: ZLayer[PostgresConfig, Throwable, DataSource] =
    ZLayer.scoped {
      for
        cfg <- ZIO.service[PostgresConfig]
        ds  <- ZIO.fromAutoCloseable(ZIO.attempt {
          val hc = HikariConfig()
          hc.setJdbcUrl(cfg.url)
          hc.setUsername(cfg.user)
          hc.setPassword(cfg.password)
          hc.setMaximumPoolSize(cfg.poolSize)
          HikariDataSource(hc)
        })
      yield ds
    }
