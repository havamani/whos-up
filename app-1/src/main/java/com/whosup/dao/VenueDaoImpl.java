package com.whosup.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.Venue;

public class VenueDaoImpl implements VenueDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public Venue selectVenue(UUID venueId) {
		if (venueId == null)
			return null;
		Select select = QueryBuilder.select().from("venue");
		select.where(QueryBuilder.eq("venue_id", venueId));
		Venue venue = cassandraOperations.selectOne(select, Venue.class);
		return venue;
	}

	@Override
	public void save(Venue venue) {
		if (venue != null)
			cassandraOperations.insert(venue);
	}

}
