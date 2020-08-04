package com.whosup.model;

import java.util.Date;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_suggested_invite")
@JsonInclude(Include.NON_NULL)
public class UserSuggestedInvite {

	@PrimaryKey
	@JsonIgnore
	private UserSuggestedInviteKey pk;

	@Column(value = "contact_name")
	private String contactName;

	@Column(value = "contact_photo_path")
	private String contactPhotoPath;

	@JsonIgnore
	@Column(value = "date_suggested")
	private Date dateSuggested;

	@Column(value = "suggestion_reason")
	private String suggestionReason;

	@Column(value = "code_last_generated")
	private String codeLastGenerated;

	public UserSuggestedInvite() {

	}

	public UserSuggestedInvite(UserSuggestedInviteKey pk, String contactName,
			String contactPhotoPath, Date dateSuggested,
			String suggestionReason) {
		this.pk = pk;
		this.contactName = contactName;
		this.contactPhotoPath = contactPhotoPath;
		this.dateSuggested = dateSuggested;
		this.suggestionReason = suggestionReason;
	}

	public UserSuggestedInviteKey getPk() {
		return pk;
	}

	public void setPk(UserSuggestedInviteKey pk) {
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

	public String getCodeLastGenerated() {
		return codeLastGenerated;
	}

	public void setCodeLastGenerated(String codeLastGenerated) {
		this.codeLastGenerated = codeLastGenerated;
	}

	public String getMobileOrEmailId() {
		return pk.getMobileOrEmailId();
	}

	public void setMobileOrEmailId(String contactID) {
		this.pk.setMobileOrEmailId(contactID);
	}
}
