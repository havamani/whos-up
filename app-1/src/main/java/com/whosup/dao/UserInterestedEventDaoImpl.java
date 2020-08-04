package com.whosup.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserInterestedEvent;

public class UserInterestedEventDaoImpl implements UserInterestedEventDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public List<UUID> findEventsByUserId(UUID userID) {
		if (userID == null)
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_interested_event");
		select.where(QueryBuilder.eq("user_id", userID));
		List<UserInterestedEvent> userEvents = cassandraOperations.select(
				select, UserInterestedEvent.class);
		List<UUID> interestedEvents = new ArrayList<UUID>();
		if (userEvents == null)
			return null;
		else {
			for (UserInterestedEvent user : userEvents)
				interestedEvents.add(user.getEventId());
		}
		return interestedEvents;
	}

	@Override
	public List<UUID> findEventsByUserIds(List<UUID> userIDs) {
		if (userIDs == null || userIDs.isEmpty())
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_interested_event");
		select.where(QueryBuilder.in("user_id", userIDs.toArray()));
		List<UserInterestedEvent> userEvents = cassandraOperations.select(
				select, UserInterestedEvent.class);
		Set<UUID> interestedEvents = new HashSet<UUID>();
		if (userEvents != null) {
			for (UserInterestedEvent user : userEvents)
				interestedEvents.add(user.getEventId());
		}
		return new ArrayList<UUID>(interestedEvents);
	}

	@Override
	public List<UserInterestedEvent> findByEventIds(List<UUID> eventIDs) {
		if (eventIDs == null || eventIDs.isEmpty())
			return new ArrayList<UserInterestedEvent>();
		Select select = QueryBuilder.select().from("user_interested_event")
				.allowFiltering();
		select.where(QueryBuilder.in("event_id", eventIDs.toArray()));
		List<UserInterestedEvent> userEvents = cassandraOperations.select(
				select, UserInterestedEvent.class);
		return userEvents;
	}

	@Override
	public UserInterestedEvent selectByEventAndUserId(UUID userID, UUID eventID) {
		if (userID == null || eventID == null)
			return null;
		Select select = QueryBuilder.select().from("user_interested_event");
		select.where(QueryBuilder.eq("user_id", userID)).and(
				QueryBuilder.eq("event_id", eventID));
		UserInterestedEvent uerInterestedEvent = cassandraOperations.selectOne(
				select, UserInterestedEvent.class);
		return uerInterestedEvent;
	}

	@Override
	public void save(UserInterestedEvent userEvent) {
		if (userEvent != null)
			cassandraOperations.insert(userEvent);
	}

	@Override
	public void delete(UserInterestedEvent userInterestedEvent) {
		if (userInterestedEvent != null)
			cassandraOperations.delete(userInterestedEvent);
	}

	@Override
	public void delete(List<UserInterestedEvent> userInterestedEvents) {
		if (userInterestedEvents != null && !userInterestedEvents.isEmpty())
			cassandraOperations.delete(userInterestedEvents);
	}

}
