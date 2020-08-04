package com.whosup.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class UserVerifiedGmailAddressKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "gmail_address", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String gmailAddress;

	@PrimaryKeyColumn(name = "user_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private UUID userID;

	public UserVerifiedGmailAddressKey() {
		
	}

	public UserVerifiedGmailAddressKey(UUID userID, String gmailAddress) {
		this.userID = userID;
		this.gmailAddress = gmailAddress;
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public String getGmailAddress() {
		return gmailAddress;
	}

	public void setGmailAddress(String gmailAddress) {
		this.gmailAddress = gmailAddress;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserVerifiedGmailAddressKey other = (UserVerifiedGmailAddressKey) obj;
		if (gmailAddress == null) {
			if (other.gmailAddress != null)
				return false;
		} else if (!gmailAddress.equals(other.gmailAddress))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gmailAddress == null) ? 0 : gmailAddress.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

}
