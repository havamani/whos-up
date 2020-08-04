package com.whosup.model.response;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.whosup.model.Idea;

@JsonInclude(Include.NON_NULL)
public class Status {

	private String status;
	private String stripeAccount;
	private String path;
	private String layerToken;
	private Idea idea;
	private Long facebookCount;
	private Long networkCount;
	private Long whosupCount;
	private Long facebookStarredCount;
	private Long networkStarredCount;
	private Long whosupStarredCount;
	private UUID eventID;
	private Integer rating;
	private List<PictureList> pictureList;
	private List<String> keywords;
	private List<String> frequentSearch;
	private List<String> popularSearch;

	public Status() {
	}

	public Status(Long facebookCount) {
		this.facebookCount = facebookCount;
		this.networkCount = 0L;
		this.whosupCount = 0L;
		this.facebookStarredCount = 0L;
		this.networkStarredCount = 0L;
		this.whosupStarredCount = 0L;
	}

	public Status(String status) {
		this.status = status;
	}

	public List<String> getFrequentSearch() {
		return frequentSearch;
	}

	public void setFrequentSearch(List<String> frequentSearch) {
		this.frequentSearch = frequentSearch;
	}

	public List<String> getPopularSearch() {
		return popularSearch;
	}

	public void setPopularSearch(List<String> popularSearch) {
		this.popularSearch = popularSearch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStripeAccount() {
		return stripeAccount;
	}

	public void setStripeAccount(String stripeAccount) {
		this.stripeAccount = stripeAccount;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLayerToken() {
		return layerToken;
	}

	public void setLayerToken(String layerToken) {
		this.layerToken = layerToken;
	}

	public Idea getIdea() {
		return idea;
	}

	public void setIdea(Idea idea) {
		this.idea = idea;
	}

	public Long getFacebookCount() {
		return facebookCount;
	}

	public void setFacebookCount(Long facebookCount) {
		this.facebookCount = facebookCount;
	}

	public Long getNetworkCount() {
		return networkCount;
	}

	public void setNetworkCount(Long networkCount) {
		this.networkCount = networkCount;
	}

	public Long getWhosupCount() {
		return whosupCount;
	}

	public void setWhosupCount(Long whosupCount) {
		this.whosupCount = whosupCount;
	}

	public Long getFacebookStarredCount() {
		return facebookStarredCount;
	}

	public void setFacebookStarredCount(Long facebookStarredCount) {
		this.facebookStarredCount = facebookStarredCount;
	}

	public Long getNetworkStarredCount() {
		return networkStarredCount;
	}

	public void setNetworkStarredCount(Long networkStarredCount) {
		this.networkStarredCount = networkStarredCount;
	}

	public Long getWhosupStarredCount() {
		return whosupStarredCount;
	}

	public void setWhosupStarredCount(Long whosupStarredCount) {
		this.whosupStarredCount = whosupStarredCount;
	}

	public UUID getEventID() {
		return eventID;
	}

	public void setEventID(UUID eventID) {
		this.eventID = eventID;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public List<PictureList> getPictureList() {
		return pictureList;
	}

	public void setPictureList(List<PictureList> pictureList) {
		this.pictureList = pictureList;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

}
