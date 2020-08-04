package com.whosup.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.whosup.model.Event;
import com.whosup.model.User;

@JsonInclude(Include.NON_NULL)
public class EventViewResponse {

	private Event event;
	private User organizer;
	private List<User> users;
	private Integer chatCount;
	private List<User> visibleToUserIds;

	public EventViewResponse() {

	}

	public EventViewResponse(Event event, User organizer, List<User> users,
			Integer chatCount) {
		super();
		this.event = event;
		this.organizer = organizer;
		this.users = users;
		this.chatCount = chatCount;
	}
	
	public EventViewResponse(Event event, User organizer, List<User> users,
			Integer chatCount, List<User> visibleToUserIds) {
		super();
		this.event = event;
		this.organizer = organizer;
		this.users = users;
		this.chatCount = chatCount;
		this.visibleToUserIds = visibleToUserIds;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Integer getChatCount() {
		return chatCount;
	}

	public void setChatCount(Integer chatCount) {
		this.chatCount = chatCount;
	}

	public List<User> getVisibleToUserIds() {
		return visibleToUserIds;
	}

	public void setVisibleToUserIds(List<User> visibleToUserIds) {
		this.visibleToUserIds = visibleToUserIds;
	}
	
	

}
