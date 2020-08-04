package com.whosup.model.response;

public class Domain {

	private String emailID;
	private Boolean isVerified;
	private long activeUserCount;

	public Domain() {
		this.activeUserCount = 0;
	}

	public Domain(String emailID, Boolean isVerified, long activeUserCount) {
		super();
		this.emailID = emailID;
		this.isVerified = isVerified;
		this.activeUserCount = activeUserCount;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public long getActiveUserCount() {
		return activeUserCount;
	}

	public void setActiveUserCount(long activeUserCount) {
		this.activeUserCount = activeUserCount;
	}

}
