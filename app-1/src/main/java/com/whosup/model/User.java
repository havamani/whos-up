package com.whosup.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user")
@JsonInclude(Include.NON_NULL)
public class User extends Model {

	@PrimaryKey(value = "user_id")
	private UUID userID;

	@Column(value = "full_name")
	private String fullName;

	@Column(value = "photo_path")
	private String photoPath;

	private Date dob;

	private String gender;

	@Column(value = "linked_in_id")
	private String linkedInID;

	@JsonIgnore
	@Column(value = "linked_in_email")
	private String linkedInEmail;

	@JsonIgnore
	@Column(value = "linked_in_access_token")
	private String linkedInAccessToken;

	@Column(value = "facebook_id")
	private String facebookID;

	@Column(value = "facebook_email")
	private String facebookEmail;

	@JsonIgnore
	@Column(value = "facebook_first_name")
	private String facebookFirstName;

	@JsonIgnore
	@Column(value = "facebook_last_name")
	private String facebookLastName;

	@JsonIgnore
	@Column(value = "facebook_photo_path")
	private String facebookPhotoPath;

	@JsonIgnore
	@Column(value = "facebook_access_token")
	private String facebookAccessToken;

	@JsonIgnore
	@Column(value = "date_signed_up")
	private Date dateSignedUp;

	@Column(value = "domain_email_to_verify")
	private String domainEmail;

	@JsonIgnore
	@Column(value = "domain_verification_code")
	private String domainVerificationCode;

	@JsonIgnore
	@Column(value = "domain_verification_code_expire")
	private Date domainVerificationExpiry;

	@Column(value = "verified_domain_email")
	private String verifiedDomainEmail;

	@Column(value = "mobile_number_to_verify")
	private String mobileNumber;

	@JsonIgnore
	@Column(value = "mobile_verification_code")
	private String mobileVerificationCode;

	@JsonIgnore
	@Column(value = "mobile_verification_code_expire")
	private Date mobileVerificationExpiry;

	@Column(value = "verified_mobile_number")
	private String verifiedMobileNumber;

	@JsonIgnore
	@Column(value = "device_id")
	private String deviceID;

	@JsonIgnore
	@Column(value = "device_type")
	private String deviceType;

	@JsonIgnore
	@Column(value = "google_id")
	private String googleID;

	@JsonIgnore
	@Column(value = "mobile_contacts_pending")
	private List<String> mobileContactsPending;

	@JsonIgnore
	@Column(value = "gmail_contacts_pending")
	private List<String> gmailContactsPending;

	@JsonIgnore
	@Column(value = "linked_in_contacts_pending")
	private List<String> linkedInContactsPending;

	@Column(value = "system_language")
	private UUID systemLanguage;

	@Column(value = "additional_language")
	private List<UUID> additionalLanguage;

	@Column(value = "is_silent")
	private Boolean isSilent;

	@Column(value = "is_available_search")
	private Boolean isAvailableSearch;

	@Column(value = "is_anyone_make_contact")
	private Boolean isAnyoneMakeContact;

	@JsonIgnore
	@Column(value = "xmtp_chat_url")
	private String xmtp_chat_url;

	@Column(value = "last_seen_online")
	private Date lastSeenOnline;

	@JsonIgnore
	@Column(value = "access_token")
	private UUID accessToken;

	@JsonIgnore
	@Column(value = "access_token_expire_date")
	private Date accessTokenExpireDate;

	@Column(value = "send_invites_to_email")
	private Boolean sendInvitesToEmail;

	@Column(value = "stripe_account_id")
	private String stripeId;

	// @JsonIgnore
	@Column(value = "stripe_sign_in_token")
	private String stripeSignInToken;

	@JsonIgnore
	@Column(value = "most_recent_loc_latlong")
	private String recentLocLatLong;

	@JsonIgnore
	@Column(value = "layer_persistent_authentication_token")
	private UUID layerAuthenticationToken;

	@Column(value = "weather_last_updated")
	private Date weatherLastUpdated;

	@Column(value = "weather_main")
	private String main;

	@Column(value = "weather_description")
	private String description;

	@Column(value = "main_temp_min")
	private Double minTemp;

	@Column(value = "main_temp_max")
	private Double maxTemp;

	@Column(value = "wind_speed")
	private Double windSpeed;

	@Column(value = "reach_last_pulled")
	private Date reachLastPulled;

	@Column(value = "reach_organizer_influence")
	private Double reachOrganizerInfluence;

	@JsonIgnore
	@Column(value = "buzz_advanced_last_removed_user_ids")
	private List<UUID> buzzAdvancedLastRemovedUserIds;

	@Column(value = "stripe_customer_id")
	private String stripeCustomerId;

	@Column(value = "cards_for_customer_id")
	private List<String> cardsForCustomer_id;

	public User() {

	}

	public User(String facebookID, String fullName, String gender) {
		this.facebookID = facebookID;
		this.fullName = fullName;
		this.gender = gender;
		this.isSilent = false;
		this.isAvailableSearch = true;
		this.isAnyoneMakeContact = true;
		this.sendInvitesToEmail = true;
		this.domainEmail = "";
		this.mobileNumber = "";
		this.verifiedDomainEmail = "";
		this.verifiedMobileNumber = "";
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLinkedInID() {
		return linkedInID;
	}

	public void setLinkedInID(String linkedInID) {
		this.linkedInID = linkedInID;
	}

	public String getLinkedInEmail() {
		return linkedInEmail;
	}

	public void setLinkedInEmail(String linkedInEmail) {
		this.linkedInEmail = linkedInEmail;
	}

	public String getLinkedInAccessToken() {
		return linkedInAccessToken;
	}

	public void setLinkedInAccessToken(String linkedInAccessToken) {
		this.linkedInAccessToken = linkedInAccessToken;
	}

	public String getFacebookID() {
		return facebookID;
	}

	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}

	public String getFacebookEmail() {
		return facebookEmail;
	}

	public void setFacebookEmail(String facebookEmail) {
		this.facebookEmail = facebookEmail;
	}

	public String getFacebookFirstName() {
		return facebookFirstName;
	}

	public void setFacebookFirstName(String facebookFirstName) {
		this.facebookFirstName = facebookFirstName;
	}

	public String getFacebookLastName() {
		return facebookLastName;
	}

	public void setFacebookLastName(String facebookLastName) {
		this.facebookLastName = facebookLastName;
	}

	public String getFacebookPhotoPath() {
		return facebookPhotoPath;
	}

	public void setFacebookPhotoPath(String facebookPhotoPath) {
		this.facebookPhotoPath = facebookPhotoPath;
	}

	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}

	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
	}

	public Date getDateSignedUp() {
		return dateSignedUp;
	}

	public void setDateSignedUp(Date dateSignedUp) {
		this.dateSignedUp = dateSignedUp;
	}

	public String getDomainEmail() {
		return domainEmail;
	}

	public void setDomainEmail(String domainEmail) {
		this.domainEmail = domainEmail;
	}

	public String getDomainVerificationCode() {
		return domainVerificationCode;
	}

	public void setDomainVerificationCode(String domainVerificationCode) {
		this.domainVerificationCode = domainVerificationCode;
	}

	public Date getDomainVerificationExpiry() {
		return domainVerificationExpiry;
	}

	public void setDomainVerificationExpiry(Date domainVerificationExpiry) {
		this.domainVerificationExpiry = domainVerificationExpiry;
	}

	public String getVerifiedDomainEmail() {
		return verifiedDomainEmail;
	}

	public void setVerifiedDomainEmail(String verifiedDomainEmail) {
		this.verifiedDomainEmail = verifiedDomainEmail;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMobileVerificationCode() {
		return mobileVerificationCode;
	}

	public void setMobileVerificationCode(String mobileVerificationCode) {
		this.mobileVerificationCode = mobileVerificationCode;
	}

	public Date getMobileVerificationExpiry() {
		return mobileVerificationExpiry;
	}

	public void setMobileVerificationExpiry(Date mobileVerificationExpiry) {
		this.mobileVerificationExpiry = mobileVerificationExpiry;
	}

	public String getVerifiedMobileNumber() {
		return verifiedMobileNumber;
	}

	public void setVerifiedMobileNumber(String verifiedMobileNumber) {
		this.verifiedMobileNumber = verifiedMobileNumber;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getGoogleID() {
		return googleID;
	}

	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}

	public List<String> getMobileContactsPending() {
		return mobileContactsPending;
	}

	public void setMobileContactsPending(List<String> mobileContactsPending) {
		this.mobileContactsPending = mobileContactsPending;
	}

	public List<String> getGmailContactsPending() {
		return gmailContactsPending;
	}

	public void setGmailContactsPending(List<String> gmailContactsPending) {
		this.gmailContactsPending = gmailContactsPending;
	}

	public List<String> getLinkedInContactsPending() {
		return linkedInContactsPending;
	}

	public void setLinkedInContactsPending(List<String> linkedInContactsPending) {
		this.linkedInContactsPending = linkedInContactsPending;
	}

	public UUID getSystemLanguage() {
		return systemLanguage;
	}

	public void setSystemLanguage(UUID systemLanguage) {
		this.systemLanguage = systemLanguage;
	}

	public List<UUID> getAdditionalLanguage() {
		return additionalLanguage;
	}

	public void setAdditionalLanguage(List<UUID> additionalLanguage) {
		this.additionalLanguage = additionalLanguage;
	}

	public Boolean getIsSilent() {
		return isSilent;
	}

	public void setIsSilent(Boolean isSilent) {
		this.isSilent = isSilent;
	}

	public Boolean getIsAvailableSearch() {
		return isAvailableSearch;
	}

	public void setIsAvailableSearch(Boolean isAvailableSearch) {
		this.isAvailableSearch = isAvailableSearch;
	}

	public Boolean getIsAnyoneMakeContact() {
		return isAnyoneMakeContact;
	}

	public void setIsAnyoneMakeContact(Boolean isAnyoneMakeContact) {
		this.isAnyoneMakeContact = isAnyoneMakeContact;
	}

	public String getXmtp_chat_url() {
		return xmtp_chat_url;
	}

	public void setXmtp_chat_url(String xmtp_chat_url) {
		this.xmtp_chat_url = xmtp_chat_url;
	}

	public Date getLastSeenOnline() {
		return lastSeenOnline;
	}

	public void setLastSeenOnline(Date lastSeenOnline) {
		this.lastSeenOnline = lastSeenOnline;
	}

	public UUID getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(UUID accessToken) {
		this.accessToken = accessToken;
	}

	public Date getAccessTokenExpireDate() {
		return accessTokenExpireDate;
	}

	public void setAccessTokenExpireDate(Date accessTokenExpireDate) {
		this.accessTokenExpireDate = accessTokenExpireDate;
	}

	public Boolean getSendInvitesToEmail() {
		return sendInvitesToEmail;
	}

	public void setSendInvitesToEmail(Boolean sendInvitesToEmail) {
		this.sendInvitesToEmail = sendInvitesToEmail;
	}

	public String getStripeId() {
		return stripeId;
	}

	public void setStripeId(String stripeId) {
		this.stripeId = stripeId;
	}

	public String getStripeSignInToken() {
		return stripeSignInToken;
	}

	public void setStripeSignInToken(String stripeSignInToken) {
		this.stripeSignInToken = stripeSignInToken;
	}

	public String getRecentLocLatLong() {
		return recentLocLatLong;
	}

	public void setRecentLocLatLong(String recentLocLatLong) {
		this.recentLocLatLong = recentLocLatLong;
	}

	public UUID getLayerAuthenticationToken() {
		return layerAuthenticationToken;
	}

	public void setLayerAuthenticationToken(UUID layerAuthenticationToken) {
		this.layerAuthenticationToken = layerAuthenticationToken;
	}

	public List<UUID> getBuzzAdvancedLastRemovedUserIds() {
		return buzzAdvancedLastRemovedUserIds;
	}

	public void setBuzzAdvancedLastRemovedUserIds(
			List<UUID> buzzAdvancedLastRemovedUserIds) {
		this.buzzAdvancedLastRemovedUserIds = buzzAdvancedLastRemovedUserIds;
	}

	public Date getWeatherLastUpdated() {
		return weatherLastUpdated;
	}

	public void setWeatherLastUpdated(Date weatherLastUpdated) {
		this.weatherLastUpdated = weatherLastUpdated;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(Double minTemp) {
		this.minTemp = minTemp;
	}

	public Double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(Double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public Double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Date getReachLastPulled() {
		return reachLastPulled;
	}

	public void setReachLastPulled(Date reachLastPulled) {
		this.reachLastPulled = reachLastPulled;
	}

	public Double getReachOrganizerInfluence() {
		return reachOrganizerInfluence;
	}

	public void setReachOrganizerInfluence(Double reachOrganizerInfluence) {
		this.reachOrganizerInfluence = reachOrganizerInfluence;
	}

	public String getStripeCustomerId() {
		return stripeCustomerId;
	}

	public void setStripeCustomerId(String stripeCustomerId) {
		this.stripeCustomerId = stripeCustomerId;
	}

	public List<String> getCardsForCustomer_id() {
		return cardsForCustomer_id;
	}

	public void setCardsForCustomer_id(List<String> cardsForCustomer_id) {
		this.cardsForCustomer_id = cardsForCustomer_id;
	}

}
