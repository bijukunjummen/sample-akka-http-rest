package http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import model.Hotel
import service.HotelService
import spray.json.DefaultJsonProtocol

trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val hotelFormat = jsonFormat4(Hotel)
}

class HotelRoutes(val hotelService: HotelService) extends Protocols {

  val route = logRequestResult("HotelRoutes") {
    pathPrefix("hotels") {
      pathEndOrSingleSlash {
        get {
          complete {
            hotelService.getHotels()
          }
        } ~
          post {
            entity(as[Hotel]) { hotelForCreate =>
              complete {
                hotelService.createHotel(hotelForCreate)
              }
            }
          }
      } ~
        pathPrefix(LongNumber) { id =>
          get {
            complete {
              hotelService.getHotel(id)
            }
          } ~
            put {
              entity(as[Hotel]) { hotelForUpdate =>
                complete {
                  hotelService.update(id, hotelForUpdate)
                }
              }
            } ~
            delete {
              onSuccess(hotelService.deleteHotel(id)) { _ =>
                complete(NoContent)
              }
            }
        }
    }
  }

}
