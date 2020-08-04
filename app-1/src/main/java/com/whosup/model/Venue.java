package com.whosup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "venue")
public class Venue extends Model {

	@PrimaryKey(value = "venue_id")
	private UUID venueID;

	@Column(value = "venue_name")
	private String venueName;

	@Column(value = "venue_description")
	private String venueDescription;

	private String address;

	@Column(value = "location_latlong")
	private String locationLatLong;

	@Column(value = "information_source")
	private String informationSource;

	@Column(value = "facility_ids")
	private List<UUID> facilityIDs;

	public Venue() {

	}

	public Venue(UUID venueID, String venueName, String venueDescription) {
		this.venueID = venueID;
		this.venueName = venueName;
		this.venueDescription = venueDescription;
		this.facilityIDs = new ArrayList<UUID>();
		this.address = "";
		this.locationLatLong = "";
		this.informationSource = "";
	}

	public UUID getVenueID() {
		return venueID;
	}

	public void setVenueId(UUID venueID) {
		this.venueID = venueID;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getVenueDescription() {
		return venueDescription;
	}

	public void setVenueDescription(String venueDescription) {
		this.venueDescription = venueDescription;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocationLatLong() {
		return locationLatLong;
	}

	public void setLocationLatLong(String locationLatLong) {
		this.locationLatLong = locationLatLong;
	}

	public String getInformationSource() {
		return informationSource;
	}

	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}

	public List<UUID> getFacilityIDs() {
		return facilityIDs;
	}

	public void setFacilityIDs(List<UUID> facilityIDs) {
		this.facilityIDs = facilityIDs;
	}

	@Override
	public String toString() {
		return "Venue [venueID=" + venueID + ", venueName=" + venueName
				+ ", venueDescription=" + venueDescription + ", address="
				+ address + "]";
	}

}
