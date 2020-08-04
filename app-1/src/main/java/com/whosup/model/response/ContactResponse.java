package com.whosup.model.response;

import com.whosup.model.User;

public class ContactResponse {

	private User user;
	private Boolean starred;

	public ContactResponse() {

	}

	public ContactResponse(User user, Boolean starred) {
		super();
		this.user = user;
		this.starred = starred;
	}

	public Boolean getStarred() {
		return starred;
	}

	public void setStarred(Boolean starred) {
		this.starred = starred;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
