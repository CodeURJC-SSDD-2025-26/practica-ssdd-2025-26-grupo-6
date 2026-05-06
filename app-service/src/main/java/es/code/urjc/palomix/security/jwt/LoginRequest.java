package es.code.urjc.palomix.security.jwt;

public class LoginRequest {

	private String accountname;
	private String password;

	public LoginRequest() {
	}

	public LoginRequest(String username, String password) {
		this.accountname = username;
		this.password = password;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String username) {
		this.accountname = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequest [username=" + accountname + ", password=" + password + "]";
	}
}
