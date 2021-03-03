package swapi;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.People;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PeopleTest extends BaseTest {

    private People person;

    private static Stream<Arguments> readPersonNameData() {
        return Stream.of(
                Arguments.of("Mace Windu"),
                Arguments.of("Yoda"),
                Arguments.of("Obi-Wan Kenobi"));
    }

    @DisplayName("Read all people list")
    @Test
    public void getPeople() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + PEOPLE)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("count")).isEqualTo("87");
    }

    @DisplayName("Read person with id = 1")
    @Test
    public void getPerson() {

        Response response = given()
                .spec(reqSpec)
                .pathParam("personId",1)
                .when()
                .get(BASE_URL + "/" + PEOPLE + "/" + "{personId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Luke Skywalker");
        List<String> vehicles = json.getList("vehicles");
        assertThat(vehicles).hasSize(2);
    }

    @DisplayName("Read person with given name")
    @ParameterizedTest(name = "Name: {0}")
    @MethodSource("readPersonNameData")
    public void getPersonWithName(String name) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("search", name)
                .when()
                .get(BASE_URL + "/" + PEOPLE)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(1);
        assertThat(json.getString("results.name")).contains(name);
    }

    @DisplayName("Create person")
    @Test
    public void createPerson() {

        List<String> films = new ArrayList<>();
        films.add("Scary Movie 1");
        films.add("Scary Movie 2");

        List<String> species = new ArrayList<>();
        species.add("Humanoid");

        List<String> starships = new ArrayList<>();
        starships.add("Boeing");
        starships.add("Kamikaze");

        List<String> vehicles = new ArrayList<>();
        vehicles.add("Ferrari Dino");
        vehicles.add("Porsche 356");

        person = new People();
        person.setName("Johnny English");
        person.setBirth_year("1968");
        person.setEye_color("blue");
        person.setGender("male");
        person.setHair_color("brow");
        person.setHeight("178");
        person.setMass("78");
        person.setSkin_color("pale");
        person.setHomeworld("https://swapi.py4e.com/api/planets/2/");
        person.setFilms(films);
        person.setSpecies(species);
        person.setStarships(starships);
        person.setVehicles(vehicles);
        person.setUrl("https://johnnyenglish.com.pl/");
        person.setCreated("2012-10-23T01:01:01.644000Z");
        person.setEdited("2012-10-26T01:01:01.644000Z");

        Response response = given()
                .spec(reqSpec)
                .body(person)
                .when()
                .post(BASE_URL + "/" + PEOPLE)
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .extract()
                .response();
    }



}
