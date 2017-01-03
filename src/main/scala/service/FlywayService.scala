package service
import javax.sql.DataSource

import org.flywaydb.core.Flyway

class FlywayService(dataSource: DataSource) {

  private[this] val flyway = new Flyway()
  flyway.setDataSource(dataSource)

  def migrateDatabaseSchema() : Unit = flyway.migrate()

  def dropDatabase() : Unit = flyway.clean()
}
