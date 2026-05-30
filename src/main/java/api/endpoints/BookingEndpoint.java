package api.endpoints;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import api.base.BaseApi;
import api.models.request.LoginRequest;
import api.models.response.APIOneResponse;
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
    
    /**
     * Creates a new request.
     *
     * @param username name of the user
	 * @param password password of the user 
     * @return Response object
     */
	public Response login(String username, String password) {
		try {
			logger.info("POST {}", LOGIN_ENDPOINT);
			LoginRequest request = new LoginRequest(username, password);
			return given().spec(BaseApi.requestSpec()).body(request).when().post(LOGIN_ENDPOINT);
		} catch (Exception e) {
			logger.error("POST failed: {}", e.getMessage());
			throw new RuntimeException("POST " + LOGIN_ENDPOINT + " failed", e);
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
     * Deserializes the response body into an objects.
     * 
     * @param response raw response from a list endpoint
     * @return deserialized list
     * @throws RuntimeException if the response cannot be mapped to the POJO list
     */
    public List<APIOneResponse> deserializeList(Response response) {
        try {
            return Arrays.asList(response.as(APIOneResponse[].class));
        } catch (Exception e) {
            logger.error("Could not deserialize response to List<APIOneResponse>: {}", e.getMessage());
            throw new RuntimeException(
                    "Deserialization failed — verify the response is an array, not a single object", e);
        }
    }
    /**
     * Safely parses a String value from test data into an int.
     * 
     * @param value     String value to parse (typically from TestData)
     * @param fieldName name of the test data field — used in the error message
     * @return parsed int value
     * @throws RuntimeException if the value cannot be parsed as an integer
     */
    public int parseId(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid test data — '{}' is not a valid integer: '{}'", fieldName, value);
            throw new RuntimeException(
                    "Test data error: field '" + fieldName + "' must be a number, got: '" + value + "'", e);
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
