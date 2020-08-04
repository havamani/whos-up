package com.whosup.model.response;

import java.util.List;

import com.whosup.model.UserContact;

public class MyCrowd {

	private Integer totalPage;
	private List<UserContact> userContacts;

	public MyCrowd() {
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public List<UserContact> getUserContacts() {
		return userContacts;
	}

	public void setUserContacts(List<UserContact> userContacts) {
		this.userContacts = userContacts;
	}

}
