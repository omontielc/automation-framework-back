package api.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing the body of a Create Post request.
 * @author Osiris Montiel Campos
 * @version 2025-07-06
 */
public class BookingRequest {

	@JsonProperty("firstname")
    private String firstName;

	@JsonProperty("lastname")
	private String lastName;

	@JsonProperty("totalprice")
	private int    totalPrice;

	@JsonProperty("depositpaid")
	private boolean depositPaid;

	@JsonProperty("bookingdates")
	private BookingDates bookingDates;

	@JsonProperty("additionalneeds")
	private String additionalNeeds;

	public BookingRequest(String firstName, String lastName, int totalPrice, boolean depositPaid, String checkin, String checkout, String additionalNeeds) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.totalPrice = totalPrice;
		this.depositPaid = depositPaid;
		this.bookingDates    = new BookingDates(checkin, checkout);
		this.additionalNeeds = additionalNeeds;
	}

	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public boolean isDepositPaid() {
		return depositPaid;
	}
	public BookingDates getBookingDates(){
		return bookingDates;
	}
	public String getAdditionalNeeds() {
		return additionalNeeds;
	}
}
