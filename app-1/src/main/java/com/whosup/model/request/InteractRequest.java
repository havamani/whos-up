package com.whosup.model.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.whosup.model.Venue;

public class InteractRequest {

	private UUID accessToken;
	private UUID eventId;
	private String smallPhotoPath;
	private String largePhotoPath;
	private String webLink;
	private String description;
	private Date eventDate;
	private Date eventStartTime;
	private Date eventEndTime;
	private String chosenLocation;
	private String chosenLocationAddress;
	private Boolean isPaid;
	private Boolean isLike;
	private Boolean isFixedAmount;
	private Integer minAge;
	private Integer maxAge;
	private Integer minPlaces;
	private Integer maxPlaces;
	private BigDecimal costToSplit;
	private Boolean isCancel;
	private Boolean isPublic;
	private Boolean femaleOnly;
	private Boolean myCrowdOnly;
	private Venue venue;
	private UUID deleteUserId;
	private List<UUID> contactIDs;
	private List<String> gatherFrom;
	private Float totalCost;
	private String name;
	private String picturePath;
	private Date pictureDate;
	private Integer rating;
	private String issue;
	private String chosenLocCountry;
	private String chosenLocAdmin1;
	private String chosenLocAdmin2;
	private String chosenLocAdmin3;
	private String gatherLocation;
	private String gatherAddress;
	private String gatherLocCountry;
	private String gatherLocAdmin1;
	private String gatherLocAdmin2;
	private String gatherLocAdmin3;
	private Double gatherRadius;
	private String comment;
	private String currency;

	public UUID getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(UUID accessToken) {
		this.accessToken = accessToken;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
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

	public String getChosenLocation() {
		return chosenLocation;
	}

	public void setChosenLocation(String chosenLocation) {
		this.chosenLocation = chosenLocation;
	}

	public String getChosenLocationAddress() {
		return chosenLocationAddress;
	}

	public void setChosenLocationAddress(String chosenLocationAddress) {
		this.chosenLocationAddress = chosenLocationAddress;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Boolean getIsLike() {
		return isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}

	public Boolean getIsFixedAmount() {
		return isFixedAmount;
	}

	public void setIsFixedAmount(Boolean isFixedAmount) {
		this.isFixedAmount = isFixedAmount;
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

	public BigDecimal getCostToSplit() {
		return costToSplit;
	}

	public void setCostToSplit(BigDecimal costToSplit) {
		this.costToSplit = costToSplit;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
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

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public UUID getDeleteUserId() {
		return deleteUserId;
	}

	public void setDeleteUserId(UUID deleteUserId) {
		this.deleteUserId = deleteUserId;
	}

	public List<UUID> getContactIDs() {
		return contactIDs;
	}

	public void setContactIDs(List<UUID> contactIDs) {
		this.contactIDs = contactIDs;
	}

	public List<String> getGatherFrom() {
		return gatherFrom;
	}

	public void setGatherFrom(List<String> gatherFrom) {
		this.gatherFrom = gatherFrom;
	}

	public Float getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Float totalCost) {
		this.totalCost = totalCost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public Date getPictureDate() {
		return pictureDate;
	}

	public void setPictureDate(Date pictureDate) {
		this.pictureDate = pictureDate;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
