package com.whosup.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_location_log")
@JsonInclude(Include.NON_NULL)
public class UserLocationLog {
	@PrimaryKey
	private UserLocationLogKey pk;

	@Column(value = "latlong")
	private String latLong;

	public UserLocationLog() {

	}

	public UserLocationLog(UserLocationLogKey pk, String latLong) {
		this.pk = pk;
		this.latLong = latLong;
	}

	public UserLocationLogKey getPk() {
		return pk;
	}

	public void setPk(UserLocationLogKey pk) {
		this.pk = pk;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

}
