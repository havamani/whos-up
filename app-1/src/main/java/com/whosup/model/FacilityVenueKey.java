package com.whosup.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

@PrimaryKeyClass
public class FacilityVenueKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@PrimaryKeyColumn(name = "facility_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID facilityId;
	@PrimaryKeyColumn(name = "latlong", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String latLong;
	@PrimaryKeyColumn(name = "venue_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
	private UUID venueId;

	public FacilityVenueKey() {

	}

	public FacilityVenueKey(UUID facilityId, String latLong, UUID venueId) {
		this.facilityId = facilityId;
		this.latLong = latLong;
		this.venueId = venueId;
	}

	public UUID getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(UUID facilityId) {
		this.facilityId = facilityId;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public UUID getVenueId() {
		return venueId;
	}

	public void setVenueId(UUID venueId) {
		this.venueId = venueId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((facilityId == null) ? 0 : facilityId.hashCode());
		result = prime * result + ((latLong == null) ? 0 : latLong.hashCode());
		result = prime * result + ((venueId == null) ? 0 : venueId.hashCode());
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
		FacilityVenueKey other = (FacilityVenueKey) obj;
		if (facilityId == null) {
			if (other.facilityId != null)
				return false;
		} else if (!facilityId.equals(other.facilityId))
			return false;
		if (latLong == null) {
			if (other.latLong != null)
				return false;
		} else if (!latLong.equals(other.latLong))
			return false;
		if (venueId == null) {
			if (other.venueId != null)
				return false;
		} else if (!venueId.equals(other.venueId))
			return false;
		return true;
	}

}
