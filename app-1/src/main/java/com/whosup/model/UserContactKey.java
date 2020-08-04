package com.whosup.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class UserContactKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID userID;

	@PrimaryKeyColumn(name = "contact_user_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private UUID contactUserID;

	public UserContactKey() {

	}

	public UserContactKey(UUID userID, UUID contactUserID) {
		this.userID = userID;
		this.contactUserID = contactUserID;
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public UUID getContactUserID() {
		return contactUserID;
	}

	public void setContactUserID(UUID contactUserID) {
		this.contactUserID = contactUserID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contactUserID == null) ? 0 : contactUserID.hashCode());
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
		UserContactKey other = (UserContactKey) obj;
		if (contactUserID == null) {
			if (other.contactUserID != null)
				return false;
		} else if (!contactUserID.equals(other.contactUserID))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}
}
