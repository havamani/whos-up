package com.whosup.model.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.whosup.model.User;
import com.whosup.model.UserEventInbox;

@JsonInclude(Include.NON_NULL)
public class UserEventDetails {

	private UserEventInbox eventDetails;
	private Boolean isThereOrganizer;
	private Boolean isOrganizer;
	private Boolean isFirstUser;
	private Boolean askFirstUser;
	private List<User> users;
	private User lastMessagedUser;
	private Date eventStartTime;
	private Date eventEndTime;
	private String eventPhotoPath;
	private Boolean isCurrentUserInQueue;
	private Integer minPlaces;
	private Boolean isPaid;
	private String webLink;
	
	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public UserEventDetails() {

	}

	public UserEventDetails(UserEventInbox eventDetails) {
		this.eventDetails = eventDetails;
		this.isOrganizer = false;
		this.isFirstUser = false;
		this.askFirstUser = true;
	}

	public UserEventInbox getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(UserEventInbox eventDetails) {
		this.eventDetails = eventDetails;
	}

	public Boolean getIsThereOrganizer() {
		return isThereOrganizer;
	}

	public void setIsThereOrganizer(Boolean isThereOrganizer) {
		this.isThereOrganizer = isThereOrganizer;
	}

	public Boolean getIsOrganizer() {
		return isOrganizer;
	}

	public void setIsOrganizer(Boolean isOrganizer) {
		this.isOrganizer = isOrganizer;
	}

	public Boolean getIsFirstUser() {
		return isFirstUser;
	}

	public void setIsFirstUser(Boolean isFirstUser) {
		this.isFirstUser = isFirstUser;
	}

	public Boolean getAskFirstUser() {
		return askFirstUser;
	}

	public void setAskFirstUser(Boolean askFirstUser) {
		this.askFirstUser = askFirstUser;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getLastMessagedUser() {
		return lastMessagedUser;
	}

	public void setLastMessagedUser(User lastMessagedUser) {
		this.lastMessagedUser = lastMessagedUser;
	}

	public Date getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public Date getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public String getEventPhotoPath() {
		return eventPhotoPath;
	}

	public void setEventPhotoPath(String eventPhotoPath) {
		this.eventPhotoPath = eventPhotoPath;
	}

	public Boolean getIsCurrentUserInQueue() {
		return isCurrentUserInQueue;
	}

	public void setIsCurrentUserInQueue(Boolean isCurrentUserInQueue) {
		this.isCurrentUserInQueue = isCurrentUserInQueue;
	}

	public Integer getMinPlaces() {
		return minPlaces;
	}

	public void setMinPlaces(Integer minPlaces) {
		this.minPlaces = minPlaces;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	
}
