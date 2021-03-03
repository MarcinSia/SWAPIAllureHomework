package swapi;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class StarshipsTest extends BaseTest {

    private static Stream<Arguments> readStarshipsData() {
        return Stream.of(
                Arguments.of("Death Star"),
                Arguments.of("Calamari Cruiser"),
                Arguments.of("Slave 1"));
    }

    @DisplayName("Read all starships list")
    @Test
    public void getAllStarshipsList() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + STARSHIPS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("count")).isEqualTo("37");
    }


    @DisplayName("Read starship with id = 9")
    @Test
    public void getStarshipWithId() {

        Response response = given()
                .spec(reqSpec)
                .pathParam("starshipId", 9)
                .when()
                .get(BASE_URL + "/" + STARSHIPS + "/" +"{starshipId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Death Star");
        assertThat(json.getString("model")).isEqualTo("DS-1 Orbital Battle Station");
    }

    @DisplayName("Read starships with given name ")
    @ParameterizedTest(name = "Name: {0}")
    @MethodSource("readStarshipsData")
    public void getStarshipWithName(String name) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("search", name)
                .when()
                .get(BASE_URL + "/" + STARSHIPS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(1);
        assertThat(json.getString("results.name")).contains(name);
    }


}
