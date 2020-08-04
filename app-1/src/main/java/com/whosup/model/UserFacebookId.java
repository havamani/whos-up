package com.whosup.model;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "user_facebook_id")
public class UserFacebookId {

	@PrimaryKey(value = "facebook_id")
	private String facebookID;

	@Column(value = "user_id")
	private UUID userID;

	public UserFacebookId() {

	}

	public UserFacebookId(String facebookID, UUID userID) {
		this.facebookID = facebookID;
		this.userID = userID;
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public String getFacebookID() {
		return facebookID;
	}

	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}
}
