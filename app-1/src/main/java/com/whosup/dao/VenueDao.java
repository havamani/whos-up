package com.whosup.dao;

import java.util.UUID;

import com.whosup.model.Venue;

public interface VenueDao {

	Venue selectVenue(UUID venueId);

	void save(Venue venue);

}
