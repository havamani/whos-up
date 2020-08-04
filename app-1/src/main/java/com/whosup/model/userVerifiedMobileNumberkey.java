package com.whosup.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class userVerifiedMobileNumberkey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID userID;

	@PrimaryKeyColumn(name = "mobile_number", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private String mobileNumber;

	public userVerifiedMobileNumberkey() {

	}

	public userVerifiedMobileNumberkey(UUID userID, String mobileNumber) {
		this.userID = userID;
		this.mobileNumber = mobileNumber;
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
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
		userVerifiedMobileNumberkey other = (userVerifiedMobileNumberkey) obj;
		if (mobileNumber == null) {
			if (other.mobileNumber != null)
				return false;
		} else if (!mobileNumber.equals(other.mobileNumber))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}
}
