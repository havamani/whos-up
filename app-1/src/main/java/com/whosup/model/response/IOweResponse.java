package com.whosup.model.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class IOweResponse {

	private UUID organiserId;
	private String organiserName;
	private String organiserPhotoPath;
	private UUID eventId;
	private String eventName;
	private Date eventDate;
	private BigDecimal amount;
	private Integer peoplePaid;
	private Integer totalPeople;
	private String currency;

	public UUID getOrganiserId() {
		return organiserId;
	}

	public void setOrganiserId(UUID organiserId) {
		this.organiserId = organiserId;
	}

	public String getOrganiserName() {
		return organiserName;
	}

	public void setOrganiserName(String organiserName) {
		this.organiserName = organiserName;
	}

	public String getOrganiserPhotoPath() {
		return organiserPhotoPath;
	}

	public void setOrganiserPhotoPath(String organiserPhotoPath) {
		this.organiserPhotoPath = organiserPhotoPath;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getPeoplePaid() {
		return peoplePaid;
	}

	public void setPeoplePaid(Integer peoplePaid) {
		this.peoplePaid = peoplePaid;
	}

	public Integer getTotalPeople() {
		return totalPeople;
	}

	public void setTotalPeople(Integer totalPeople) {
		this.totalPeople = totalPeople;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	
	

}
