package com.whosup.model.response;

public class ContactCount {

	private Long contactCount;
	private Long requestCount;

	public ContactCount() {

	}

	public ContactCount(Long contactCount, Long requestCount) {
		super();
		this.contactCount = contactCount;
		this.requestCount = requestCount;
	}

	public Long getContactCount() {
		return contactCount;
	}

	public void setContactCount(Long contactCount) {
		this.contactCount = contactCount;
	}

	public Long getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(Long requestCount) {
		this.requestCount = requestCount;
	}

}
