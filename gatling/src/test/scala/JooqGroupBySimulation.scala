import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class JooqGroupBySimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8090")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .header("Keep-Alive", "150")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")


  val scn = scenario("GroupBy")
    .repeat(200) {
      exec(http("authors")
        .get("/ws/authors/groupBy").check(status.is(session => 200)))
    }


  setUp(scn.inject(
    rampUsers(1000) over(10 seconds)
  ).protocols(httpConf)).maxDuration(120 seconds)


}