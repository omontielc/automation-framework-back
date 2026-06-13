package test.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import api.endpoints.APIOneEndpoint;
import api.models.request.APIOneRequest;
import api.models.response.APIOneResponse;
import config.Config;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import util.EnvironmentWriter;
import util.TestBase;

/**
 * @author Osiris Montiel Campos
 * @version 2025-10-06
 */
@Epic("Automation Framework API")
@Feature("API Endpoint")
public class ATC01_Test1 extends TestBase {

    private APIOneEndpoint apiOne;

    @BeforeSuite
    public void setupSuite() {
        super.setupSuite(Config.APIONE_URL_BASE);
	    this.apiOne = new APIOneEndpoint(this.baseUrl);
	    EnvironmentWriter.write(Config.BOOKING_URL_BASE, "QA");
        logger.info("ATC01 suite initialized");
    }

//    @Test
//    @Story("Retrieve all apiOne")
//    @Description("GET /posts should return 200 and a non-empty list of 100")
//    @Severity(SeverityLevel.CRITICAL)
//    public void getAllapiOne() {
//     	Response response = apiOne.getAll();
//
//     	List<APIOneResponse> posts = apiOne.deserializeList(response);
//
//        assertFalse(posts.isEmpty(), "List should not be empty");
//
//        logger.info("Retrieved post: {}", posts);
//
//   }

    @Test
    @Story("Retrieve a single post")
    @Description("GET /posts/1 should return the correct post with all required fields")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldGetPostById() {
    	int id = Integer.parseInt(testData.getData("Id", 1));
        Response response = apiOne.getById(id);

        APIOneResponse post = apiOne.deserialize(response);
        assertEquals(post.getId(),   id  ,  "Post ID should be 1");
        assertNotNull(post.getTitle(),     "Title should not be null");

        logger.info("Retrieved post: {}", post);
    }

    @Test
    @Story("Filter apiOne by user")
    @Description("GET /posts?userId=1 should return only apiOne belonging to user 1")
    @Severity(SeverityLevel.NORMAL)
    public void shouldGetapiOneByUserId() {
    	int userId = apiOne.parseId(testData.getData("UserId", 1), "UserId");
    	Response response = apiOne.getByUserId(userId);

    	List<APIOneResponse> posts = apiOne.deserializeList(response);

        assertFalse(posts.isEmpty(), "List should not be empty");
        posts.forEach(post -> assertEquals(post.getUserId(), Integer.parseInt(testData.getData("UserId", 1)), "All posts should belong to userId 1"));

        logger.info("Retrieved post: {}", posts);
    }

    @Test
    @Story("Handle non-existent resource")
    @Description("GET /posts/9999 should return 404 for a post that does not exist")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturn404ForNonExistentPost() {
    	int id = apiOne.parseId(testData.getData("Id_2", 1), "Id_2");
    	Response response = apiOne.getById(id);

        assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent resource");
        assertNotNull(response.getBody(), "Response body should not be null");

        logger.info("Post error: {}",  response.getStatusCode());

    }

    @Test
    @Story("Create a new post")
    @Description("POST /posts should create a resource and return 201 with the created data")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldCreatePost() {
    	int userId = apiOne.parseId(testData.getData("UserId", 1), "UserId");
    	APIOneRequest request = new APIOneRequest("My first automated post", "This is the post body", userId);

        Response response = apiOne.create(request);
        APIOneResponse post = apiOne.deserialize(response);
        assertEquals(response.getStatusCode(), 201, "Should return 201 Created");

        logger.info("Post created with id: {}",  post.getId());
    }

    @Test
    @Story("Update an existing post")
    @Description("PUT /post/1 should fully replace the post and return 200 with updated data")
    @Severity(SeverityLevel.NORMAL)
    public void shouldUpdatePost() {
    	int id = Integer.parseInt(testData.getData("Id", 1));
        APIOneRequest updated = new APIOneRequest("Updated title", "Updated body", id);

        Response response = apiOne.update(id, updated);

        APIOneResponse post = apiOne.deserialize(response);
        assertEquals(post.getId(),   id  ,  "Post ID should be 1");
        assertNotNull(post.getTitle(),     "Title should not be null");

        logger.info("Updated post: {}", post);

    }

    @Test
    @Story("Delete an existing post")
    @Description("DELETE /post/1 should return 200 and an empty response body")
    @Severity(SeverityLevel.NORMAL)
    public void shouldDeletePost() {
    	int id = apiOne.parseId(testData.getData("Id", 1), "Id");
    	Response response = apiOne.delete(id);

        assertEquals(response.getStatusCode(), 200, "Should be deleted");
        logger.info("Deleted post: {}", id);
    }

    @Test
    @Story("Validate post response schema")
    @Description("GET /posts/1 response must match the defined JSON Schema")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldMatchPostSchema() {
        int id = apiOne.parseId(testData.getData("Id", 1), "Id");
        Response response = apiOne.getById(id);

        // Validate the status code
        assertEquals(response.getStatusCode(), 200, "Should return 200");

		// Validate the response body
        apiOne.validateSchema(response, "schemas/" + testData.getData("Schema", 1) + ".json");
        logger.info("shouldMatchPostSchema PASSED");
    }

}
