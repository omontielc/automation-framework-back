package api.endpoints;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import api.base.BaseApi;
import api.models.request.APIOneRequest;
import api.models.response.APIOneResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import util.PropertiesHandle;


/**
 * Encapsulates all HTTP operations for the endpoint.
 * @author Osiris Montiel Campos
 * @version 2025-10-06
 */
public class APIOneEndpoint {

    private static final Logger logger = LogManager.getLogger(APIOneEndpoint.class);
    private static final Properties prop = new PropertiesHandle("API").getProperty();
    private static final String ENDPOINT = prop.getProperty("API.apiOne.endPoint");
    
    private final String baseUrl;
    
    public APIOneEndpoint(String baseUrl) {
		this.baseUrl = baseUrl;
	}

    //metodo para crear un post
	public Response createe(APIOneRequest request) {
		try {
			logger.info("POST {}", ENDPOINT);
			return given().spec(BaseApi.requestSpec(baseUrl)).body(request).when().post(ENDPOINT);
		} catch (Exception e) {
			logger.error("POST failed: {}", e.getMessage());
			throw new RuntimeException("POST " + ENDPOINT + " failed", e);
		}
	}

    /**
     * Retrieves all posts.
     *
     * @return Response object
     */
    @Step("GET all posts")
    public Response getAll() {
    	try {
            logger.info("GET {}", ENDPOINT);
            return given().spec(BaseApi.requestSpec(baseUrl)).when().get(ENDPOINT);
        } catch (Exception e) {
            logger.error("GET all posts failed: {}", e.getMessage());
            throw new RuntimeException("GET " + ENDPOINT + " failed", e);
        }
    }

    /**
     * Retrieves a single post by its ID.
     *
     * @param id post identifier
     * @return Response object
     */
    @Step("GET post by id: {id}")
    public Response getById(int id) {
    	if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than 0 — received: " + id);
        }
        try {
            logger.info("GET {}/{}", ENDPOINT, id);
            return given().spec(BaseApi.requestSpec(baseUrl)).when().get(ENDPOINT + "/" + id);
        } catch (Exception e) {
            logger.error("GET by id failed for id {}: {}", id, e.getMessage());
            throw new RuntimeException("GET " + ENDPOINT + "/" + id + " failed", e);
        }
    }

    /**
     * Retrieves all posts belonging to a specific user.
     *
     * @param userId user identifier to filter by
     * @return Response object
     */
    @Step("GET posts by userId: {userId}")
    public Response getByUserId(int userId) {
    	if (userId <= 0) {
            throw new IllegalArgumentException(
                    "userId must be greater than 0 — received: " + userId);
        }
        try {
            logger.info("GET {}?userId={}", ENDPOINT, userId);
            return given().spec(BaseApi.requestSpec(baseUrl)).queryParam("userId", userId).when().get(ENDPOINT);
        } catch (Exception e) {
            logger.error("GET by userId failed for userId {}: {}", userId, e.getMessage());
            throw new RuntimeException("GET " + ENDPOINT + "?userId=" + userId + " failed", e);
        }
    }

    /**
     * Creates a new post.
     *
     * @param postRequest POJO that Jackson serializes to JSON automatically
     */
    @Step("POST create post — title: {postRequest.title}")
    public Response create(APIOneRequest postRequest) {
    	if (postRequest == null) {
            throw new IllegalArgumentException("Request body must not be null");
        }
        try {
            logger.info("POST {} — body: {}", ENDPOINT, postRequest.getTitle());
            return given().spec(BaseApi.requestSpec(baseUrl)).body(postRequest).when().post(ENDPOINT);
        } catch (Exception e) {
            logger.error("POST failed — title: {}: {}", postRequest.getTitle(), e.getMessage());
            throw new RuntimeException("POST " + ENDPOINT + " failed", e);
        }
    }

    /**
     * Fully updates an existing post (replaces all fields).
     *
     * @param id          ID of the post to update
     * @param postRequest updated post data
     * @return Response object
     */
    @Step("PUT update post id: {id}")
    public Response update(int id, APIOneRequest postRequest) {
    	if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than 0 — received: " + id);
        }
        if (postRequest == null) {
            throw new IllegalArgumentException("Request body must not be null");
        }
        try {
            logger.info("PUT {}/{}", ENDPOINT, id);
            return given().spec(BaseApi.requestSpec(baseUrl)).body(postRequest).when().put(ENDPOINT + "/" + id);
        } catch (Exception e) {
            logger.error("PUT failed for id {}: {}", id, e.getMessage());
            throw new RuntimeException("PUT " + ENDPOINT + "/" + id + " failed", e);
        }
    }

    /**
     * Deletes a post by its ID.
     *
     * @param id ID of the post to delete
     * @return Response object
     */
    @Step("DELETE post id: {id}")
    public Response delete(int id) {
    	if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than 0 — received: " + id);
        }
        try {
            logger.info("DELETE {}/{}", ENDPOINT, id);
            return given().spec(BaseApi.requestSpec(baseUrl)).when().delete(ENDPOINT + "/" + id);
        } catch (Exception e) {
            logger.error("DELETE failed for id {}: {}", id, e.getMessage());
            throw new RuntimeException("DELETE " + ENDPOINT + "/" + id + " failed", e);
        }
    }
    /**
     * Deserializes the response body into a single object.
     *
     * @param response raw response from a single-object endpoint
     * @return deserialized
     * @throws RuntimeException if the response cannot be mapped to the POJO
     */
    public APIOneResponse deserialize(Response response) {
        try {
            return response.as(APIOneResponse.class);
        } catch (Exception e) {
            logger.error("Could not deserialize response to APIOneResponse: {}", e.getMessage());
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
