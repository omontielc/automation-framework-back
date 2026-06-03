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

    @Test()
    @Story("Create a reservation")
    @Description("Booking should create a resource and return 200 with the booking data")
    @Severity(SeverityLevel.CRITICAL)
    public void createBooking() {
 
		String firstname = testData.getData("FirstName", 1);
		String lastname = testData.getData("LastName", 1);
		int totalprice = Integer.parseInt(testData.getData("TotalPrice", 1));
		boolean depositpaid = Boolean.parseBoolean(testData.getData("DepositPaid", 1));
		String checkin = testData.getData("Checkin", 1);
		String checkout = testData.getData("Checkout", 1);
		String additionalneeds = testData.getData("AdditionalNeeds", 1);

		Response response = booking.createBooking(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);
		
		assertEquals(response.getStatusCode(), 200, "Should return 200");
		logger.info("Retrieved response: {}", response);			
    }
    
    @Test(dependsOnMethods = {"createBooking"})
    @Story("Update a reservation")
    @Description("POST /booking should update a resource and return 200 with the booking data")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBooking() {
    	
		String username = testData.getData("Username", 1);
		String password = testData.getData("Password", 1);
		Response response = booking.login(username, password);

        LoginResponse loginResponse = booking.deserializeLogin(response);
        assertNotNull(loginResponse.getToken(), "Token should not be null");
        
		String token = loginResponse.getToken();
        logger.info("Retrieved token: {}", token);	
 
		String firstname = testData.getData("FirstName", 1);
		String lastname = testData.getData("LastName", 1);
		int totalprice = Integer.parseInt(testData.getData("TotalPrice", 1));
		boolean depositpaid = Boolean.parseBoolean(testData.getData("DepositPaid", 1));
		String checkin = testData.getData("NewCheckin", 1);
		String checkout = testData.getData("NewCheckout", 1);
		String additionalneeds = testData.getData("AdditionalNeeds", 1);
    	
		response = booking.getBookingByName(firstname);
		
		List<Object> bookings = response.jsonPath().getList("$");

		Assert.assertTrue(bookings.size() > 0, "The reservation list is empty.");
		
		Integer id = response.jsonPath().getInt("[0].bookingid");

		response = booking.updateBooking(id, token, firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);
		
		assertEquals(response.getStatusCode(), 200, "Should return 200");
		logger.info("Retrieved response: {}", response);	
		
		response = booking.getBookingById(id);
		logger.info("Retrieved response: {}", response);
		assertEquals(response.getStatusCode(), 200, "Should return 200");
		BookingResponse bookingResponse = booking.deserializeBooking(response);
		assertEquals(bookingResponse.getBookingDates().getCheckin(), checkin, "The checkin date wasn't be updated");	
		assertEquals(bookingResponse.getBookingDates().getCheckout(), checkout, "The checkout date wasn't be updated");	
    }
    @Test(dependsOnMethods = {"updateBooking"})
    @Story("delete a reservation")
    @Description("Booking should delete a resource and return 201")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBooking() {
    	
		String username = testData.getData("Username", 1);
		String password = testData.getData("Password", 1);
		Response response = booking.login(username, password);

        LoginResponse loginResponse = booking.deserializeLogin(response);
        assertNotNull(loginResponse.getToken(), "Token should not be null");
        
		String token = loginResponse.getToken();
        logger.info("Retrieved token: {}", token);	
 
		String firstname = testData.getData("FirstName", 1);
		
		response = booking.getBookingByName(firstname);
		
		List<Object> bookings = response.jsonPath().getList("$");

		Assert.assertTrue(bookings.size() > 0, "The reservation list is empty.");
		
		Integer id = response.jsonPath().getInt("[0].bookingid");

		response = booking.deleteBookingById(id, token);
		
		assertEquals(response.getStatusCode(), 201, "The status code should be 201");
		
		response = booking.getBookingById(id);
		assertEquals(response.getStatusCode(), 404, "The booking should be deleted");

	
    }

}
