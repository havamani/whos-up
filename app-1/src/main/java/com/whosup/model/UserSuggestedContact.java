package com.whosup.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_suggested_contact")
@JsonInclude(Include.NON_NULL)
public class UserSuggestedContact {

	@PrimaryKey
	@JsonIgnore
	private UserSuggestedContactKey pk;

	@Column(value = "contact_name")
	private String contactName;

	@Column(value = "contact_photo_path")
	private String contactPhotoPath;

	@JsonIgnore
	@Column(value = "date_suggested")
	private Date dateSuggested;

	@Column(value = "suggestion_reason")
	private String suggestionReason;

	@Column(value = "is_request_sent")
	private Boolean isRequestSent;

	@JsonIgnore
	@Column(value = "is_suggested_via_import_gmail")
	private Boolean isSuggestedViaImportGmail;

	@JsonIgnore
	@Column(value = "is_suggested_via_import_mobile")
	private Boolean isSuggestedViaImportMobile;

	public UserSuggestedContact() {

	}

	public UserSuggestedContact(UserSuggestedContactKey pk, String contactName,
			String contactPhotoPath, Date dateSuggested,
			String suggestionReason) {
		this.pk = pk;
		this.contactName = contactName;
		this.contactPhotoPath = contactPhotoPath;
		this.dateSuggested = dateSuggested;
		this.suggestionReason = suggestionReason;
		this.isRequestSent = false;
		this.isSuggestedViaImportGmail = false;
		this.isSuggestedViaImportMobile = false;
	}

	public UserSuggestedContactKey getPk() {
		return pk;
	}

	public void setPk(UserSuggestedContactKey pk) {
		this.pk = pk;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhotoPath() {
		return contactPhotoPath;
	}

	public void setContactPhotoPath(String contactPhotoPath) {
		this.contactPhotoPath = contactPhotoPath;
	}

	public Date getDateSuggested() {
		return dateSuggested;
	}

	public void setDateSuggested(Date dateSuggested) {
		this.dateSuggested = dateSuggested;
	}

	public String getSuggestionReason() {
		return suggestionReason;
	}

	public void setSuggestionReason(String suggestionReason) {
		this.suggestionReason = suggestionReason;
	}

	public Boolean getIsRequestSent() {
		return isRequestSent;
	}

	public void setIsRequestSent(Boolean isRequestSent) {
		this.isRequestSent = isRequestSent;
	}

	public Boolean getIsSuggestedViaImportGmail() {
		return isSuggestedViaImportGmail;
	}

	public void setIsSuggestedViaImportGmail(Boolean isSuggeestedViaImportGmail) {
		this.isSuggestedViaImportGmail = isSuggeestedViaImportGmail;
	}

	public Boolean getIsSuggestedViaImportMobile() {
		return isSuggestedViaImportMobile;
	}

	public void setIsSuggestedViaImportMobile(
			Boolean isSuggeestedViaImportMobile) {
		this.isSuggestedViaImportMobile = isSuggeestedViaImportMobile;
	}

	public UUID getContactID() {
		return pk.getContactID();
	}

	public void setContactID(UUID contactID) {
		this.pk.setContactID(contactID);
	}
}
