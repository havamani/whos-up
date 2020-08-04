package com.whosup.model.request;

import java.util.List;
import java.util.UUID;

public class NetworkRequest {

	private UUID accessToken;
	private UUID contactId;
	private String nonce;
	private String id;
	private String gmailAccessToken;
	private String linkedInAccessToken;
	private String picturePath;
	private String latLong;
	private String country;
	private String admin1;
	private String admin2;
	private String admin3;
	private String code;
	private Boolean accept;
	private Boolean shareInterest;
	private Boolean isSilent;
	private Boolean isFavourite;
	private Boolean isAvailableSearch;
	private Boolean isAnyoneMakeContact;
	private Boolean isPayPalRemoved;
	private Boolean sendEmail;
	private int pageNo;
	private String tag;
	private Boolean isImport;
	private List<ContactDetail> contacts;
	private UUID systemLanguage;
	private List<UUID> additionalLanguages;
	private Boolean connectViaWhosup;
	private Boolean connectViaEmailDomain;
	
	public UUID getSystemLanguage() {
		return systemLanguage;
	}

	public void setSystemLanguage(UUID systemLanguage) {
		this.systemLanguage = systemLanguage;
	}

	public List<UUID> getAdditionalLanguages() {
		return additionalLanguages;
	}

	public void setAdditionalLanguages(List<UUID> additionalLanguages) {
		this.additionalLanguages = additionalLanguages;
	}

	public UUID getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(UUID accessToken) {
		this.accessToken = accessToken;
	}

	public UUID getContactId() {
		return contactId;
	}

	public void setContactId(UUID contactId) {
		this.contactId = contactId;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGmailAccessToken() {
		return gmailAccessToken;
	}

	public void setGmailAccessToken(String gmailAccessToken) {
		this.gmailAccessToken = gmailAccessToken;
	}

	public String getLinkedInAccessToken() {
		return linkedInAccessToken;
	}

	public void setLinkedInAccessToken(String linkedInAccessToken) {
		this.linkedInAccessToken = linkedInAccessToken;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAdmin1() {
		return admin1;
	}

	public void setAdmin1(String admin1) {
		this.admin1 = admin1;
	}

	public String getAdmin2() {
		return admin2;
	}

	public void setAdmin2(String admin2) {
		this.admin2 = admin2;
	}

	public String getAdmin3() {
		return admin3;
	}

	public void setAdmin3(String admin3) {
		this.admin3 = admin3;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(Boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	public Boolean getAccept() {
		return accept;
	}

	public void setAccept(Boolean accept) {
		this.accept = accept;
	}

	public Boolean getShareInterest() {
		return shareInterest;
	}

	public void setShareInterest(Boolean shareInterest) {
		this.shareInterest = shareInterest;
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

	public Boolean getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Boolean getIsImport() {
		return isImport;
	}

	public void setIsImport(Boolean isImport) {
		this.isImport = isImport;
	}

	public List<ContactDetail> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactDetail> contacts) {
		this.contacts = contacts;
	}

	public Boolean getIsPayPalRemoved() {
		return isPayPalRemoved;
	}

	public void setIsPayPalRemoved(Boolean isPayPalRemoved) {
		this.isPayPalRemoved = isPayPalRemoved;
	}

	public Boolean getConnectViaWhosup() {
		return connectViaWhosup;
	}

	public void setConnectViaWhosup(Boolean connectViaWhosup) {
		this.connectViaWhosup = connectViaWhosup;
	}

	public Boolean getConnectViaEmailDomain() {
		return connectViaEmailDomain;
	}

	public void setConnectViaEmailDomain(Boolean connectViaEmailDomain) {
		this.connectViaEmailDomain = connectViaEmailDomain;
	}

	
}
