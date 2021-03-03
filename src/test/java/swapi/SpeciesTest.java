package swapi;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class SpeciesTest extends BaseTest {

    private static Stream<Arguments> readSpeciesNameData() {
        return Stream.of(
                Arguments.of("Droid"),
                Arguments.of("Wookie"),
                Arguments.of("Hutt"));
    }

    @DisplayName("Read all species")
    @Test
    public void getAllSpecies() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + SPECIES)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("count")).isEqualTo("37");
    }

    @DisplayName("Read specie with id = 3")
    @Test
    public void getSpecieWithId() {

        Response response = given()
                .spec(reqSpec)
                .pathParam("specieId", 3)
                .when()
                .get(BASE_URL + "/" + SPECIES + "/{specieId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Wookiee");
        assertThat(json.getString("language")).isEqualTo("Shyriiwook");
    }

    @DisplayName("Read species with given name")
    @ParameterizedTest(name = "Name: {0}")
    @MethodSource("readSpeciesNameData")
    public void getSpecieWithName(String name) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("search", name)
                .when()
                .get(BASE_URL + "/" + SPECIES)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(1);
        assertThat(json.getString("results.name")).contains(name);
    }
}
