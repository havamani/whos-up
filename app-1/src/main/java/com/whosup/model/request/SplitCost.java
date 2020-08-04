package com.whosup.model.request;

import java.util.UUID;

public class SplitCost {

	private String payAccessToken;
	private UUID accessToken;
	private UUID eventId;
	private UUID paidCashUserId;
	private UUID refundUserId;
	private String paypalLoginId;

	public String getPayAccessToken() {
		return payAccessToken;
	}

	public void setPayAccessToken(String payAccessToken) {
		this.payAccessToken = payAccessToken;
	}

	public UUID getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(UUID accessToken) {
		this.accessToken = accessToken;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

	public UUID getPaidCashUserId() {
		return paidCashUserId;
	}

	public void setPaidCashUserId(UUID paidCashUserId) {
		this.paidCashUserId = paidCashUserId;
	}

	public UUID getRefundUserId() {
		return refundUserId;
	}

	public void setRefundUserId(UUID refundUserId) {
		this.refundUserId = refundUserId;
	}

	public String getPaypalLoginId() {
		return paypalLoginId;
	}

	public void setPaypalLoginId(String paypalLoginId) {
		this.paypalLoginId = paypalLoginId;
	}

}
