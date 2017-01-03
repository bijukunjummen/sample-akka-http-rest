import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import http.HotelRoutes
import service.{DatabaseService, HotelService}
import slick.jdbc.H2Profile
import akka.http.scaladsl.Http

object AkkaHttpMicroservice extends App  {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)



  val databaseService = new DatabaseService(config.getConfig("testdb"), H2Profile)
  val hotelService = new HotelService(databaseService)
  val hotelRoutes = new HotelRoutes(hotelService)
  val routes = hotelRoutes.route

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
