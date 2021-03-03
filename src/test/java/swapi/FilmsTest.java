package swapi;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Films;
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

public class FilmsTest extends BaseTest {

    private Films film;

    private static Stream<Arguments> readFilmTitleData() {
        return Stream.of(
                Arguments.of("The Phantom Menace"),
                Arguments.of("Attack of the Clones"),
                Arguments.of("Revenge of the Sith"));
    }

    @DisplayName("Read all films")
    @Test
    public void getAllFilms() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + FILMS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("count")).isEqualTo("7");
    }


    @DisplayName("Read film with id = 7")
    @Test
    public void getFilmWithId() {

        Response response = given()
                .spec(reqSpec)
                .pathParam("id",7)
                .when()
                .get(BASE_URL + "/" + FILMS + "/"+"{id}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("title")).isEqualTo("The Force Awakens");
        assertThat(json.getString("director")).isEqualTo("J. J. Abrams");
        assertThat(json.getString("release_date")).isEqualTo("2015-12-11");
        assertThat(json.getList("species")).containsExactly("https://swapi.py4e.com/api/species/1/",
                "https://swapi.py4e.com/api/species/2/",
                "https://swapi.py4e.com/api/species/3/");
    }

    @DisplayName("Read films with given title")
    @ParameterizedTest(name = "Title: {0}")
    @MethodSource("readFilmTitleData")
    public void getFilmWithTitle(String title) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("search", title)
                .when()
                .get(BASE_URL + "/" + FILMS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getInt("count")).isEqualTo(1);
        assertThat(json.getString("results.title")).contains(title);
    }

    @DisplayName("Create film")
    @Test
    public void createFilm(){

        List<String> species = new ArrayList<>();
        species.add("Demon");

        List<String> starships = new ArrayList<>();
        starships.add("Zoom");
        starships.add("Lightship");

        List<String> vehicles = new ArrayList<>();
        vehicles.add("Ferrari Dino");
        vehicles.add("Porsche 356");
        vehicles.add("Elefant");

        List<String> characters = new ArrayList<>();
        characters.add("Brat Pidd");
        characters.add("Tristano Colando");
        characters.add("Ariana Wilde");
        characters.add("Romuald Coward");

        List<String> planets = new ArrayList<>();
        planets.add("Earth");

        film = new Films();
        film.setTitle("Casino Royale");
        film.setEpisode_id(21);
        film.setOpening_crawl("You Know My Name");
        film.setDirector("Martin Campbell");
        film.setProducer("Paul Haggis");
        film.setRelease_date("2006-11-14");
        film.setSpecies(species);
        film.setStarships(starships);
        film.setVehicles(vehicles);
        film.setCharacters(characters);
        film.setPlanets(planets);
        film.setUrl("https://jamesbond.com.pl/casino-royale/");
        film.setCreated("2006-11-14");
        film.setEdited("2006-11-17");

        Response response = given()
                .spec(reqSpec)
                .body(film)
                .when()
                .post(BASE_URL + "/" + FILMS)
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .extract()
                .response();
    }


}
