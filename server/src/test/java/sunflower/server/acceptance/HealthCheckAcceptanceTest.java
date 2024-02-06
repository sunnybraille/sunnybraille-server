package sunflower.server.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sunflower.server.acceptance.common.AcceptanceTest;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckAcceptanceTest extends AcceptanceTest {

    @Test
    void 서버_헬스_체크() {
        final ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .get("/health")

                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
