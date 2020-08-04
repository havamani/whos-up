package com.whosup.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class OwesMeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID userId;
	
	@PrimaryKeyColumn(name = "event_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private UUID eventId;
	
	@PrimaryKeyColumn(name = "owing_user_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
	private UUID owingUserId;

	public OwesMeKey(){

	}

	public OwesMeKey(UUID userId, UUID eventId, UUID owingUserId) {
		this.userId = userId;
		this.eventId = eventId;
		this.owingUserId = owingUserId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

	public UUID getOwingUserId() {
		return owingUserId;
	}

	public void setOwingUserId(UUID owingUserId) {
		this.owingUserId = owingUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((owingUserId == null) ? 0 : owingUserId.hashCode());
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		OwesMeKey other = (OwesMeKey) obj;
		if (owingUserId == null) {
			if (other.owingUserId != null)
				return false;
		} else if (!owingUserId.equals(other.owingUserId))
			return false;
		if (eventId == null) {
			if (other.eventId != null)
				return false;
		} else if (!eventId.equals(other.eventId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
