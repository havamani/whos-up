package com.whosup.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_event_feedback")
@JsonInclude(Include.NON_NULL)
public class UserEventFeedback {

	@PrimaryKey
	private UserEventFeedbackKey pk;

	@Column(value = "star_1_to_5")
	private Integer rating;

	@Column(value = "main_issue")
	private String issue;

	public UserEventFeedback() {

	}

	public UserEventFeedback(UserEventFeedbackKey pk, Integer rating,
			String issue) {
		this.pk = pk;
		this.rating = rating;
		this.issue = issue;
	}

	public UserEventFeedbackKey getPk() {
		return pk;
	}

	public void setPk(UserEventFeedbackKey pk) {
		this.pk = pk;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

}
