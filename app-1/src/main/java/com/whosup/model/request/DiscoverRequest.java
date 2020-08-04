package com.whosup.model.request;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DiscoverRequest {

	private UUID accessToken;
	private UUID ideaID;
	private String ideaCategory;
	private String eventName;
	private String smallPhotoPath;
	private String LargePhotoPath;
	private String webLink;
	private String description;
	private Date eventDate;
	private String chosenLocation;
	private String chosenAddress;
	private String chosenLocCountry;
	private String chosenLocAdmin1;
	private String chosenLocAdmin2;
	private String chosenLocAdmin3;
	private List<String> gatherFrom;
	private Boolean isPublic;
	private Integer minPlaces;
	private Integer maxPlaces;
	private String gatherLocation;
	private String gatherAddress;
	private String gatherLocCountry;
	private String gatherLocAdmin1;
	private String gatherLocAdmin2;
	private String gatherLocAdmin3;
	private Double gatherRadius;
	private Integer minAge;
	private Integer maxAge;
	private Boolean femaleOnly;
	private Boolean myCrowdOnly;
	private List<UUID> contactIDs;
	private List<String> filter;
	private UUID eventId;
	private Date eventStartTime;
	private Date eventEndTime;

	public UUID getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(UUID accessToken) {
		this.accessToken = accessToken;
	}

	public UUID getIdeaID() {
		return ideaID;
	}

	public void setIdeaID(UUID ideaID) {
		this.ideaID = ideaID;
	}

	public String getIdeaCategory() {
		return ideaCategory;
	}

	public void setIdeaCategory(String ideaCategory) {
		this.ideaCategory = ideaCategory;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getSmallPhotoPath() {
		return smallPhotoPath;
	}

	public void setSmallPhotoPath(String smallPhotoPath) {
		this.smallPhotoPath = smallPhotoPath;
	}

	public String getLargePhotoPath() {
		return LargePhotoPath;
	}

	public void setLargePhotoPath(String largePhotoPath) {
		LargePhotoPath = largePhotoPath;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getChosenLocation() {
		return chosenLocation;
	}

	public void setChosenLocation(String chosenLocation) {
		this.chosenLocation = chosenLocation;
	}

	public String getChosenAddress() {
		return chosenAddress;
	}

	public void setChosenAddress(String chosenAddress) {
		this.chosenAddress = chosenAddress;
	}

	public String getChosenLocCountry() {
		return chosenLocCountry;
	}

	public void setChosenLocCountry(String chosenLocCountry) {
		this.chosenLocCountry = chosenLocCountry;
	}

	public String getChosenLocAdmin1() {
		return chosenLocAdmin1;
	}

	public void setChosenLocAdmin1(String chosenLocAdmin1) {
		this.chosenLocAdmin1 = chosenLocAdmin1;
	}

	public String getChosenLocAdmin2() {
		return chosenLocAdmin2;
	}

	public void setChosenLocAdmin2(String chosenLocAdmin2) {
		this.chosenLocAdmin2 = chosenLocAdmin2;
	}

	public String getChosenLocAdmin3() {
		return chosenLocAdmin3;
	}

	public void setChosenLocAdmin3(String chosenLocAdmin3) {
		this.chosenLocAdmin3 = chosenLocAdmin3;
	}

	public List<String> getGatherFrom() {
		return gatherFrom;
	}

	public void setGatherFrom(List<String> gatherFrom) {
		this.gatherFrom = gatherFrom;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getMinPlaces() {
		return minPlaces;
	}

	public void setMinPlaces(Integer minPlaces) {
		this.minPlaces = minPlaces;
	}

	public Integer getMaxPlaces() {
		return maxPlaces;
	}

	public void setMaxPlaces(Integer maxPlaces) {
		this.maxPlaces = maxPlaces;
	}

	public String getGatherLocation() {
		return gatherLocation;
	}

	public void setGatherLocation(String gatherLocation) {
		this.gatherLocation = gatherLocation;
	}

	public String getGatherAddress() {
		return gatherAddress;
	}

	public void setGatherAddress(String gatherAddress) {
		this.gatherAddress = gatherAddress;
	}

	public String getGatherLocCountry() {
		return gatherLocCountry;
	}

	public void setGatherLocCountry(String gatherLocCountry) {
		this.gatherLocCountry = gatherLocCountry;
	}

	public String getGatherLocAdmin1() {
		return gatherLocAdmin1;
	}

	public void setGatherLocAdmin1(String gatherLocAdmin1) {
		this.gatherLocAdmin1 = gatherLocAdmin1;
	}

	public String getGatherLocAdmin2() {
		return gatherLocAdmin2;
	}

	public void setGatherLocAdmin2(String gatherLocAdmin2) {
		this.gatherLocAdmin2 = gatherLocAdmin2;
	}

	public String getGatherLocAdmin3() {
		return gatherLocAdmin3;
	}

	public void setGatherLocAdmin3(String gatherLocAdmin3) {
		this.gatherLocAdmin3 = gatherLocAdmin3;
	}

	public Double getGatherRadius() {
		return gatherRadius;
	}

	public void setGatherRadius(Double gatherRadius) {
		this.gatherRadius = gatherRadius;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Boolean getFemaleOnly() {
		return femaleOnly;
	}

	public void setFemaleOnly(Boolean femaleOnly) {
		this.femaleOnly = femaleOnly;
	}

	public Boolean getMyCrowdOnly() {
		return myCrowdOnly;
	}

	public void setMyCrowdOnly(Boolean myCrowdOnly) {
		this.myCrowdOnly = myCrowdOnly;
	}

	public List<UUID> getContactIDs() {
		return contactIDs;
	}

	public void setContactIDs(List<UUID> contactIDs) {
		this.contactIDs = contactIDs;
	}

	public List<String> getFilter() {
		return filter;
	}

	public void setFilter(List<String> filter) {
		this.filter = filter;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
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

}
