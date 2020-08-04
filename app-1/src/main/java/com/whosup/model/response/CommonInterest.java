package com.whosup.model.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CommonInterest {

	private String title;
	private String photoPath;
	private Date date;
	private List<String> commonFriends;
	private String appStoreLink;

	public CommonInterest() {

	}

	public CommonInterest(String title, String photoPath, Date date) {
		super();
		this.title = title;
		this.photoPath = photoPath;
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getCommonFriends() {
		return commonFriends;
	}

	public void setCommonFriends(List<String> commonFriends) {
		this.commonFriends = commonFriends;
	}

	public String getAppStoreLink() {
		return appStoreLink;
	}

	public void setAppStoreLink(String appStoreLink) {
		this.appStoreLink = appStoreLink;
	}

}
