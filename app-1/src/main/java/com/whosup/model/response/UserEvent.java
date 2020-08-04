package com.whosup.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserEvent {

	private UserEventDetails upcoming;
	private List<UserEventDetails> remaining;

	public UserEvent() {

	}

	public UserEventDetails getUpcoming() {
		return upcoming;
	}

	public void setUpcoming(UserEventDetails upcoming) {
		this.upcoming = upcoming;
	}

	public List<UserEventDetails> getRemaining() {
		return remaining;
	}

	public void setRemaining(List<UserEventDetails> remaining) {
		this.remaining = remaining;
	}
}
