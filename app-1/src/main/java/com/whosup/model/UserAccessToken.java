package com.whosup.model;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_access_token")
@JsonInclude(Include.NON_NULL)
public class UserAccessToken {

	@PrimaryKey(value = "user_id")
	private UUID userId;

	@Column(value = "access_token")
	private UUID accessToken;

	public UserAccessToken() {

	}

	public UserAccessToken(UUID userId, UUID accessToken) {
		this.userId = userId;
		this.accessToken = accessToken;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(UUID accessToken) {
		this.accessToken = accessToken;
	}

}
