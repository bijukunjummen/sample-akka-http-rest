package model.db

import model.Hotel
import slick.jdbc.JdbcProfile

case class DAO(val driver: JdbcProfile) {
  import driver.api._
  class Hotels(tag: Tag) extends Table[Hotel](tag, "HOTELS") {
    def id = column[Long]("hotel_id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("hotel_name")

    def address = column[String]("hotel_address")

    def zip = column[String]("zip")

    override def * = (id.?, name, address, zip) <> ((Hotel.apply _).tupled, Hotel.unapply)
  }

}