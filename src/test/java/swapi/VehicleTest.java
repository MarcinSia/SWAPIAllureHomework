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
import static org.assertj.core.api.Assertions.*;

public class VehicleTest extends BaseTest {

    private static Stream<Arguments> readVehicleNameData() {
        return Stream.of(
                Arguments.of("X-34 landspeeder"),
                Arguments.of("TIE/LN starfighter"),
                Arguments.of("Imperial Speeder Bike"));
    }

    @DisplayName("Read all vehicles list")
    @Test
    public void getAllVehiclesList() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + VEHICLES)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("count")).isEqualTo("39");
    }


    @DisplayName("Read vehicle with id = 6")
    @Test
    public void getVehicleWithId() {

        Response response = given()
                .spec(reqSpec)
                .pathParam("vehicleId", 6)
                .when()
                .get(BASE_URL + "/" + VEHICLES + "/" + "{vehicleId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("T-16 skyhopper");
        assertThat(json.getList("films"))
                .hasSize(1)
                .containsExactly("https://swapi.py4e.com/api/films/1/");
    }

    @DisplayName("Read vehicles with given name")
    @ParameterizedTest(name = "Name: {0}")
    @MethodSource("readVehicleNameData")
    public void getVehicleWithName(String name) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("search", name)
                .when()
                .get(BASE_URL + "/" + VEHICLES)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(1);
        assertThat(json.getString("results.name")).contains(name);
    }


}
