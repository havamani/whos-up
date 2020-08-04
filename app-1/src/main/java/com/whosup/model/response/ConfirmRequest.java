package com.whosup.model.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;
import java.util.UUID;
@JsonInclude(Include.NON_NULL)
public class ConfirmRequest {

	private String organizer;
	private BigDecimal amount;
	private Integer totalAttending;
	private Integer maxPlace;
	private Boolean isConfirmed;
	private Boolean isCancelled;
	private Boolean inQueue;
	private Integer position;
	private String currency;
	private List<UUID> confirmedUserIDs;

	public ConfirmRequest() {

	}

	public ConfirmRequest(String organizer, BigDecimal amount,
			Integer totalAttending, Integer maxPlace) {
		super();
		this.organizer = organizer;
		this.amount = amount;
		this.totalAttending = totalAttending;
		this.maxPlace = maxPlace;
		this.isConfirmed = false;
		this.isCancelled = false;
		this.inQueue = false;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getTotalAttending() {
		return totalAttending;
	}

	public void setTotalAttending(Integer totalAttending) {
		this.totalAttending = totalAttending;
	}

	public Integer getMaxPlace() {
		return maxPlace;
	}

	public void setMaxPlace(Integer maxPlace) {
		this.maxPlace = maxPlace;
	}

	public Boolean getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(Boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public Boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Boolean getInQueue() {
		return inQueue;
	}

	public void setInQueue(Boolean inQueue) {
		this.inQueue = inQueue;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<UUID> getConfirmedUserIDs() {
		return confirmedUserIDs;
	}

	public void setConfirmedUserIDs(List<UUID> confirmedUserIDs) {
		this.confirmedUserIDs = confirmedUserIDs;
	}

	
}
