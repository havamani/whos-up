package com.whosup.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.whosup.model.User;

@JsonInclude(Include.NON_NULL)
public class SettingsResponse {
	private Long contactCount;
	private Long requestCount;
	private User user;
	private Long activeUserCount;

	public SettingsResponse() {

	}

	public SettingsResponse(User user, ContactCount count, Long activeUserCount) {
		super();
		this.contactCount = count.getContactCount();
		this.requestCount = count.getRequestCount();
		this.user = user;
		this.activeUserCount = activeUserCount;
	}

	public Long getContactCount() {
		return contactCount;
	}

	public void setContactCount(Long contactCount) {
		this.contactCount = contactCount;
	}

	public Long getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(Long requestCount) {
		this.requestCount = requestCount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getActiveUserCount() {
		return activeUserCount;
	}

	public void setActiveUserCount(Long activeUserCount) {
		this.activeUserCount = activeUserCount;
	}

}
