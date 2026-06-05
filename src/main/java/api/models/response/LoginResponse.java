package api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO representing a Post returned by the API.
 * @author Osiris Montiel Campos
 * @version 2025-10-08
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {

    private String token;

    // ----------------------------------------------------------------
    // Getters & Setters
    // ----------------------------------------------------------------

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "LoginResponse [token=" + token + "]";
	}
}
