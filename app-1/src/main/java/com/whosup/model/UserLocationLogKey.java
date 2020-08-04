package com.whosup.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class UserLocationLogKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID userID;

	@PrimaryKeyColumn(name = "date_time_at_loc_utc", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private Date dateTimeAtLocUtc;

	public UserLocationLogKey() {
	}

	public UserLocationLogKey(UUID userID, Date dateTimeAtLocUtc) {
		this.userID = userID;
		this.dateTimeAtLocUtc = dateTimeAtLocUtc;
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public Date getDateTimeAtLocUtc() {
		return dateTimeAtLocUtc;
	}

	public void setDateTimeAtLocUtc(Date dateTimeAtLocUtc) {
		this.dateTimeAtLocUtc = dateTimeAtLocUtc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dateTimeAtLocUtc == null) ? 0 : dateTimeAtLocUtc.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserLocationLogKey other = (UserLocationLogKey) obj;
		if (dateTimeAtLocUtc == null) {
			if (other.dateTimeAtLocUtc != null)
				return false;
		} else if (!dateTimeAtLocUtc.equals(other.dateTimeAtLocUtc))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}

}
