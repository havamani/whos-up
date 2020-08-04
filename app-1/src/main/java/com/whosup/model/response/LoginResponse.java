package com.whosup.model.response;

import java.util.UUID;

public class LoginResponse {

	private UUID accessToken;

	public LoginResponse() {

	}

	public LoginResponse(UUID accessToken) {
		super();
		this.accessToken = accessToken;
	}

	public UUID getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(UUID accessToken) {
		this.accessToken = accessToken;
	}

}
