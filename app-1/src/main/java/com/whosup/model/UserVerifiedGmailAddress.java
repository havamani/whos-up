package com.whosup.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "user_verified_gmail_address")
public class UserVerifiedGmailAddress {

	@PrimaryKey
	private UserVerifiedGmailAddressKey pk;

	public UserVerifiedGmailAddress() {

	}

	public UserVerifiedGmailAddress(UserVerifiedGmailAddressKey pk) {
		this.pk = pk;
	}

	public UserVerifiedGmailAddressKey getPk() {
		return pk;
	}

	public void setPk(UserVerifiedGmailAddressKey pk) {
		this.pk = pk;
	}

}
