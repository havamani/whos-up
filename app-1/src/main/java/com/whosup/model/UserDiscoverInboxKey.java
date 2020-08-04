package com.whosup.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class UserDiscoverInboxKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID userID;

	@PrimaryKeyColumn(name = "is_event", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
	private Boolean isEvent;

	@PrimaryKeyColumn(name = "event_or_idea_id", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
	private UUID eventID;

	public UserDiscoverInboxKey() {

	}

	public UserDiscoverInboxKey(UUID userID, UUID eventID, Boolean isEvent) {
		this.userID = userID;
		this.isEvent = isEvent;
		this.eventID = eventID;
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public UUID getEventID() {
		return eventID;
	}

	public void setEventID(UUID eventID) {
		this.eventID = eventID;
	}

	public Boolean getIsEvent() {
		return isEvent;
	}

	public void setIsEvent(Boolean isEvent) {
		this.isEvent = isEvent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventID == null) ? 0 : eventID.hashCode());
		result = prime * result + ((isEvent == null) ? 0 : isEvent.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDiscoverInboxKey other = (UserDiscoverInboxKey) obj;
		if (eventID == null) {
			if (other.eventID != null)
				return false;
		} else if (!eventID.equals(other.eventID))
			return false;
		if (isEvent == null) {
			if (other.isEvent != null)
				return false;
		} else if (!isEvent.equals(other.isEvent))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}

}
