package service

import model.Hotel

import scala.concurrent.{ExecutionContext, Future}

class HotelService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) {
  import databaseService._
  import databaseService.driver.api._

  val hotels = TableQuery[daoType.Hotels]

  def getHotels(): Future[Seq[Hotel]] = db.run(hotels.result)

  def getHotel(id: Long): Future[Option[Hotel]] = db.run(hotels.filter(_.id === id).result.headOption)

  def getHotelsByName(name: String): Future[Seq[Hotel]] = db.run(hotels.filter(_.name === name).result)

  def createHotel(hotel: Hotel): Future[Hotel] = db.run(hotels returning hotels.map(_.id) into ((hotel, id) => hotel.copy(id=Some(id))) += hotel)

  def update(id: Long, toUpdate: Hotel): Future[Option[Hotel]] = getHotel(id).flatMap {
    case Some(hotel) => {
      val updatedHotel = Hotel(hotel.id, toUpdate.name, toUpdate.address, toUpdate.zip)
      db.run(hotels.filter(_.id === id).update(updatedHotel)).map(_ => Some(updatedHotel))
    }
    case None => Future.successful(None)
  }

  def deleteHotel(id: Long):Future[Int] = db.run(hotels.filter(_.id === id).delete)
}
