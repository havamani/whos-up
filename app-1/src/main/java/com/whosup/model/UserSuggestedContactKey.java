package com.whosup.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class UserSuggestedContactKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID userID;

	@PrimaryKeyColumn(name = "contact_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private UUID contactID;

	public UserSuggestedContactKey() {

	}

	public UserSuggestedContactKey(UUID userID, UUID contactID) {
		this.userID = userID;
		this.contactID = contactID;
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public UUID getContactID() {
		return contactID;
	}

	public void setContactID(UUID contactID) {
		this.contactID = contactID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contactID == null) ? 0 : contactID.hashCode());
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
		UserSuggestedContactKey other = (UserSuggestedContactKey) obj;
		if (contactID == null) {
			if (other.contactID != null)
				return false;
		} else if (!contactID.equals(other.contactID))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}
}
