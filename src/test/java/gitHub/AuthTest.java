package gitHub;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AuthTest {

    private static final String TOKEN = "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";

    @Test
    public void basicAuth() {
        given()
                .auth()
                .preemptive()
                .basic("lukasz-przytocki", "Tuliglowach!1")
                .get("https://api.github.com/user")
                .then()
                .statusCode(200);
    }

    @Test
    public void bearerToken() {
        given()
                .headers("Authorization", "Bearer " + TOKEN)
                .get("https://api.github.com/user")
                .then()
                .statusCode(200);
    }
    @Test
    public void oAuth2() {

        given()
                .auth()
                .oauth2(TOKEN)
                .get("https://api.github.com/user")
                .then()
                .statusCode(200);
    }

}
