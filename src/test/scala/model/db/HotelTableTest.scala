package model.db


import model.Hotel
import org.scalatest.{BeforeAndAfter, FlatSpec}
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._
import slick.jdbc.meta._

import scala.concurrent.duration._
import scala.concurrent.Await

class HotelTableTest extends FlatSpec with BeforeAndAfter {

  var db: Database = _
  implicit var session: Session = _
  val daoType = DAO(H2Profile)
  val hotels = TableQuery[daoType.Hotels]

  before {
    db = Database.forConfig("testdb")
    session = db.createSession()
  }

  "Operations using Hotel entity" should "Work" in {
    createSchema()
    val tables = Await.result(db.run(MTable.getTables), 2 seconds)
    assert(tables.size == 1)
    Await.result(db.run(hotels += Hotel(name = "Hotel1", address = "Address 1", zip = "Zip 1")), 2 seconds)
    Await.result(db.run(hotels += Hotel(name = "Hotel2", address = "Address 2", zip = "Zip 2")), 2 seconds)

    val q = for {
      h <- hotels
    } yield(h)

    val hotelsResult = Await.result(db.run(hotels.result), 2 seconds)
    println(hotelsResult)
  }

  def createSchema() =
    Await.result(db.run((hotels.schema).create), 2 seconds)

  after { db.close }

}
