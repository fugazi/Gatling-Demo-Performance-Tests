import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import SpaceflightNewsAPI._
import scala.concurrent.duration._

class SpaceflightNewsAPI extends Simulation {

    /*
    * Define the domain of the simulation on Gatling
    */
    val domain = System.getProperty("domain", "api.spaceflightnewsapi.net/v3")

    /*
    * Authorization Bearer token for calling all endpoints for Performance testing
     */
    val token = "Bearer <Your API key>"

     /*
     * Define the http protocol builder to be simulated
      */
      val httpConf = http
        .baseURL("https://$domain/")
        .acceptHeader("application/json")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .header("Authorization", token)
        .userAgentHeader("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")
        .warmUp("https://$domain/")
        .check(status.is(200))

        /*
        * Create all the scenarios (requests) to be simulated
         */
        val testGetArticles = scenario("Get Articles")
          .exec(http("Get Articles")
            .get("$domain/articles")
            .header("Authorization", token)
            .check(status.is(200)))
        
        val testGetBlogs = scenario("Get Blogs")
          .exec(http("Get Blogs")
            .get("$domain/blogs")
            .header("Authorization", token)
            .check(status.is(200)))

        val testGetInfo = scenario("Get Info")
          .exec(http("Get Info")
            .get("$domain/info")
            .header("Authorization", token)
            .check(status.is(200)))

        val testGetReports = scenario("Get Reports")
          .exec(http("Get Reports")
            .get("$domain/reports")
            .header("Authorization", token)
            .check(status.is(200)))
        
        val testGetReportsID = scenario("Get Reports {id}")
          .exec(http("Get Reports {id}")
            .get("$domain/reports/1014")
            .header("Authorization", token)
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
object SpaceflightNewsAPI {
    val USERS = System.getProperty("users", "2").toInt
    val FAILED_REQUESTS_COUNT = System.getProperty("failedRequestsCount", "0").toInt
    val RESPONSE_TIME = System.getProperty("responseTime", "1000").toInt
    val DURATION = System.getProperty("duration", "60").toInt
    val SUCCESS_RATIO = System.getProperty("successRatio", "100").toInt
}