package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PlaceholderPerfTest extends Simulation {

    private final String domain = System.getProperty("domain", "www.placeholder.com");

    HttpProtocolBuilder httpConf = http
            .baseUrl("https://" + domain + "/")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .acceptEncodingHeader("gzip")
            .warmUp("https://" + domain + "/")
            .check(status().is(200));

    /**
     * Dummy Performance Test with placeholder-site
     */
    HttpRequestActionBuilder testServiceHealthCheck = http("Service Health Check")
            .get("/health")
            .check(status().is(200));

    ScenarioBuilder scnGpcPerfTest = scenario("Dummy Service Health Check")
            .exec(testServiceHealthCheck);

    public PlaceholderPerfTest() {
        setUp(scnGpcPerfTest.injectClosed(constantConcurrentUsers(5).during(Duration.ofSeconds(5))))
                .protocols(httpConf)
                .assertions(global().successfulRequests().percent().gt(99.0),
                        global().responseTime().percentile(99.0).lt(6000));
    }
}
