package com.whosup.model.response;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PictureList {

	private String picturePath;
	private String name;
	private List<UUID> likeBy;
	private String uploadedBy;
	private Integer noOfLikes;
	private Boolean isLiked;
	private Date pictureDate;
	private String comment;

	public PictureList() {

	}

	public PictureList(String picturePath, String name, List<UUID> likeBy,
			String uploadedBy, Date pictureDate, Integer noOfLikes, String comment) {
		super();
		this.picturePath = picturePath;
		this.name = name;
		this.likeBy = likeBy;
		this.uploadedBy = uploadedBy;
		this.pictureDate = pictureDate;
		this.noOfLikes = noOfLikes;
		this.isLiked = false;
		this.comment = comment;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UUID> getLikeBy() {
		return likeBy;
	}

	public void setLikeBy(List<UUID> likeBy) {
		this.likeBy = likeBy;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Integer getNoOfLikes() {
		return noOfLikes;
	}

	public void setNoOfLikes(Integer noOfLikes) {
		this.noOfLikes = noOfLikes;
	}

	public Boolean getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(Boolean isLiked) {
		this.isLiked = isLiked;
	}

	public Date getPictureDate() {
		return pictureDate;
	}

	public void setPictureDate(Date pictureDate) {
		this.pictureDate = pictureDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	

}
