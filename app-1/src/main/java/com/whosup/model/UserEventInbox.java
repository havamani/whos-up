package com.whosup.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_my_events_inbox")
@JsonInclude(Include.NON_NULL)
public class UserEventInbox {

	@PrimaryKey
	private UserEventInboxKey pk;

	@Column(value = "event_date")
	private Date eventDate;

	@Column(value = "is_from_public")
	private Boolean isFromPublic;

	@Column(value = "is_chat")
	private Boolean isChat;

	@Column(value = "is_history")
	private Boolean isHistory;

	@Column(value = "is_event_confirmed")
	private Boolean isEventConfirmed;

	@Column(value = "event_title")
	private String eventTitle;

	@Column(value = "people_gathered")
	private Integer peopleGathered;

	@Column(value = "people_to_gather")
	private Integer peopleToGather;

	@Column(value = "places_filled")
	private Integer placesFilled;

	@Column(value = "places_to_fill")
	private Integer placesToFill;

	@Column(value = "is_user_confirmed")
	private Boolean isUserConfirmed;

	@Column(value = "is_about_to_timeout")
	private Boolean isAboutToTimeout;

	@Column(value = "chat_id")
	private UUID chatId;

	public UserEventInbox() {

	}

	public UserEventInbox(UserEventInboxKey pk, Boolean isFromPublic) {
		this.pk = pk;
		this.isFromPublic = isFromPublic;
		this.isChat = false;
		this.isUserConfirmed = false;
		this.isEventConfirmed = false;
		this.isHistory = false;
		this.isAboutToTimeout = false;
	}

	public UserEventInboxKey getPk() {
		return pk;
	}

	public void setPk(UserEventInboxKey pk) {
		this.pk = pk;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Boolean getIsFromPublic() {
		return isFromPublic;
	}

	public void setIsFromPublic(Boolean isFromPublic) {
		this.isFromPublic = isFromPublic;
	}

	public void setIsChat(Boolean isChat) {
		this.isChat = isChat;
	}

	public Boolean getIsChat() {
		return isChat;
	}

	public Boolean getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Boolean isHistory) {
		this.isHistory = isHistory;
	}

	public Boolean getIsEventConfirmed() {
		return isEventConfirmed;
	}

	public void setIsEventConfirmed(Boolean isEventConfirmed) {
		this.isEventConfirmed = isEventConfirmed;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public Integer getPeopleGathered() {
		return peopleGathered;
	}

	public void setPeopleGathered(Integer peopleGathered) {
		this.peopleGathered = peopleGathered;
	}

	public Integer getPeopleToGather() {
		return peopleToGather;
	}

	public void setPeopleToGather(Integer peopleToGather) {
		this.peopleToGather = peopleToGather;
	}

	public Integer getPlacesFilled() {
		return placesFilled;
	}

	public void setPlacesFilled(Integer placesFilled) {
		this.placesFilled = placesFilled;
	}

	public Integer getPlacesToFill() {
		return placesToFill;
	}

	public void setPlacesToFill(Integer placesToFill) {
		this.placesToFill = placesToFill;
	}

	public Boolean getIsUserConfirmed() {
		return isUserConfirmed;
	}

	public void setIsUserConfirmed(Boolean isUserConfirmed) {
		this.isUserConfirmed = isUserConfirmed;
	}

	public Boolean getIsAboutToTimeout() {
		return isAboutToTimeout;
	}

	public void setIsAboutToTimeout(Boolean isAboutToTimeout) {
		this.isAboutToTimeout = isAboutToTimeout;
	}

	public UUID getChatId() {
		return chatId;
	}

	public void setChatId(UUID chatId) {
		this.chatId = chatId;
	}

}
