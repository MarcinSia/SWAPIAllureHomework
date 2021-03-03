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

public class PlanetsTest extends BaseTest {

    private static Stream<Arguments> readPlanetNameData() {
        return Stream.of(
                Arguments.of("Tatooine"),
                Arguments.of("Alderaan"),
                Arguments.of("Naboo"));
    }

    @DisplayName("Read all planets")
    @Test
    public void getAllPlanets() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + PLANETS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("count")).isEqualTo("61");
    }

    @DisplayName("Read planet with id = 13")
    @Test
    public void getPlanetWithId() {

        Response response = given()
                .spec(reqSpec)
                .pathParam("planetId",13)
                .when()
                .get(BASE_URL + "/" + PLANETS + "/"+ "{planetId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Mustafar");
        assertThat(json.getString("climate")).isEqualTo("hot");
        assertThat(json.getString("terrain")).isEqualTo("volcanoes, lava rivers, mountains, caves");
    }

    @DisplayName("Read planets with given name")
    @ParameterizedTest(name = "Name: {0}")
    @MethodSource("readPlanetNameData")
    public void getPlanetWithName(String name) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("search", name)
                .when()
                .get(BASE_URL + "/" + PLANETS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(1);
        assertThat(json.getString("results.name")).contains(name);
    }


}
