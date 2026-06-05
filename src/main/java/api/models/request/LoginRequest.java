package api.models.request;

/**
 * POJO representing the body of a request.
 * @author Osiris Montiel Campos
 * @version 2025-07-06
 */
public class LoginRequest {

    private String username;
    private String password;

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
