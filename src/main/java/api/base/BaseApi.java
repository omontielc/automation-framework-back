package api.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Base configuration for all API request/response specifications.
 * @author Osiris Montiel Campos
 * @version 2025-11-12
 */
public class BaseApi {

    private static final Logger logger = LogManager.getLogger(BaseApi.class);

    /**
     * Builds a request specification with an explicit baseUri.
     *
     * @param baseUri the base URL for the API under test
     * @return configured RequestSpecification
     */
    public static RequestSpecification requestSpec(String baseUri) {
        logger.info("Building request specification for: {}", baseUri);
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Builds and returns the base response specification.
     * @return configured ResponseSpecification
     */
    public static ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }
}
