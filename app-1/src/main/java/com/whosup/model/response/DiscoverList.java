package com.whosup.model.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.whosup.model.UserDiscoverInbox;

@JsonInclude(Include.NON_NULL)
public class DiscoverList {

	private UserDiscoverInbox inbox;
	private String description;
	private Boolean isBreak;
	private Boolean isChat;
	private Boolean isConfirmed;
	private List<String> names;
	private Double momentum;
	private Date eventExpiry;
	private Date eventStartTime;
	private Date eventDate;
	
	public DiscoverList() {

	}

	public DiscoverList(UserDiscoverInbox inbox, Boolean isBreak,
			List<String> names) {
		super();
		this.inbox = inbox;
		this.isBreak = isBreak;
		this.names = names;
	}


	public Date getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	
	

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public UserDiscoverInbox getInbox() {
		return inbox;
	}

	public void setInbox(UserDiscoverInbox inbox) {
		this.inbox = inbox;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsBreak() {
		return isBreak;
	}

	public void setIsBreak(Boolean isBreak) {
		this.isBreak = isBreak;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public Boolean getIsChat() {
		return isChat;
	}

	public void setIsChat(Boolean isChat) {
		this.isChat = isChat;
	}

	public Boolean getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(Boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public Double getMomentum() {
		return momentum;
	}

	public void setMomentum(Double momentum) {
		this.momentum = momentum;
	}

	public Date getEventExpiry() {
		return eventExpiry;
	}

	public void setEventExpiry(Date eventExpiry) {
		this.eventExpiry = eventExpiry;
	}

	
}
