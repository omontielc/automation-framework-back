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
import io.restassured.response.Response;
import util.PropertiesHandle;


/**
 * Encapsulates all HTTP operations for the endpoint.
 * @author Osiris Montiel Campos
 * @version 2025-10-06
 */
public class BookingEndpoint {

    private static final Logger logger = LogManager.getLogger(BookingEndpoint.class);
    
    private static final Properties prop = new PropertiesHandle("API").getProperty();
    private static final String LOGIN_ENDPOINT = prop.getProperty("API.login.endPoint");
    private static final String BOOKING_ENDPOINT = prop.getProperty("API.booking.endPoint");
    /**
     * Creates a login token.
     *
     * @param username name of the user
	 * @param password password of the user 
     * @return Response object
     */
	public Response login(String username, String password) {
		try {
			logger.info("EndPoint {}", LOGIN_ENDPOINT);
			LoginRequest request = new LoginRequest(username, password);
			return given().spec(BaseApi.requestSpec()).body(request).when().post(LOGIN_ENDPOINT);
		} catch (Exception e) {
			logger.error("EndPoint failed: {}", e.getMessage());
			throw new RuntimeException("EndPoint " + LOGIN_ENDPOINT + " failed", e);
		}
	}
	
	/**
	 * Creates a new booking.
	 *
	 * @param firstname name of the user
	 * @param lastname last name of the user
	 * @param totalprice total price of the booking
	 * @param depositpaid whether the deposit has been paid
	 * @param checkin check-in date
	 * @param checkout check-out date
	 * @param additionalneeds additional needs of the booking
	 * @return Response object
	 */
	public Response createBooking(String firstname, String lastname, int totalprice, boolean depositpaid, String checkin, String checkout, String additionalneeds) {
		try {
			logger.info("EndPoint {}", BOOKING_ENDPOINT);
			BookingRequest request = new BookingRequest(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);
			return given().spec(BaseApi.requestSpec()).body(request).when().post(BOOKING_ENDPOINT);
		} catch (Exception e) {
			logger.error("EndPoint failed: {}", e.getMessage());
			throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
		}
	}
	/**
	 * Get a booking by its Name.
	 *
	 * @param firstname name of the user
	 * @return Response object
	 */
	public Response getBookingByName(String firstname) {
		try {
			logger.info("EndPoint {}", BOOKING_ENDPOINT);
			return given()
			        .spec(BaseApi.requestSpec())
			        .queryParam("firstname", firstname)  // ← así se agregan query params
			        .when()
			        .get(BOOKING_ENDPOINT);
		} catch (Exception e) {
			logger.error("EndPoint failed: {}", e.getMessage());
			throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
		}
	}
	/**
	 * Update a booking.
	 *
	 * @param id id of the booking
	 * @param token session token
	 * @param firstname name of the user
	 * @param lastname last name of the user
	 * @param totalprice total price of the booking
	 * @param depositpaid whether the deposit has been paid
	 * @param checkin check-in date
	 * @param checkout check-out date
	 * @param additionalneeds additional needs of the booking
	 * @return Response object
	 */
	public Response updateBooking(Integer id, String token,String firstname, String lastname, int totalprice, boolean depositpaid, String checkin, String checkout, String additionalneeds) {
		try {
			logger.info("EndPoint {}", BOOKING_ENDPOINT);
			BookingRequest request = new BookingRequest(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);
			return given()
					.spec(BaseApi.requestSpec())
					.header("Cookie", "token=" + token)
					.body(request)
					.when()
					.put(BOOKING_ENDPOINT+ "/"+ id);
		} catch (Exception e) {
			logger.error("EndPoint failed: {}", e.getMessage());
			throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
		}
	}
	
	/**
	 * Get a booking by its Name.
	 *
	 * @param firstname name of the user
	 * @return Response object
	 */
	public Response getBookingById(int id) {
		try {
			logger.info("EndPoint {}", BOOKING_ENDPOINT);
			return given()
			        .spec(BaseApi.requestSpec())  // ← así se agregan query params
			        .when()
			        .get(BOOKING_ENDPOINT+"/"+id);
		} catch (Exception e) {
			logger.error("EndPoint failed: {}", e.getMessage());
			throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
		}
	}
	
	/**
	 * Get a booking by its Name.
	 * 
	 * @param id booking
	 * @param token session token
	 * @return Response object
	 */
	public Response deleteBookingById(Integer id, String token) {
		try {
			logger.info("EndPoint {}", BOOKING_ENDPOINT);
			return given()
			        .spec(BaseApi.requestSpec())
					.header("Cookie", "token=" + token)
			        .when()
			        .delete(BOOKING_ENDPOINT+"/"+id);
		} catch (Exception e) {
			logger.error("EndPoint failed: {}", e.getMessage());
			throw new RuntimeException("EndPoint " + BOOKING_ENDPOINT + " failed", e);
		}
	}
    
	//General Accions
	
    /**
     * Deserializes the response body into a single object.
     * 
     * @param response raw response from a single-object endpoint
     * @return deserialized 
     * @throws RuntimeException if the response cannot be mapped to the POJO
     */
    public LoginResponse deserializeLogin(Response response) {
        try {
            return response.as(LoginResponse.class);
        } catch (Exception e) {
            logger.error("Could not deserialize response to LoginResponse: {}", e.getMessage());
            throw new RuntimeException(
                    "Deserialization failed — verify the response is a single object, not an array", e);
        }
    }
    
    /**
     * Deserializes the response body into a single object.
     * 
     * @param response raw response from a single-object endpoint
     * @return deserialized 
     * @throws RuntimeException if the response cannot be mapped to the POJO
     */
    public 	BookingResponse deserializeBooking(Response response) {
        try {
            return response.as(BookingResponse.class);
        } catch (Exception e) {
            logger.error("Could not deserialize response to BookingResponse: {}", e.getMessage());
            throw new RuntimeException(
                    "Deserialization failed — verify the response is a single object, not an array", e);
        }
    }
    /**
     * Validates that the response body matches the expected JSON Schema.
     *	
     * @param response   raw response to validate
     * @param schemaPath path to the schema file relative to src/test/resources
     * @throws RuntimeException if the schema file is not found
     * @throws AssertionError   if the response does not match the schema
     */
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
}
