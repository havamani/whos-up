package com.whosup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "event_history_picture")
@JsonInclude(Include.NON_NULL)
public class EventHistoryPicture {

	@PrimaryKey
	@JsonIgnore
	private EventHistoryPictureKey pk;

	@Column(value = "posted_by_user_id")
	private UUID postedBy;

	@Column(value = "users_liked_by")
	private List<UUID> likedBy;

	@Column(value = "number_of_likes")
	private Integer numberOfLikes;
	
	@Column(value = "comment")
	private String comment;

	public EventHistoryPicture(){
		
	}

	public EventHistoryPicture(EventHistoryPictureKey pk, UUID postedBy) {
		this.pk = pk;
		this.postedBy = postedBy;
		this.likedBy = new ArrayList<UUID>();
		this.numberOfLikes = 0;
	}

	public EventHistoryPictureKey getPk() {
		return pk;
	}

	public void setPk(EventHistoryPictureKey pk) {
		this.pk = pk;
	}

	public UUID getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(UUID postedBy) {
		this.postedBy = postedBy;
	}

	public List<UUID> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(List<UUID> likedBy) {
		this.likedBy = likedBy;
	}

	public Integer getNumberOfLikes() {
		return numberOfLikes;
	}

	public void setNumberOfLikes(Integer numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
