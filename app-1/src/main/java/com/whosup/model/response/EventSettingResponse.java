package com.whosup.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.whosup.model.Event;
import com.whosup.model.User;

@JsonInclude(Include.NON_NULL)
public class EventSettingResponse {

	private Event event;
	private List<User> confirmed;
	private List<User> unConfirmed;
	private User organizer;
	private Boolean isEventCompleted;
	private List<User> visibleToUserIds;

	public EventSettingResponse() {

	}

	public EventSettingResponse(Event event) {
		this.event = event;
		this.isEventCompleted = false;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<User> getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(List<User> confirmed) {
		this.confirmed = confirmed;
	}

	public List<User> getUnConfirmed() {
		return unConfirmed;
	}

	public void setUnConfirmed(List<User> unConfirmed) {
		this.unConfirmed = unConfirmed;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public Boolean getIsEventCompleted() {
		return isEventCompleted;
	}

	public void setIsEventCompleted(Boolean isEventCompleted) {
		this.isEventCompleted = isEventCompleted;
	}

	public List<User> getVisibleToUserIds() {
		return visibleToUserIds;
	}

	public void setVisibleToUserIds(List<User> visibleToUserIds) {
		this.visibleToUserIds = visibleToUserIds;
	}

}
