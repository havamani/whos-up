package com.whosup.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "facility_venue")
@JsonInclude(Include.NON_NULL)
public class FacilityVenue {

	@PrimaryKey
	private FacilityVenueKey pk;

	public FacilityVenue() {

	}

	public FacilityVenue(FacilityVenueKey pk) {
		this.pk = pk;
	}

	public FacilityVenueKey getPk() {
		return pk;
	}

	public void setPk(FacilityVenueKey pk) {
		this.pk = pk;
	}

}
