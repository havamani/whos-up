package com.whosup.model.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class OwesMeResponse {

	private List<OwesMeUserDetails> owesMeUserDetails;
	private UUID eventID;
	private String title;
	private Date eventDate;
	private BigDecimal totalAmount;
	private BigDecimal amountPaid;
	private BigDecimal costToSplit;
	private String currency;
	
	public UUID getEventID() {
		return eventID;
	}

	public void setEventID(UUID eventID) {
		this.eventID = eventID;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public BigDecimal getCostToSplit() {
		return costToSplit;
	}

	public void setCostToSplit(BigDecimal costToSplit) {
		this.costToSplit = costToSplit;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<OwesMeUserDetails> getOwesMeUserDetails() {
		return owesMeUserDetails;
	}

	public void setOwesMeUserDetails(List<OwesMeUserDetails> owesMeUserDetails) {
		this.owesMeUserDetails = owesMeUserDetails;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

}
