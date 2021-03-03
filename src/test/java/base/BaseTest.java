package base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    protected static final String BASE_URL = "https://swapi.py4e.com/api";
    protected static final String PEOPLE = "people";
    protected static final String PLANETS = "planets";
    protected static final String SPECIES = "species";
    protected static final String VEHICLES = "vehicles";
    protected static final String STARSHIPS = "starships";
    protected static final String FILMS = "films";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    @BeforeAll
    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.addFilter(new AllureRestAssured());

        reqSpec = reqBuilder.build();
    }
}
