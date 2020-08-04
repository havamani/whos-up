package com.whosup.model;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "facility")
@JsonInclude(Include.NON_NULL)
public class Facility {

	@PrimaryKey(value = "facility_id")
	private UUID id;

	@Column(value = "facility_title")
	private String title;

	@Column(value = "facility_description")
	private String description;

	public Facility() {

	}

	public Facility(UUID id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
