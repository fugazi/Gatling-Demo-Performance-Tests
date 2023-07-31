package simulations;

import static io.gatling.javaapi.core.CoreDsl.RawFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class AirportGapPerfTest extends Simulation {

    /**
     * Declare the base URL of the simulation on Gatling
     */
    private final String domain = System.getProperty("domain", "https://airportgap.dev-tester.com/api");

    /**
     * Authorization token for calling all endpoints for Performance testing
     */
    private static final String token = "BYdUpmNaHLpffkKLBxfQ5qUn";

    /**
     * Define all the variables to be used in the simulation
     */
    private static final String AIRPORT_ID = "MDE";

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
            .get(String.format("/api/airports/%s", AIRPORT_ID))
            .header("Authorization", token)
            .check(status().is(200));

    HttpRequestActionBuilder testPostAirportsDistance = http("Calculates the distance between two airports")
            .post("/api/airports/distance/")
            .body(RawFileBody("airportDistance.json"))
            .check(status().is(200));
}
