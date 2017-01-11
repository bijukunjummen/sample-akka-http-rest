import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import http.HotelRoutes
import service.{CloudFoundryHelper, DatabaseService, HotelService}
import slick.jdbc.{H2Profile, MySQLProfile}
import akka.http.scaladsl.Http

object AkkaHttpMicroservice extends App {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)


  val cfServicesHelper = new CloudFoundryHelper(sys.env)
  val databaseService = if (!cfServicesHelper.inCfCloud()) {
    val dbConfig = config.getConfig("sampledb")
    new DatabaseService(
      dbConfig.getString("url"), dbConfig.getString("user"), dbConfig.getString("password"), MySQLProfile)
  } else {
    val dbConfig = cfServicesHelper.getConfigFor("p-mysql", "mydb")
    new DatabaseService(
      dbConfig.getString("jdbcUrl"), dbConfig.getString("username"), dbConfig.getString("password"), MySQLProfile)
  }

  val hotelService = new HotelService(databaseService)
  val hotelRoutes = new HotelRoutes(hotelService)
  val routes = hotelRoutes.route

  val port = if (sys.env.contains("PORT")) sys.env("PORT").toInt else config.getInt("http.port")
  Http().bindAndHandle(routes, config.getString("http.interface"), port)
}
