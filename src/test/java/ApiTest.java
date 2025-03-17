import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    @Test
    public void testGetAllObjects() {

        RestAssured.baseURI = "https://api.restful-api.dev";

        Response response =
                given()
                        .header("Accept", "application/json")
                        .when()
                        .get("/objects")
                        .then()
                        .statusCode(200)
                        .body("size()", greaterThan(0))
                        .body("[0].id", notNullValue())
                        .body("[0].name", notNullValue())
                        .extract().response();


        System.out.println(response.prettyPrint());


        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("$").size() > 0);
    }

    @Test
    public void testGetObjectsById() {

        RestAssured.baseURI = "https://api.restful-api.dev";


        Response response =
                given()
                        .header("Accept", "application/json")
                        .queryParam("id", "3")
                        .queryParam("id", "5")
                        .queryParam("id", "10")
                        .when()
                        .get("/objects")
                        .then()
                        .statusCode(200)
                        .body("size()", equalTo(3))
                        .body("id", hasItems("3", "5", "10"))
                        .body("[0].name", notNullValue())
                        .body("[0].data", notNullValue())
                        .extract().response();


        System.out.println(response.prettyPrint());


        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(3, response.jsonPath().getList("$").size());
    }

    @Test
    public void testGetSingleObject() {

        RestAssured.baseURI = "https://api.restful-api.dev";


        Response response =
                given()
                        .header("Accept", "application/json")
                        .when()
                        .get("/objects/7")
                        .then()
                        .statusCode(200)
                        .body("id", equalTo("7"))
                        .body("name", equalTo("Apple MacBook Pro 16"))
                        .body("data.year", equalTo(2019))
                        .body("data.price", equalTo(1849.99f))
                        .body("data['CPU model']", equalTo("Intel Core i9"))
                        .body("data['Hard disk size']", equalTo("1 TB"))
                        .extract().response();


        System.out.println(response.prettyPrint());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Apple MacBook Pro 16", response.jsonPath().getString("name"));
        Assertions.assertEquals(2019, response.jsonPath().getInt("data.year"));
    }

    @Test
    public void testAddObject() {
        // Set the base URI for Rest-Assured
        RestAssured.baseURI = "https://api.restful-api.dev";

        // Define the request body
        String requestBody = "{\n" +
                "   \"name\": \"Apple MacBook Pro 16\",\n" +
                "   \"data\": {\n" +
                "      \"year\": 2019,\n" +
                "      \"price\": 1849.99,\n" +
                "      \"CPU model\": \"Intel Core i9\",\n" +
                "      \"Hard disk size\": \"1 TB\"\n" +
                "   }\n" +
                "}";


        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/objects")
                .then()
                .statusCode(200)
                .body("name", equalTo("Apple MacBook Pro 16"))
                .body("data.year", equalTo(2019))
                .body("data.price", equalTo(1849.99f))
                .body("data['CPU model']", equalTo("Intel Core i9"))
                .body("data['Hard disk size']", equalTo("1 TB"))
                .body("createdAt", notNullValue())
                .body("id", notNullValue());
    }

    @Test
    public void updateObject() {

        RestAssured.baseURI = "https://api.restful-api.dev";


        String requestBody = "{\n" +
                "   \"name\": \"Apple MacBook Pro 16\",\n" +
                "   \"data\": {\n" +
                "      \"year\": 2019,\n" +
                "      \"price\": 2049.99,\n" +
                "      \"CPU model\": \"Intel Core i9\",\n" +
                "      \"Hard disk size\": \"1 TB\",\n" +
                "      \"color\": \"silver\"\n" +
                "   }\n" +
                "}";


        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/objects/ff808181932badb60195a2ea0bc3641e")
                .then()
                .statusCode(200)
                .body("id", equalTo("ff808181932badb60195a2ea0bc3641e"))
                .body("name", equalTo("Apple MacBook Pro 16"))
                .body("data.year", equalTo(2019))
                .body("data.price", equalTo(2049.99f))
                .body("data.color", equalTo("silver"))
                .body("updatedAt", notNullValue());
    }


    @Test
    public void partialUpdateObject() {

        RestAssured.baseURI = "https://api.restful-api.dev";

        String requestBody = "{\n" +
                "   \"name\": \"Apple MacBook Pro 16 (Updated Name)\",\n" +
                "   \"data\": {\n" +
                "      \"price\": 1849.99,\n" +
                "      \"year\": 2019,\n" +
                "      \"CPU model\": \"Intel Core i9\",\n" +
                "      \"Hard disk size\": \"1 TB\"\n" +
                "   }\n" +
                "}";


        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/objects/ff808181932badb60195a32fe92364b6")
                .then()
                .statusCode(200)
                .body("id", equalTo("ff808181932badb60195a32fe92364b6"))
                .body("name", equalTo("Apple MacBook Pro 16 (Updated Name)"))
                .body("data.year", equalTo(2019))
                .body("data.price", equalTo(1849.99f))
                .body("data.'CPU model'", equalTo("Intel Core i9"))
                .body("data.'Hard disk size'", equalTo("1 TB"))
                .body("updatedAt", notNullValue());
    }


    @Test
    public void deleteObject() {

        RestAssured.baseURI = "https://api.restful-api.dev";


        String responseBody = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/objects/6")
                .then()
                .statusCode(405)
                .extract().response().asString();


        System.out.println("Response Body: " + responseBody);

    }
}