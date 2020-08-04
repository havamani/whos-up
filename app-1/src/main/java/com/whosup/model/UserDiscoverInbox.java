package com.whosup.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_discover_inbox")
@JsonInclude(Include.NON_NULL)
public class UserDiscoverInbox extends Model {

	@PrimaryKey
	private UserDiscoverInboxKey pk;

	@Column(value = "idea_date_suggested")
	private Date ideaDateSuggested;

	@Column(value = "idea_venue_suggested")
	private UUID ideaVenueSuggested;

	@Column(value = "idea_cost_suggested")
	private BigDecimal ideaCostSuggested;

	@JsonIgnore
	@Column(value = "idea_members_suggested_event_id")
	private UUID ideaMembersSuggestedEventID;

	private String title;

	@Column(value = "photo_300_300_path")
	private String smallPhotoPath;

	@Column(value = "photo_640_1000_path")
	private String largePhotoPath;

	private String message;

	@Column(value = "people_photos")
	private List<String> peoplePhotos;

	@JsonIgnore
	@Column(value = "is_delivered")
	private Boolean isDelivered;

	@JsonIgnore
	@Column(value = "is_read")
	private Boolean isRead;
	
	@Column(value = "web_link")
	private String webLink;

	public UserDiscoverInbox() {

	}

	public UserDiscoverInbox(UserDiscoverInboxKey pk) {
		this.pk = pk;
	}

	public UserDiscoverInboxKey getPk() {
		return pk;
	}

	public void setPk(UserDiscoverInboxKey pk) {
		this.pk = pk;
	}

	public Date getIdeaDateSuggested() {
		return ideaDateSuggested;
	}

	public void setIdeaDateSuggested(Date ideaDateSuggested) {
		this.ideaDateSuggested = ideaDateSuggested;
	}

	public UUID getIdeaVenueSuggested() {
		return ideaVenueSuggested;
	}

	public void setIdeaVenueSuggested(UUID ideaVenueSuggested) {
		this.ideaVenueSuggested = ideaVenueSuggested;
	}

	public BigDecimal getIdeaCostSuggested() {
		return ideaCostSuggested;
	}

	public void setIdeaCostSuggested(BigDecimal ideaCostSuggested) {
		this.ideaCostSuggested = ideaCostSuggested;
	}

	public UUID getIdeaMembersSuggestedEventID() {
		return ideaMembersSuggestedEventID;
	}

	public void setIdeaMembersSuggestedEventID(UUID ideaMembersSuggestedEventID) {
		this.ideaMembersSuggestedEventID = ideaMembersSuggestedEventID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSmallPhotoPath() {
		return smallPhotoPath;
	}

	public void setSmallPhotoPath(String smallPhotoPath) {
		this.smallPhotoPath = smallPhotoPath;
	}

	public String getLargePhotoPath() {
		return largePhotoPath;
	}

	public void setLargePhotoPath(String largePhotoPath) {
		this.largePhotoPath = largePhotoPath;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getPeoplePhotos() {
		return peoplePhotos;
	}

	public void setPeoplePhotos(List<String> peoplePhotos) {
		this.peoplePhotos = peoplePhotos;
	}

	public Boolean getIsDelivered() {
		return isDelivered;
	}

	public void setIsDelivered(Boolean isDelivered) {
		this.isDelivered = isDelivered;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	
}
