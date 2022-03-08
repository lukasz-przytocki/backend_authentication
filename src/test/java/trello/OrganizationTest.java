package trello;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class OrganizationTest {
    protected static final String KEY = "94e8072c1d1178bd66cdcaebe0c14379";
    protected static final String TOKEN = "60cc29227ff4b31bec5a6fd13db5710c2f3309431a9f557e25003b8cb7638926";

    private static Stream<Arguments> createOrganizationData(){
        return Stream.of(
                Arguments.of("This is display name","Description","name","https://www.interia.pl"),
                Arguments.of("This is display name","Description","abc","http://www.interia.pl"),
                Arguments.of("This is display name","Description","name_abc","http://www.interia.pl"),
                Arguments.of("This is display name","Description","name123","http://www.interia.pl"));
    }

    private static Stream<Arguments> createFailedOrganizationData(){
        return Stream.of(
                Arguments.of("This is display name","Description","Name","https://www.interia.pl"),
                Arguments.of("This is display name","Description","a:bc","http://www.interia.pl"),
                Arguments.of("This is display name","Description","","http://www.interia.pl"),
                Arguments.of("This is display name","Description","name123","http://www.interia.pl"),
                Arguments.of("This is display name","Description","name123","http://www.interia.pl"));
    }

    @DisplayName("Create organization with valida data")
    @ParameterizedTest(name="Display name: {0}, desc {1}, name {2}, website {3}")
    @MethodSource("createOrganizationData")
    public void cresteOrganization(String displayName,String desc, String name, String webSite){
//        Organization organization = new Organization();
//        organization.setDisplayName(displayName);
//        organization.setDesc(desc);
//        organization.setName(name);
//        organization.setWebsite(webSite);

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", webSite)
                .when()
                .post("https://api.trello.com/1/organizations")
                .then()
                .statusCode(200)
                .extract().response();

        JsonPath path = response.jsonPath();
        Assertions.assertThat(path.getString("website")).isEqualTo(webSite);

        String organizationId = path.getString("id");

        given()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .when()
                .delete("https://api.trello.com/1/organizations"+"/"+organizationId)
                .then().statusCode(200);
    }

    @DisplayName("Create organization with valida data")
    @ParameterizedTest(name="Display name: {0}, desc {1}, name {2}, website {3}")
    @MethodSource("createFailedOrganizationData")
    public void cresteOrganizationWithFailedData(String displayName,String desc, String name, String webSite){

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", webSite)
                .when()
                .post("https://api.trello.com/1/organizations")
                .then()
                .statusCode(400)
                .extract().response();

        JsonPath path = response.jsonPath();
        Assertions.assertThat(path.getString("website")).isEqualTo(webSite);

        String organizationId = path.getString("id");

        given()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .when()
                .delete("https://api.trello.com/1/organizations"+"/"+organizationId)
                .then().statusCode(200);
    }
}
