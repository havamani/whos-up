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

@Table(value = "event")
@JsonInclude(Include.NON_NULL)
public class Event extends Model {

	@PrimaryKey(value = "event_id")
	private UUID eventID;

	@Column(value = "organizer_user_id")
	private UUID organizerUserID;

	@JsonIgnore
	@Column(value = "gathered_1st_by_user_id")
	private UUID gathered1stByUserID;

	@JsonIgnore
	@Column(value = "is_first_user_reject")
	private Boolean isFirstUserReject;

	@JsonIgnore
	@Column(value = "gathered_1st_date")
	private Date gathered1stDate;

	@Column(value = "has_organizer_confirmed")
	private Boolean hasOrganiserConfirmed;

	@JsonIgnore
	@Column(value = "visible_to_user_ids")
	private List<UUID> visibleToUserIDs;

	@Column(value = "interested_user_ids")
	private List<UUID> interestedUserIDs;

	@JsonIgnore
	@Column(value = "queued_user_ids")
	private List<UUID> queuedUserIDs;

	@JsonIgnore
	@Column(value = "unconfirmed_user_ids")
	private List<UUID> unConfirmedUserIDs;

	@JsonIgnore
	@Column(value = "confirmed_user_ids")
	private List<UUID> confirmedUserIDs;

	@JsonIgnore
	@Column(value = "reach_last_pulled")
	private Date reachLadtPulled;

	@JsonIgnore
	@Column(value = "reach_end")
	private Date reachEnd;

	@JsonIgnore
	@Column(value = "reach_initial_credits_remaining")
	private Integer reachIntialCreditsRemaining;

	@JsonIgnore
	@Column(value = "idea_id")
	private UUID ideaID;

	@JsonIgnore
	private List<String> keywords;

	@Column(value = "title")
	private String title;

	@Column(value = "description")
	private String description;

	@Column(value = "web_link")
	private String webLink;

	@Column(value = "photo_300_300_path")
	private String smallPhotoPath;

	@Column(value = "photo_640_1000_path")
	private String largePhotoPath;

	@Column(value = "event_date")
	private Date eventDate;

	@Column(value = "event_start_time")
	private Date eventStartTime;

	@Column(value = "event_end_time")
	private Date eventEndTime;

	@Column(value = "event_expiry")
	private Date eventExpiry;

	@JsonIgnore
	@Column(value = "event_link_url")
	private String eventLinkUrl;

	@JsonIgnore
	@Column(value = "chat_id")
	private String chatId;

	@JsonIgnore
	@Column(value = "venue_id")
	private UUID venueID;

	@JsonIgnore
	@Column(value = "venue_name")
	private String venueName;

	@Column(value = "chosen_loc_addr")
	private String chosenLocAddr;

	@Column(value = "chosen_location_latlong")
	private String chosenLocationLatLong;

	@JsonIgnore
	@Column(value = "chosen_loc_country")
	private String chosenLocCountry;

	@JsonIgnore
	@Column(value = "chosen_loc_admin_1")
	private String chosenLocAdmin1;

	@JsonIgnore
	@Column(value = "chosen_loc_admin_2")
	private String chosenLocAdmin2;

	@JsonIgnore
	@Column(value = "chosen_loc_admin_3")
	private String chosenLocAdmin3;

	@JsonIgnore
	private Double mass;

	@JsonIgnore
	private Double momentum;

	@JsonIgnore
	@Column(value = "compound_effect")
	private Double compoundEffect;

	@JsonIgnore
	@Column(value = "count_buzzes")
	private Integer countBuzzes;

	@JsonIgnore
	@Column(value = "time_momentum_last_updated")
	private Date momentumLastUpdated;

	@JsonIgnore
	@Column(value = "gather_advanced_loc_addr")
	private String gatherAdvancedLocAddr;

	@JsonIgnore
	@Column(value = "gather_advanced_loc_latlong")
	private String gatherAdvancedLocationLatLong;

	@JsonIgnore
	@Column(value = "gather_advanced_loc_country")
	private String gatherAdvancedLocCountry;

	@JsonIgnore
	@Column(value = "gather_advanced_loc_admin_1")
	private String gatherAdvancedLocAdmin1;

	@JsonIgnore
	@Column(value = "gather_advanced_loc_admin_2")
	private String gatherAdvancedLocAdmin2;

	@JsonIgnore
	@Column(value = "gather_advanced_loc_admin_3")
	private String gatherAdvancedLocAdmin3;

	@Column(value = "gather_advanced_loc_radius")
	private Double gatherAdvancedLocRadius;

	@Column(value = "gather_advanced_age_lower")
	private Integer gatherAdvancedAgeLower;

	@Column(value = "gather_advanced_age_upper")
	private Integer gatherAdvancedAgeUpper;

	@Column(value = "gather_advanced_female_only")
	private Boolean gatherAdvancedFemaleOnly;

	@Column(value = "is_paid")
	private Boolean isPaid;

	@Column(value = "is_fixed_amount")
	private Boolean isFixedAmount;

	@Column(value = "currency")
	private String currency;

	@Column(value = "cost_to_split")
	private BigDecimal costToSplit;

	@Column(value = "fixed_cost")
	private BigDecimal fixedCost;

	@JsonIgnore
	@Column(value = "total_paid")
	private Integer totalPaid;

	@Column(value = "total_attending")
	private Integer totalAttending;

	@Column(value = "min_places")
	private Integer minPlaces;

	@Column(value = "max_places")
	private Integer maxPlaces;

	@Column(value = "is_public")
	private Boolean isPublic;

	@Column(value = "is_chat")
	private Boolean isChat;

	@Column(value = "is_history")
	private Boolean isHistory;

	@JsonIgnore
	@Column(value = "is_deleted")
	private Boolean isDeleted;

	@Column(value = "gather_advanced_is_fb_selected")
	private Boolean gatherAdvancedIsFbSelected;

	@Column(value = "gather_advanced_is_li_selected")
	private Boolean gatherAdvancedIsLiSelected;

	@Column(value = "gather_advanced_is_network_selected")
	private Boolean gatherAdvancedIsNetworkSelected;

	@Column(value = "gather_advanced_is_starred_filter_selected")
	private Boolean gatherAdvancedIsStarredFilterSelected;

	@Column(value = "gather_advanced_is_wu_selected")
	private Boolean gatherAdvancedIsWuSelected;

	public Event() {

	}

	public Event(UUID eventID, UUID gathered1stByUserID, Date gathered1stDate,
			UUID ideaID) {
		this.eventID = eventID;
		this.gathered1stByUserID = gathered1stByUserID;
		organizerUserID = gathered1stByUserID;
		this.gathered1stDate = gathered1stDate;
		this.isFirstUserReject = false;
		this.hasOrganiserConfirmed = false;
		this.ideaID = ideaID;
		this.totalAttending = 0;
		this.isChat = false;
		this.isDeleted = false;
		this.isHistory = false;
		this.mass = 1.0;
		this.momentum = 0.0;
		this.compoundEffect = 0.0;
		this.countBuzzes = 1;
		this.momentumLastUpdated = gathered1stDate;
		this.gatherAdvancedIsFbSelected = false;
		this.gatherAdvancedIsLiSelected = false;
		this.gatherAdvancedIsNetworkSelected = false;
		this.gatherAdvancedIsStarredFilterSelected = false;
		this.gatherAdvancedIsWuSelected = false;
	}

	public UUID getEventID() {
		return eventID;
	}

	public void setEventID(UUID eventID) {
		this.eventID = eventID;
	}

	public UUID getOrganizerUserID() {
		return organizerUserID;
	}

	public void setOrganizerUserID(UUID organizerUserID) {
		this.organizerUserID = organizerUserID;
	}

	public UUID getGathered1stByUserID() {
		return gathered1stByUserID;
	}

	public void setGathered1stByUserID(UUID gathered1stByUserID) {
		this.gathered1stByUserID = gathered1stByUserID;
	}

	public Boolean getIsFirstUserReject() {
		return isFirstUserReject;
	}

	public void setIsFirstUserReject(Boolean isFirstUserReject) {
		this.isFirstUserReject = isFirstUserReject;
	}

	public Date getGathered1stDate() {
		return gathered1stDate;
	}

	public void setGathered1stDate(Date gathered1stDate) {
		this.gathered1stDate = gathered1stDate;
	}

	public Boolean getHasOrganiserConfirmed() {
		return hasOrganiserConfirmed;
	}

	public void setHasOrganiserConfirmed(Boolean hasOrganiserConfirmed) {
		this.hasOrganiserConfirmed = hasOrganiserConfirmed;
	}

	public List<UUID> getVisibleToUserIDs() {
		return visibleToUserIDs;
	}

	public void setVisibleToUserIDs(List<UUID> visibleToUserIDs) {
		this.visibleToUserIDs = visibleToUserIDs;
	}

	public List<UUID> getInterestedUserIDs() {
		return interestedUserIDs;
	}

	public void setInterestedUserIDs(List<UUID> interestedUserIDs) {
		this.interestedUserIDs = interestedUserIDs;
	}

	public List<UUID> getQueuedUserIDs() {
		return queuedUserIDs;
	}

	public void setQueuedUserIDs(List<UUID> queuedUserIDs) {
		this.queuedUserIDs = queuedUserIDs;
	}

	public List<UUID> getUnConfirmedUserIDs() {
		return unConfirmedUserIDs;
	}

	public void setUnConfirmedUserIDs(List<UUID> unConfirmedUserIDs) {
		this.unConfirmedUserIDs = unConfirmedUserIDs;
	}

	public List<UUID> getConfirmedUserIDs() {
		return confirmedUserIDs;
	}

	public void setConfirmedUserIDs(List<UUID> confirmedUserIDs) {
		this.confirmedUserIDs = confirmedUserIDs;
	}

	public Date getReachLadtPulled() {
		return reachLadtPulled;
	}

	public void setReachLadtPulled(Date reachLadtPulled) {
		this.reachLadtPulled = reachLadtPulled;
	}

	public Date getReachEnd() {
		return reachEnd;
	}

	public void setReachEnd(Date reachEnd) {
		this.reachEnd = reachEnd;
	}

	public Integer getReachIntialCreditsRemaining() {
		return reachIntialCreditsRemaining;
	}

	public void setReachIntialCreditsRemaining(
			Integer reachIntialCreditsRemaining) {
		this.reachIntialCreditsRemaining = reachIntialCreditsRemaining;
	}

	public UUID getIdeaID() {
		return ideaID;
	}

	public void setIdeaID(UUID ideaID) {
		this.ideaID = ideaID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
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

	public Date getEventExpiry() {
		return eventExpiry;
	}

	public void setEventExpiry(Date eventExpiry) {
		this.eventExpiry = eventExpiry;
	}

	public String getEventLinkUrl() {
		return eventLinkUrl;
	}

	public void setEventLinkUrl(String eventLinkUrl) {
		this.eventLinkUrl = eventLinkUrl;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public UUID getVenueID() {
		return venueID;
	}

	public void setVenueID(UUID venueID) {
		this.venueID = venueID;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getChosenLocAddr() {
		return chosenLocAddr;
	}

	public void setChosenLocAddr(String chosenLocAddr) {
		this.chosenLocAddr = chosenLocAddr;
	}

	public String getChosenLocationLatLong() {
		return chosenLocationLatLong;
	}

	public void setChosenLocationLatLong(String chosenLocationLatLong) {
		this.chosenLocationLatLong = chosenLocationLatLong;
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

	public Double getMass() {
		return mass;
	}

	public void setMass(Double mass) {
		this.mass = mass;
	}

	public Double getMomentum() {
		return momentum;
	}

	public void setMomentum(Double momentum) {
		this.momentum = momentum;
	}

	public Double getCompoundEffect() {
		return compoundEffect;
	}

	public void setCompoundEffect(Double compoundEffect) {
		this.compoundEffect = compoundEffect;
	}

	public Integer getCountBuzzes() {
		return countBuzzes;
	}

	public void setCountBuzzes(Integer countBuzzes) {
		this.countBuzzes = countBuzzes;
	}

	public Date getMomentumLastUpdated() {
		return momentumLastUpdated;
	}

	public void setMomentumLastUpdated(Date momentumLastUpdated) {
		this.momentumLastUpdated = momentumLastUpdated;
	}

	public String getGatherAdvancedLocAddr() {
		return gatherAdvancedLocAddr;
	}

	public void setGatherAdvancedLocAddr(String gatherAdvancedLocAddr) {
		this.gatherAdvancedLocAddr = gatherAdvancedLocAddr;
	}

	public String getGatherAdvancedLocationLatLong() {
		return gatherAdvancedLocationLatLong;
	}

	public void setGatherAdvancedLocationLatLong(
			String gatherAdvancedLocationLatLong) {
		this.gatherAdvancedLocationLatLong = gatherAdvancedLocationLatLong;
	}

	public String getGatherAdvancedLocCountry() {
		return gatherAdvancedLocCountry;
	}

	public void setGatherAdvancedLocCountry(String gatherAdvancedLocCountry) {
		this.gatherAdvancedLocCountry = gatherAdvancedLocCountry;
	}

	public String getGatherAdvancedLocAdmin1() {
		return gatherAdvancedLocAdmin1;
	}

	public void setGatherAdvancedLocAdmin1(String gatherAdvancedLocAdmin1) {
		this.gatherAdvancedLocAdmin1 = gatherAdvancedLocAdmin1;
	}

	public String getGatherAdvancedLocAdmin2() {
		return gatherAdvancedLocAdmin2;
	}

	public void setGatherAdvancedLocAdmin2(String gatherAdvancedLocAdmin2) {
		this.gatherAdvancedLocAdmin2 = gatherAdvancedLocAdmin2;
	}

	public String getGatherAdvancedLocAdmin3() {
		return gatherAdvancedLocAdmin3;
	}

	public void setGatherAdvancedLocAdmin3(String gatherAdvancedLocAdmin3) {
		this.gatherAdvancedLocAdmin3 = gatherAdvancedLocAdmin3;
	}

	public Double getGatherAdvancedLocRadius() {
		return gatherAdvancedLocRadius;
	}

	public void setGatherAdvancedLocRadius(Double gatherAdvancedLocRadius) {
		this.gatherAdvancedLocRadius = gatherAdvancedLocRadius;
	}

	public Integer getGatherAdvancedAgeLower() {
		return gatherAdvancedAgeLower;
	}

	public void setGatherAdvancedAgeLower(Integer gatherAdvancedAgeLower) {
		this.gatherAdvancedAgeLower = gatherAdvancedAgeLower;
	}

	public Integer getGatherAdvancedAgeUpper() {
		return gatherAdvancedAgeUpper;
	}

	public void setGatherAdvancedAgeUpper(Integer gatherAdvancedAgeUpper) {
		this.gatherAdvancedAgeUpper = gatherAdvancedAgeUpper;
	}

	public Boolean getGatherAdvancedFemaleOnly() {
		return gatherAdvancedFemaleOnly;
	}

	public void setGatherAdvancedFemaleOnly(Boolean gatherAdvancedFemaleOnly) {
		this.gatherAdvancedFemaleOnly = gatherAdvancedFemaleOnly;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Boolean getIsFixedAmount() {
		return isFixedAmount;
	}

	public void setIsFixedAmount(Boolean isFixedAmount) {
		this.isFixedAmount = isFixedAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(BigDecimal fixedCost) {
		this.fixedCost = fixedCost;
	}

	public BigDecimal getCostToSplit() {
		return costToSplit;
	}

	public void setCostToSplit(BigDecimal costToSplit) {
		this.costToSplit = costToSplit;
	}

	public Integer getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(Integer totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Integer getTotalAttending() {
		return totalAttending;
	}

	public void setTotalAttending(Integer totalAttending) {
		this.totalAttending = totalAttending;
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

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Boolean getIsChat() {
		return isChat;
	}

	public void setIsChat(Boolean isChat) {
		this.isChat = isChat;
	}

	public Boolean getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Boolean isHistory) {
		this.isHistory = isHistory;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getGatherAdvancedIsFbSelected() {
		return gatherAdvancedIsFbSelected;
	}

	public void setGatherAdvancedIsFbSelected(Boolean gatherAdvancedIsFbSelected) {
		this.gatherAdvancedIsFbSelected = gatherAdvancedIsFbSelected;
	}

	public Boolean getGatherAdvancedIsLiSelected() {
		return gatherAdvancedIsLiSelected;
	}

	public void setGatherAdvancedIsLiSelected(Boolean gatherAdvancedIsLiSelected) {
		this.gatherAdvancedIsLiSelected = gatherAdvancedIsLiSelected;
	}

	public Boolean getGatherAdvancedIsNetworkSelected() {
		return gatherAdvancedIsNetworkSelected;
	}

	public void setGatherAdvancedIsNetworkSelected(
			Boolean gatherAdvancedIsNetworkSelected) {
		this.gatherAdvancedIsNetworkSelected = gatherAdvancedIsNetworkSelected;
	}

	public Boolean getGatherAdvancedIsStarredFilterSelected() {
		return gatherAdvancedIsStarredFilterSelected;
	}

	public void setGatherAdvancedIsStarredFilterSelected(
			Boolean gatherAdvancedIsStarredFilterSelected) {
		this.gatherAdvancedIsStarredFilterSelected = gatherAdvancedIsStarredFilterSelected;
	}

	public Boolean getGatherAdvancedIsWuSelected() {
		return gatherAdvancedIsWuSelected;
	}

	public void setGatherAdvancedIsWuSelected(Boolean gatherAdvancedIsWuSelected) {
		this.gatherAdvancedIsWuSelected = gatherAdvancedIsWuSelected;
	}

}
