package com.whosup.model;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_contact_invite")
@JsonInclude(Include.NON_NULL)
public class UserContactInvite {

	@JsonIgnore
	@PrimaryKey(value = "single_use_code")
	private String singleUseCode;

	@JsonIgnore
	@Column(value = "invited_by_user_id")
	private UUID invitedUserId;

	public UserContactInvite() {

	}

	public UserContactInvite(String singleUseCode, UUID invitedUserId) {
		this.singleUseCode = singleUseCode;
		this.invitedUserId = invitedUserId;
	}

	public String getSingleUseCode() {
		return singleUseCode;
	}

	public void setSingleUseCode(String singleUseCode) {
		this.singleUseCode = singleUseCode;
	}

	public UUID getInvitedUserId() {
		return invitedUserId;
	}

	public void setInvitedUserId(UUID invitedUserId) {
		this.invitedUserId = invitedUserId;
	}

}
