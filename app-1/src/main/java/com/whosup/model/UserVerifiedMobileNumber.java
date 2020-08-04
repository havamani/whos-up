package com.whosup.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "user_verified_mobile_number")
public class UserVerifiedMobileNumber {

	@PrimaryKey
	private userVerifiedMobileNumberkey pk;

	public UserVerifiedMobileNumber(){
		
	}
	
	public UserVerifiedMobileNumber(userVerifiedMobileNumberkey pk) {
		this.pk = pk;
	}

	public userVerifiedMobileNumberkey getPk() {
		return pk;
	}

	public void setPk(userVerifiedMobileNumberkey pk) {
		this.pk = pk;
	}

	public String getMobileNumber() {
		return pk.getMobileNumber();
	}

	public void setMobileNumber(String mobileNumber) {
		this.pk.setMobileNumber(mobileNumber);
	}
}
