package com.whosup.model.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SuggestList {

	private UUID contactUserID;
	private String contactFullName;
	private String contactPhotoPath;
	private Boolean isMobile;
	private String contactDetail;
	private Boolean onWhosUp;
	private Boolean isInvitedAlready;
	private String message;

	public UUID getContactUserID() {
		return contactUserID;
	}

	public void setContactUserID(UUID contactUserID) {
		this.contactUserID = contactUserID;
	}

	public String getContactFullName() {
		return contactFullName;
	}

	public void setContactFullName(String contactFullName) {
		this.contactFullName = contactFullName;
	}

	public String getContactPhotoPath() {
		return contactPhotoPath;
	}

	public void setContactPhotoPath(String contactPhotoPath) {
		this.contactPhotoPath = contactPhotoPath;
	}

	public Boolean getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(Boolean isMobile) {
		this.isMobile = isMobile;
	}

	public String getContactDetail() {
		return contactDetail;
	}

	public void setContactDetail(String contactDetail) {
		this.contactDetail = contactDetail;
	}

	public Boolean getOnWhosUp() {
		return onWhosUp;
	}

	public void setOnWhosUp(Boolean onWhosUp) {
		this.onWhosUp = onWhosUp;
	}

	public Boolean getIsInvitedAlready() {
		return isInvitedAlready;
	}

	public void setIsInvitedAlready(Boolean isInvitedAlready) {
		this.isInvitedAlready = isInvitedAlready;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
