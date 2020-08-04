package com.whosup.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class UserSuggestedInviteKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID userID;

	@PrimaryKeyColumn(name = "mobile_number_or_email_address", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private String mobileOrEmailId;

	public UserSuggestedInviteKey() {

	}

	public UserSuggestedInviteKey(UUID userID, String mobileOrEmailId) {
		this.userID = userID;
		this.mobileOrEmailId = mobileOrEmailId;
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public String getMobileOrEmailId() {
		return mobileOrEmailId;
	}

	public void setMobileOrEmailId(String mobileOrEmailId) {
		this.mobileOrEmailId = mobileOrEmailId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mobileOrEmailId == null) ? 0 : mobileOrEmailId.hashCode());
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
		UserSuggestedInviteKey other = (UserSuggestedInviteKey) obj;
		if (mobileOrEmailId == null) {
			if (other.mobileOrEmailId != null)
				return false;
		} else if (!mobileOrEmailId.equals(other.mobileOrEmailId))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}
}
