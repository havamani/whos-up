package com.whosup.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_contact_request")
@JsonInclude(Include.NON_NULL)
public class UserContactRequest {

	@PrimaryKey
	@JsonIgnore
	private UserSuggestedContactKey pk;

	@Column(value = "contact_name")
	private String contactName;

	@Column(value = "contact_photo_path")
	private String contactPhotoPath;

	@JsonIgnore
	@Column(value = "date_invited")
	private Date dateInvited;

	public UserContactRequest() {

	}

	public UserContactRequest(UserSuggestedContactKey pk, String contactName,
			String contactPhotoPath, Date dateInvited) {
		this.pk = pk;
		this.contactName = contactName;
		this.contactPhotoPath = contactPhotoPath;
		this.dateInvited = dateInvited;
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

	public Date getDateInvited() {
		return dateInvited;
	}

	public void setDateInvited(Date dateInvited) {
		this.dateInvited = dateInvited;
	}

	public UUID getContactID() {
		return pk.getContactID();
	}

	public void setContactID(UUID contactID) {
		this.pk.setContactID(contactID);
	}
}
