package test.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import api.endpoints.BookingEndpoint;
import api.models.response.BookingResponse;
import api.models.response.LoginResponse;
import config.Config;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import util.EnvironmentWriter;
import util.TestBase;

/**
 * @author Osiris Montiel Campos
 * @version 2025-11-12
 */
@Epic("Automation Framework API")
@Feature("API Booking")
@Owner("Osiris Montiel Campos")
public class ATC02_Booking extends TestBase {

	private BookingEndpoint booking;

	@BeforeSuite
	public void setupSuite() {
	    super.setupSuite(Config.BOOKING_URL_BASE);
	    this.booking = new BookingEndpoint(this.baseUrl);
	    EnvironmentWriter.write(Config.BOOKING_URL_BASE, "QA");
	    logger.info("ATC02 suite initialized");
	}

    // =========================================================
    // TC01 - Create Booking
    // =========================================================

    @Test
    @Story("Create a reservation")
    @Description("Verifies that POST /booking creates a new resource successfully and returns status 200 with booking data")
    @Severity(SeverityLevel.CRITICAL)
    public void createBooking() {
        String firstname    = getTestData("FirstName");
        String lastname     = getTestData("LastName");
        int totalprice      = Integer.parseInt(testData.getData("TotalPrice", 1));
        boolean depositpaid = Boolean.parseBoolean(testData.getData("DepositPaid", 1));
        String checkin      = getTestData("Checkin");
        String checkout     = getTestData("Checkout");
        String needs        = getTestData("AdditionalNeeds");

        Response response = booking.createBooking(firstname, lastname, totalprice,
                depositpaid, checkin, checkout, needs);

        validateStatusCode(response, 200);
        logger.info("createBooking completed successfully");
    }

    // =========================================================
    // TC02 - Update Booking
    // =========================================================

    @Test(dependsOnMethods = {"createBooking"})
    @Story("Update a reservation")
    @Description("Verifies that PUT /booking/{id} updates checkin and checkout dates and returns status 200")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBooking() {
        String token = authenticateAndGetToken();

        String firstname    = getTestData("FirstName");
        String lastname     = getTestData("LastName");
        int totalprice      = Integer.parseInt(testData.getData("TotalPrice", 1));
        boolean depositpaid = Boolean.parseBoolean(testData.getData("DepositPaid", 1));
        String checkin      = getTestData("NewCheckin");
        String checkout     = getTestData("NewCheckout");
        String needs        = getTestData("AdditionalNeeds");

        Integer id = getFirstBookingId(firstname);

        Response response = booking.updateBooking(id, token, firstname, lastname,
                totalprice, depositpaid, checkin, checkout, needs);

        validateStatusCode(response, 200);
        verifyBookingDatesUpdated(id, checkin, checkout);
        logger.info("updateBooking completed successfully");
    }

    // =========================================================
    // TC03 - Delete Booking
    // =========================================================

    @Test(dependsOnMethods = {"updateBooking"})
    @Story("Delete a reservation")
    @Description("Verifies that DELETE /booking/{id} removes the resource and returns 201, and that a subsequent GET returns 404")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBooking() {
        String token     = authenticateAndGetToken();
        String firstname = getTestData("FirstName");
        Integer id       = getFirstBookingId(firstname);

        sendDeleteBooking(id, token);
        verifyBookingDeleted(id);
        logger.info("deleteBooking completed successfully");
    }

    @Step("Read test data: {field}")
    private String getTestData(String field) {
        return testData.getData(field, 1);
    }

    @Step("Authenticate and retrieve session token")
    private String authenticateAndGetToken() {
        String username = getTestData("Username");
        String password = getTestData("Password");
        Response response = booking.login(username, password);
        LoginResponse loginResponse = booking.deserializeLogin(response);
        assertNotNull(loginResponse.getToken(), "Token should not be null");
        attachToken(loginResponse.getToken());
        logger.info("Token retrieved successfully");
        return loginResponse.getToken();
    }

    @Step("GET bookings by firstname: {firstname} — return first booking ID")
    private Integer getFirstBookingId(String firstname) {
        Response response = booking.getBookingByName(firstname);
        List<Object> bookings = response.jsonPath().getList("$");
        Assert.assertTrue(bookings.size() > 0, "The reservation list is empty.");
        Integer id = response.jsonPath().getInt("[0].bookingid");
        logger.info("First booking ID found: {}", id);
        return id;
    }

    @Step("DELETE /booking/{id} — verify returns 201")
    private void sendDeleteBooking(Integer id, String token) {
        Response response = booking.deleteBookingById(id, token);
        validateStatusCode(response, 201);
    }

    @Step("Verify status code is {expectedStatus}")
    private void validateStatusCode(Response response, int expectedStatus) {
        assertEquals(response.getStatusCode(), expectedStatus,
                "Expected status " + expectedStatus + " but got " + response.getStatusCode());
    }

    @Step("Verify booking dates updated — checkin: {expectedCheckin}, checkout: {expectedCheckout}")
    private void verifyBookingDatesUpdated(Integer id, String expectedCheckin, String expectedCheckout) {
        Response response = booking.getBookingById(id);
        validateStatusCode(response, 200);
        BookingResponse bookingResponse = booking.deserializeBooking(response);
        assertEquals(bookingResponse.getBookingDates().getCheckin(), expectedCheckin,
                "Checkin date was not updated correctly");
        assertEquals(bookingResponse.getBookingDates().getCheckout(), expectedCheckout,
                "Checkout date was not updated correctly");
    }

    @Step("Verify booking {id} no longer exists — expect 404")
    private void verifyBookingDeleted(Integer id) {
        Response response = booking.getBookingById(id);
        validateStatusCode(response, 404);
    }

    @Attachment(value = "Session Token", type = "text/plain")
    private String attachToken(String token) {
        return token;
    }
}