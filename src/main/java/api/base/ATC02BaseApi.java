package api.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Base configuration for all API request/response specifications.
 * @author Osiris Montiel Campos
 * @version 2025-10-08
 */
public class ATC02BaseApi {

    private static final Logger logger = LogManager.getLogger(ATC02BaseApi.class);

    /**
     * Builds and returns the base.
     * @return configured ready to use
     */
    public static RequestSpecification requestSpec() {
        logger.info("Building base request specification — baseUri: {}", Config.BOIKING_BASE_URL);
        return new RequestSpecBuilder()
                .setBaseUri(Config.BOIKING_BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())   // auto-attaches to Allure
                .log(LogDetail.ALL)                   // logs request to console
                .build();
    }

    /**
     * Builds and returns the base.
     * @return configured ready to use
     */
    public static ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)                   // logs response to console
                .build();
    }

    /**
     * Configures RestAssured globally at suite startup.
     */
    public static void setup() {
        RestAssured.baseURI = Config.BOIKING_BASE_URL;
        RestAssured.requestSpecification  = requestSpec();
        RestAssured.responseSpecification = responseSpec();
        logger.info("RestAssured global configuration applied");
    }
}
