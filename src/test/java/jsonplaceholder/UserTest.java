package jsonplaceholder;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jsonpaceholder.Address;
import jsonpaceholder.Company;
import jsonpaceholder.Geo;
import jsonpaceholder.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserTest {

    @Test
    public void createNewUser() {
        User user = new User();
        user.setName("Lukasz Prz");
        user.setUsername("Lukasz");
        user.setEmail("lukaszP@interia.pl");
        user.setPhone("2423424234");
        user.setWebsite("www.interia.pl");

        Geo geo = new Geo();
        geo.setLan("-24.45");
        geo.setLat("56.34");

        Address address = new Address();
        address.setCity("Wroclaw");
        address.setSuite("Apt 2");
        address.setZipcode("23-345");
        address.setGeo(geo);

        user.setAddress(address);

        Company company = new Company();
        company.setName("Alibaba");
        company.setCatchPhrase("Multi layer cross fir");
        company.setBs("Harness");

        user.setCompany(company);

        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("https://jsonplaceholder.typicode.com/posts")
                .then()
                .statusCode(201)
                .extract()
                .response();

        JsonPath path = response.jsonPath();
        path.prettyPrint();
        Assertions.assertThat(path.getString("username")).isEqualTo(user.getUsername());

    }
}
