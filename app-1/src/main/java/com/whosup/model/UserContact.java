package com.whosup.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_contact")
@JsonInclude(Include.NON_NULL)
public class UserContact {

	@PrimaryKey
	@JsonIgnore
	private UserContactKey pk;

	@Column(value = "contact_full_name")
	private String contactFullName;

	@Column(value = "contact_photo_path")
	private String contactPhotoPath;

	@Column(value = "is_starred")
	private Boolean isStarred;

	@JsonIgnore
	@Column(value = "is_contact_signed_up")
	private Boolean isContactSignedUp;

	@JsonIgnore
	@Column(value = "connection_status_id")
	private Integer connectionStatusID;

	@JsonIgnore
	@Column(value = "date_connected")
	private Date dateConnected;

	@JsonIgnore
	@Column(value = "date_requested")
	private Date dateRequested;

	@JsonIgnore
	@Column(value = "date_suggested")
	private Date dateSuggested;

	@Column(value = "is_connected_via_fb")
	private Boolean isConnectedViaFB;

	@Column(value = "is_connected_via_linked_in")
	private Boolean isConnectedViaLinkedIN;

	@Column(value = "is_connected_via_import_gmail")
	private Boolean isConnectedViaImportGmail;

	@Column(value = "is_connected_via_import_mobile")
	private Boolean isConnectedViaImportMobile;

	@JsonIgnore
	@Column(value = "last_known_latlong")
	private String lastKnownLatlong;

	@Column(value = "is_nearby_recently")
	private Boolean isNearby;

	@JsonIgnore
	@Column(value = "is_nearby_last_7_days")
	private Boolean isNearbyLast7Days;

	@Column(value = "share_interest")
	private Boolean shareInterest;
	
	@Column(value="is_connected_via_email_domain")
	private Boolean isConnectedViaEmailDomain;

	@Column(value="is_connected_via_whosup")
	private Boolean isConnectedViaWhosup;
	
	public UserContact() {

	}

	public UserContact(UserContactKey pk, String contactFullName,
			String contactPhotoPath) {
		this.pk = pk;
		this.contactFullName = contactFullName;
		this.contactPhotoPath = contactPhotoPath;
		this.isStarred = false;
		this.isConnectedViaFB = false;
		this.isConnectedViaLinkedIN = false;
		this.isConnectedViaImportGmail = false;
		this.isConnectedViaImportMobile = false;
		this.isNearby = false;
		this.isNearbyLast7Days = false;
		this.shareInterest = true;
		this.isContactSignedUp = true;
	}

	public UserContactKey getPk() {
		return pk;
	}

	public void setPk(UserContactKey pk) {
		this.pk = pk;
	}

	public String getContactFullName() {
		return contactFullName;
	}

	public void setContactFullName(String contactFullName) {
		this.contactFullName = contactFullName;
	}

	public String getContactPhotoPath() {
		return contactPhotoPath;
	}

	public void setContactPhotoPath(String contactPhotoPath) {
		this.contactPhotoPath = contactPhotoPath;
	}

	public Boolean getIsStarred() {
		return isStarred;
	}

	public void setIsStarred(Boolean isStarred) {
		this.isStarred = isStarred;
	}

	public Boolean getIsContactSignedUp() {
		return isContactSignedUp;
	}

	public void setIsContactSignedUp(Boolean isContactSignedUp) {
		this.isContactSignedUp = isContactSignedUp;
	}

	public Integer getConnectionStatusID() {
		return connectionStatusID;
	}

	public void setConnectionStatusID(Integer connectionStatusID) {
		this.connectionStatusID = connectionStatusID;
	}

	public Date getDateConnected() {
		return dateConnected;
	}

	public void setDateConnected(Date dateConnected) {
		this.dateConnected = dateConnected;
	}

	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

	public Date getDateSuggested() {
		return dateSuggested;
	}

	public void setDateSuggested(Date dateSuggested) {
		this.dateSuggested = dateSuggested;
	}

	public Boolean getIsConnectedViaFB() {
		return isConnectedViaFB;
	}

	public void setIsConnectedViaFB(Boolean isConnectedViaFB) {
		this.isConnectedViaFB = isConnectedViaFB;
	}

	public Boolean getIsConnectedViaLinkedIN() {
		return isConnectedViaLinkedIN;
	}

	public void setIsConnectedViaLinkedIN(Boolean isConnectedViaLinkedIN) {
		this.isConnectedViaLinkedIN = isConnectedViaLinkedIN;
	}

	public Boolean getIsConnectedViaImportGmail() {
		return isConnectedViaImportGmail;
	}

	public void setIsConnectedViaImportGmail(Boolean isConnectedViaImportGmail) {
		this.isConnectedViaImportGmail = isConnectedViaImportGmail;
	}

	public Boolean getIsConnectedViaImportMobile() {
		return isConnectedViaImportMobile;
	}

	public void setIsConnectedViaImportMobile(Boolean isConnectedViaImportMobile) {
		this.isConnectedViaImportMobile = isConnectedViaImportMobile;
	}

	public String getLastKnownLatlong() {
		return lastKnownLatlong;
	}

	public void setLastKnownLatlong(String lastKnownLatlong) {
		this.lastKnownLatlong = lastKnownLatlong;
	}

	public Boolean getIsNearby() {
		return isNearby;
	}

	public void setIsNearby(Boolean isNearby) {
		this.isNearby = isNearby;
	}

	public Boolean getIsNearbyLast7Days() {
		return isNearbyLast7Days;
	}

	public void setIsNearbyLast7Days(Boolean isNearbyLast7Days) {
		this.isNearbyLast7Days = isNearbyLast7Days;
	}

	public Boolean getShareInterest() {
		return shareInterest;
	}

	public void setShareInterest(Boolean shareInterest) {
		this.shareInterest = shareInterest;
	}

	public UUID getContactUserID() {
		return pk.getContactUserID();
	}

	public void setContactUserID(UUID contactUserID) {
		this.pk.setContactUserID(contactUserID);
	}

	public Boolean getIsConnectedViaEmailDomain() {
		return isConnectedViaEmailDomain;
	}

	public void setIsConnectedViaEmailDomain(Boolean isConnectedViaEmailDomain) {
		this.isConnectedViaEmailDomain = isConnectedViaEmailDomain;
	}

	public Boolean getIsConnectedViaWhosup() {
		return isConnectedViaWhosup;
	}

	public void setIsConnectedViaWhosup(Boolean isConnectedViaWhosup) {
		this.isConnectedViaWhosup = isConnectedViaWhosup;
	}
	
}