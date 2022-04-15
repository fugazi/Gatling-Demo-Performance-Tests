import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import ArtChicagoAPI._
import scala.concurrent.duration._

class ArtChicagoAPI extends Simulation {

    /*
    * Define the domain of the simulation on Gatling
    */
    val domain = System.getProperty("domain", "api.artic.edu/api/v1")

    /*
    * Authorization token for calling all endpoints for Performance testing
    * val token = System.getProperty("token", "")
     */

     /*
     * Define the http protocol builder to be simulated
      */
      val httpConf = http
        .baseURL("https://$domain/")
        .acceptHeader("application/json")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("en-US,en;q=0.5")
        //.header("Authorization", token)
        .userAgentHeader("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")
        .warmUp("https://$domain/")
        .check(status.is(200))

        /*
        * Create all the scenarios (requests) to be simulated
         */
        val testGetGalleries = scenario("Get Galleries")
          .exec(http("Get Galleries")
            .get("$domain/galleries")
            .check(status.is(200)))
        
        val testGetExhibitions = scenario("Get Exhibitions")
          .exec(http("Get Exhibitions")
            .get("$domain/exhibitions")
            .check(status.is(200)))

        val testGetArtworks = scenario("Get Artworks")
          .exec(http("Get Artworks")
            .get("$domain/artworks")
            .check(status.is(200)))

        val testGetAgents = scenario("Get Agents")
          .exec(http("Get Agents")
            .get("$domain/agents")
            .check(status.is(200)))
        
        val testGetPlaces = scenario("Get Places")
          .exec(http("Get Places")
            .get("$domain/places")
            .check(status.is(200)))
        
        /*
        * Sets up the scenarios that will be launched in this Simulation.
        */
        
        setUp (
          scnArtChicagoAPI.inject(atOnceUsers(USERS)),
        ).protocols(httpConf).assertions(global.successfulRequests.percent.is(SUCCESS_RATIO), global.failedRequests.count.is(FAILED_REQUESTS_COUNT),
        global.responseTime.mean.lt(RESPONSE_TIME))
}

/*
* Create the object with the parameters of the simulation on Gatling
*/
object ArtChicagoAPI {
    val USERS = System.getProperty("users", "2").toInt
    val FAILED_REQUESTS_COUNT = System.getProperty("failedRequestsCount", "0").toInt
    val RESPONSE_TIME = System.getProperty("responseTime", "1000").toInt
    val DURATION = System.getProperty("duration", "60").toInt
    val SUCCESS_RATIO = System.getProperty("successRatio", "100").toInt
}