package service;

import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import model.db.DAO
import slick.jdbc.JdbcProfile

class DatabaseService(dbConfig: Config, val driver: JdbcProfile) {

  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(dbConfig.getString("url"))
  hikariConfig.setUsername(dbConfig.getString("user"))
  hikariConfig.setPassword(dbConfig.getString("password"))
  hikariConfig.setDriverClassName(dbConfig.getString("driver"))

  val dataSource = new HikariDataSource(hikariConfig)

  private val flywayService = new FlywayService(dataSource)
  println("Migrating database..")
  flywayService.migrateDatabaseSchema()

  import driver.api._
  val db = Database.forDataSource(dataSource)

  val daoType = DAO(driver)
  db.createSession()
}