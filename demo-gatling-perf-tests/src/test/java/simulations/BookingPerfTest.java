package simulations;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

/**
 * This class represents a performance test for Restful-Booker web service.
 * It uses Gatling to simulate users authenticating and making requests to the web service.
 */
public class BookingPerfTest extends Simulation {

    private final String domain = System.getProperty("domain", "restful-booker.herokuapp.com");
    private static final String ENDPOINT = "/booking";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";
    private static final String CONTENT_TYPE = "application/json";
    private static final String ACCEPT_HEADER = "application/json;q=0.9;v=1";
    private static final String BOOK_HEADER= "1";
    private static final String USER_AGENT_HEADER = "test";

    private final String secureToken = "";

    /**
     * Performs user authentication from authWebService by making an HTTP POST request and extracts
     * the secure token from the response.
     *
     * @return a ChainBuilder object representing the sequence of actions for AuthWebService
     */
    private ChainBuilder authWebService() {
        return exec(http("authWebService")
                .post("https://restful-booker.herokuapp.com/auth")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("username", USERNAME)
                .formParam("password", PASSWORD)
                .check(status().is(200))
                .check(jsonPath("$.token").saveAs("secureToken")));
    }

    HttpProtocolBuilder httpConf = http
            .baseUrl("https://" + domain + "/")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .acceptEncodingHeader("gzip");

    /**
     * Represents an HTTP request to Creates a new Booking in the API for testing performance.
     * It includes various headers and checks the response status.
     */
    HttpRequestActionBuilder testBookingWebService = http("CreateBookingWebService")
            .post(ENDPOINT)
            .header("Content-Type", CONTENT_TYPE)
            .header("Accept", ACCEPT_HEADER)
            .header("Sequence", BOOK_HEADER)
            .header("User-Agent", USER_AGENT_HEADER)
            .header("Identity-Token", secureToken)
            .check(status().is(201));

    /**
     * Represents a sequence of actions for user authentication and obtaining a secure token.
     * It includes making a POST request to the authWebService and extracting the secure token from the response.
     */
    ChainBuilder executeAuthWebService = authWebService();

    /**
     * Represents the scenario for the CreateBookingWebService Performance Test.
     * It includes user authentication and a pause to obtain the secure token, followed by the CreateBooking web service request.
     */
    ScenarioBuilder scnBookingPerfTest = scenario("CreateBookingWebService Performance Test")
            .exec(executeAuthWebService)
            .pause(3) // Add a pause to get secureToken
            .exec(testBookingWebService);


    /**
     * Constructor for the BookingPerfTest class.
     * Configures the simulation and defines assertions for test performance.
     */
    public BookingPerfTest() {
        setUp(scnBookingPerfTest.injectOpen(atOnceUsers(1)))
                .protocols(httpConf)
                .assertions(global().successfulRequests().percent().gt(99.0),
                        global().responseTime().percentile(99.0).lt(6000));
    }
}
