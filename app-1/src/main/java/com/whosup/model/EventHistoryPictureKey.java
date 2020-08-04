package com.whosup.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class EventHistoryPictureKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "event_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID eventID;

	@PrimaryKeyColumn(name = "posted_date", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private Date postedDate;

	@PrimaryKeyColumn(name = "image_path", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
	private String imagePath;

	public EventHistoryPictureKey() {

	}

	public EventHistoryPictureKey(UUID eventID, Date postedDate,
			String imagePath) {
		this.eventID = eventID;
		this.postedDate = postedDate;
		this.imagePath = imagePath;
	}

	public UUID getEventID() {
		return eventID;
	}

	public void setEventID(UUID eventID) {
		this.eventID = eventID;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventID == null) ? 0 : eventID.hashCode());
		result = prime * result
				+ ((imagePath == null) ? 0 : imagePath.hashCode());
		result = prime * result
				+ ((postedDate == null) ? 0 : postedDate.hashCode());
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
		EventHistoryPictureKey other = (EventHistoryPictureKey) obj;
		if (eventID == null) {
			if (other.eventID != null)
				return false;
		} else if (!eventID.equals(other.eventID))
			return false;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath))
			return false;
		if (postedDate == null) {
			if (other.postedDate != null)
				return false;
		} else if (!postedDate.equals(other.postedDate))
			return false;
		return true;
	}

}
