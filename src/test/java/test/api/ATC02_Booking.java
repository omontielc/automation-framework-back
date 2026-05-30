package test.api;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import api.endpoints.BookingEndpoint;
import api.models.response.LoginResponse;
import config.Config;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import util.TestBase;

/**
 * @author Osiris Montiel Campos
 * @version 2025-11-12
 */
@Epic("Automation Framework API")
@Feature("API Booking")
public class ATC02_Booking extends TestBase {

    private final BookingEndpoint booking = new BookingEndpoint();
    
    @BeforeSuite
    public void setupSuite() {
        super.setupSuite(Config.BOOKING_URL_BASE);
        logger.info("ATC02 suite initialized");
    }

    @Test
    @Story("Create a reservation")
    @Description("POST /booking should create a resource and return 200 with the booking data")
    @Severity(SeverityLevel.CRITICAL)
    public void createBooking() {

        Response response = booking.login(testData.getData("Username", 1), testData.getData("Password", 1));

        LoginResponse loginResponse = booking.deserializeLogin(response);
        assertNotNull(loginResponse.getToken(), "Title should not be null");
        
		String token = loginResponse.getToken();
		System.out.println("==============="+token);

        logger.info("Retrieved loginResponse: {}", loginResponse);
    }

}
