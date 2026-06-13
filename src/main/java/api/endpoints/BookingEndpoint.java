package api.endpoints;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import api.base.BaseApi;
import api.models.request.BookingRequest;
import api.models.request.LoginRequest;
import api.models.response.BookingResponse;
import api.models.response.LoginResponse;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import util.PropertiesHandle;

/**
 * Encapsulates all HTTP operations for the Booking endpoint.
 * @author Osiris Montiel Campos
 * @version 2025-11-12
 */
public class BookingEndpoint {

    private static final Logger logger = LogManager.getLogger(BookingEndpoint.class);

    private static final Properties prop = new PropertiesHandle("API").getProperty();
    private static final String LOGIN_ENDPOINT    = prop.getProperty("API.login.endPoint");
    private static final String BOOKING_ENDPOINT  = prop.getProperty("API.booking.endPoint");
    
    private final String baseUrl;
    
    public BookingEndpoint(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // =========================================================
    // HTTP Operations
    // =========================================================

    @Step("POST {LOGIN_ENDPOINT} — login with user: {username}")
    public Response login(String username, String password) {
        try {
            logger.info("EndPoint {}", LOGIN_ENDPOINT);
            LoginRequest request = new LoginRequest(username, password);
            Response response = given().spec(BaseApi.requestSpec(baseUrl)).body(request).when().post(LOGIN_ENDPOINT);
            attachResponse("Login Response", response);
            attachHeaders(response);
            return response;
        } catch (Exception e) {
            logger.error("EndPoint failed: {}", e.getMessage());
            throw new RuntimeException("EndPoint " + LOGIN_ENDPOINT + " failed", e);
        }
    }

    @Step("POST {BOOKING_ENDPOINT} — create booking for {firstname} {lastname}")
    public Response createBooking(String firstname, String lastname, int totalprice,
                                   boolean depositpaid, String checkin, String checkout,
                                   String additionalneeds) {
        try {
            logger.info("EndPoint {}", BOOKING_ENDPOINT);
            BookingRequest request = new BookingRequest(firstname, lastname, totalprice,
                    depositpaid, checkin, checkout, additionalneeds);
            Response response = given().spec(BaseApi.requestSpec(baseUrl)).body(request).when().post(BOOKING_ENDPOINT);
            attachResponse("Create Booking Response", response);
            attachHeaders(response);
            return response;
        } catch (Exception e) {
            logger.error("EndPoint failed: {}", e.getMessage());
            throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
        }
    }

    @Step("GET {BOOKING_ENDPOINT}?firstname={firstname}")
    public Response getBookingByName(String firstname) {
        try {
            logger.info("EndPoint {}", BOOKING_ENDPOINT);
            Response response = given()
                    .spec(BaseApi.requestSpec(baseUrl))
                    .queryParam("firstname", firstname)
                    .when()
                    .get(BOOKING_ENDPOINT);
            attachResponse("Get Booking By Name Response", response);
            attachHeaders(response);
            return response;
        } catch (Exception e) {
            logger.error("EndPoint failed: {}", e.getMessage());
            throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
        }
    }

    @Step("GET {BOOKING_ENDPOINT}/{id}")
    public Response getBookingById(int id) {
        try {
            logger.info("EndPoint {}", BOOKING_ENDPOINT);
            Response response = given()
                    .spec(BaseApi.requestSpec(baseUrl))
                    .when()
                    .get(BOOKING_ENDPOINT + "/" + id);
            attachResponse("Get Booking By ID Response", response);
            attachHeaders(response);
            return response;
        } catch (Exception e) {
            logger.error("EndPoint failed: {}", e.getMessage());
            throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
        }
    }

    @Step("PUT {BOOKING_ENDPOINT}/{id} — update booking for {firstname} {lastname}")
    public Response updateBooking(Integer id, String token, String firstname, String lastname,
                                   int totalprice, boolean depositpaid, String checkin,
                                   String checkout, String additionalneeds) {
        try {
            logger.info("EndPoint {}", BOOKING_ENDPOINT);
            BookingRequest request = new BookingRequest(firstname, lastname, totalprice,
                    depositpaid, checkin, checkout, additionalneeds);
            Response response = given()
                    .spec(BaseApi.requestSpec(baseUrl))
                    .header("Cookie", "token=" + token)
                    .body(request)
                    .when()
                    .put(BOOKING_ENDPOINT + "/" + id);
            attachResponse("Update Booking Response", response);
            attachHeaders(response);
            return response;
        } catch (Exception e) {
            logger.error("EndPoint failed: {}", e.getMessage());
            throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
        }
    }

    @Step("DELETE {BOOKING_ENDPOINT}/{id}")
    public Response deleteBookingById(Integer id, String token) {
        try {
            logger.info("EndPoint {}", BOOKING_ENDPOINT);
            Response response = given()
                    .spec(BaseApi.requestSpec(baseUrl))
                    .header("Cookie", "token=" + token)
                    .when()
                    .delete(BOOKING_ENDPOINT + "/" + id);
            attachResponse("Delete Booking Response", response);
            attachHeaders(response);
            return response;
        } catch (Exception e) {
            logger.error("EndPoint failed: {}", e.getMessage());
            throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
        }
    }

    // =========================================================
    // Deserializers
    // =========================================================

    @Step("Deserialize response to LoginResponse")
    public LoginResponse deserializeLogin(Response response) {
        try {
            return response.as(LoginResponse.class);
        } catch (Exception e) {
            logger.error("Could not deserialize response to LoginResponse: {}", e.getMessage());
            throw new RuntimeException(
                    "Deserialization failed — verify the response is a single object, not an array", e);
        }
    }

    @Step("Deserialize response to BookingResponse")
    public BookingResponse deserializeBooking(Response response) {
        try {
            return response.as(BookingResponse.class);
        } catch (Exception e) {
            logger.error("Could not deserialize response to BookingResponse: {}", e.getMessage());
            throw new RuntimeException(
                    "Deserialization failed — verify the response is a single object, not an array", e);
        }
    }

    // =========================================================
    // Schema Validation
    // =========================================================

    @Step("Validate response matches JSON schema: {schemaPath}")
    public void validateSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            logger.info("Schema validation PASSED — schema: {}", schemaPath);
        } catch (AssertionError e) {
            logger.error("Schema validation FAILED — schema: {}: {}", schemaPath, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Schema file not found or invalid — {}: {}", schemaPath, e.getMessage());
            throw new RuntimeException("Schema validation error — check that the file exists: " + schemaPath, e);
        }
    }

    // =========================================================
    // Attachments
    // =========================================================

    @Attachment(value = "{label}", type = "application/json")
    private String attachResponse(String label, Response response) {
        try {
            int statusCode = response.getStatusCode();
            String body = response.getBody().asPrettyString();

            // Incluye el status code en el attachment para más contexto
            return "Status Code: " + statusCode + "\n\n" + body;
        } catch (Exception e) {
            return "Could not attach response body: " + e.getMessage();
        }
    }

    @Attachment(value = "Request Headers", type = "text/plain")
    private String attachHeaders(Response response) {
        try {
            return response.getHeaders().toString();
        } catch (Exception e) {
            return "Could not attach headers: " + e.getMessage();
        }
    }
}