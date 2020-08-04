package com.whosup.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.whosup.model.UserContact;

@JsonInclude(Include.NON_NULL)
public class Profile {

	private UserContact userContact;
	private Integer friendStatus;
	private List<CommonInterest> commomInterest;
	private boolean isConnectedViaEmailDomain;
	private boolean isConnectedViaWhosup;
	
	public Profile() {

	}

	public Profile(UserContact userContact, Integer isFriend,
			List<CommonInterest> commomInterest) {
		this.userContact = userContact;
		this.friendStatus = isFriend;
		this.commomInterest = commomInterest;
	}

	public UserContact getUserContact() {
		return userContact;
	}

	public void setUserContact(UserContact user) {
		this.userContact = user;
	}

	public List<CommonInterest> getCommomInterest() {
		return commomInterest;
	}

	public void setCommomInterest(List<CommonInterest> commomInterest) {
		this.commomInterest = commomInterest;
	}

	public Integer getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(Integer friendStatus) {
		this.friendStatus = friendStatus;
	}

	public boolean isConnectedViaEmailDomain() {
		return isConnectedViaEmailDomain;
	}

	public void setConnectedViaEmailDomain(boolean isConnectedViaEmailDomain) {
		this.isConnectedViaEmailDomain = isConnectedViaEmailDomain;
	}

	public boolean isConnectedViaWhosup() {
		return isConnectedViaWhosup;
	}

	public void setConnectedViaWhosup(boolean isConnectedViaWhosup) {
		this.isConnectedViaWhosup = isConnectedViaWhosup;
	}
	
}
