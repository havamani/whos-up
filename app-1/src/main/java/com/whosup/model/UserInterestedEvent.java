package com.whosup.model;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "user_interested_event")
public class UserInterestedEvent {

	@PrimaryKey
	private UserInterestedEventKey pk;

	public UserInterestedEvent() {

	}

	public UserInterestedEvent(UserInterestedEventKey pk) {
		this.pk = pk;
	}

	public UUID getuserId() {
		return pk.getUserId();
	}

	public void setuserId(UUID userId) {
		this.pk.setUserId(userId);

	}

	public UUID getEventId() {
		return pk.getEventId();
	}

	public void setEventId(UUID eventId) {
		this.pk.setEventId(eventId);
	}

}
