package simulations;

import static io.gatling.javaapi.core.CoreDsl.RawFileBody;
import static io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import java.time.Duration;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class AirportGapPerfTest extends Simulation {

    /**
     * Declare the base URL of the simulation on Gatling
     */
    private final String domain = System.getProperty("domain", "airportgap.dev-tester.com/api");

    /**
     * Authorization token for calling all endpoints for Performance testing
     */
    private static final String token = "BYdUpmNaHLpffkKLBxfQ5qUn";

    /**
     * Define all the variables to be used in the simulation
     */
    private static final String AIRPORT_ID = "MDE";
    private static final String AIRPORT_FAVORITE_ID = "1";

    /**
     * Define the http protocol builder to be simulated
     */
    HttpProtocolBuilder httpConf = http
            .baseUrl("https://" + domain + "/")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .acceptEncodingHeader("gzip")
            .header("Authorization", token)
            .warmUp("https://" + domain + "/")
            .check(status().is(200));

    /**
     * Declare all the Test scenarios (requests) to be simulated with Gatling
     */
    HttpRequestActionBuilder testGetAirports = http("Get all airports in the Airport Gap database")
            .get("/airports")
            .check(status().is(200));

    HttpRequestActionBuilder testGetAirportsById = http("Get the airport specified by the ID")
            .get(String.format("/airports/%s", AIRPORT_ID))
            .check(status().is(200));

    HttpRequestActionBuilder testPostAirportsDistance = http("Post Calculates the distance between two airports")
            .post("/airports/distance/")
            .header("Authorization", token)
            .body(RawFileBody("airportDistance.json"))
            .check(status().is(200));

    HttpRequestActionBuilder testPostAirportFavorite = http("Post a favorite airport to your Airport Gap account")
            .post("/favorites/")
            .header("Authorization", token)
            .body(RawFileBody("airportFavorites.json"))
            .check(status().is(200));

    HttpRequestActionBuilder testGetFavorites = http("Get all the favorite airports saved to your Airport Gap account")
            .get("/favorites")
            .header("Authorization", token)
            .check(status().is(200));

    HttpRequestActionBuilder testGetFavoritesById = http("Get a favorite airport specified by the ID")
            .get(String.format("/favorites/%s", AIRPORT_FAVORITE_ID))
            .header("Authorization", token)
            .check(status().is(200));

    HttpRequestActionBuilder testPatchFavoriteAirport = http("Patch the note of one of your favorite airports")
            .patch(String.format("/favorites/%s", AIRPORT_FAVORITE_ID))
            .header("Authorization", token)
            .body(RawFileBody("airportNotes.json"))
            .check(status().is(200));

    /**
     * Sets up the scenarios that will be launched in this Simulation.
     */
    ScenarioBuilder scnAirportGapPerfTest = scenario("Airport Gap Performance Test")
            .exec(testGetAirports)
            .exec(testGetAirportsById)
            .exec(testPostAirportsDistance)
            .exec(testPostAirportFavorite)
            .exec(testGetFavorites)
            .exec(testGetFavoritesById)
            .exec(testPatchFavoriteAirport);

    /**
     * Sets up the parameters of the simulation to be launched by Gatling
     */
    public AirportGapPerfTest() {
        setUp(scnAirportGapPerfTest.injectClosed(constantConcurrentUsers(5).during(Duration.ofSeconds(5))))
                .protocols(httpConf)
                .assertions(global().successfulRequests().percent().gt(99.0),
                        global().responseTime().percentile(99.0).lt(6000)
                );
    }
}
