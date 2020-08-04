package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserEventInbox;

public class UserEventInboxDaoImpl implements UserEventInboxDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public List<UserEventInbox> findUsersByEventId(UUID eventId) {
		if (eventId == null)
			return new ArrayList<UserEventInbox>();
		Select select = QueryBuilder.select().from("user_my_events_inbox")
				.allowFiltering();
		select.where(QueryBuilder.eq("event_id", eventId));
		List<UserEventInbox> eventInbox = cassandraOperations.select(select,
				UserEventInbox.class);
		return eventInbox;
	}

	@Override
	public List<UserEventInbox> findUsersByEventIds(List<UUID> eventIds) {
		if (eventIds == null || eventIds.isEmpty())
			return new ArrayList<UserEventInbox>();
		Select select = QueryBuilder.select().from("user_my_events_inbox")
				.allowFiltering();
		select.where(QueryBuilder.in("event_id", eventIds.toArray()));
		List<UserEventInbox> eventInbox = cassandraOperations.select(select,
				UserEventInbox.class);
		return eventInbox;
	}

	@Override
	public List<UserEventInbox> findByUserIdAndEventIds(UUID userId,
			List<UUID> eventIds) {
		if (eventIds == null || eventIds.isEmpty() || userId == null)
			return new ArrayList<UserEventInbox>();
		Select select = QueryBuilder.select().from("user_my_events_inbox")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.in("event_id", eventIds.toArray()));
		List<UserEventInbox> eventInbox = cassandraOperations.select(select,
				UserEventInbox.class);
		return eventInbox;
	}

	@Override
	public List<UUID> findChatNotOpenedEventsByUserId(UUID userId) {
		if (userId == null)
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_my_events_inbox")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId));
		List<UserEventInbox> eventInbox = cassandraOperations.select(select,
				UserEventInbox.class);
		List<UUID> eventIds = new ArrayList<UUID>();
		if (eventInbox != null)
			for (UserEventInbox e : eventInbox) {
				if (!e.getIsChat())
					eventIds.add(e.getPk().getEventID());
			}
		return eventIds;
	}

	@Override
	public List<UUID> findConfirmedUsers(UUID eventId) {
		if (eventId == null)
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_my_events_inbox")
				.allowFiltering();
		select.where(QueryBuilder.eq("event_id", eventId));
		List<UserEventInbox> eventInbox = cassandraOperations.select(select,
				UserEventInbox.class);
		List<UUID> userIds = new ArrayList<UUID>();
		if (eventInbox != null)
			for (UserEventInbox e : eventInbox) {
				if (e.getIsUserConfirmed())
					userIds.add(e.getPk().getUserID());
			}
		return userIds;
	}

	@Override
	public List<UUID> findUnConfirmedUsers(UUID eventId) {
		if (eventId == null)
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_my_events_inbox")
				.allowFiltering();
		select.where(QueryBuilder.eq("event_id", eventId));
		List<UserEventInbox> eventInbox = cassandraOperations.select(select,
				UserEventInbox.class);
		List<UUID> userIds = new ArrayList<UUID>();
		if (eventInbox != null)
			for (UserEventInbox e : eventInbox) {
				if (!e.getIsUserConfirmed())
					userIds.add(e.getPk().getUserID());
			}
		return userIds;
	}

	@Override
	public UserEventInbox selectEventByUserIdAndEventId(UUID userID,
			UUID eventId) {
		if (eventId == null || userID == null)
			return null;
		Select select = QueryBuilder.select().from("user_my_events_inbox");
		select.where(QueryBuilder.eq("user_id", userID)).and(
				QueryBuilder.eq("event_id", eventId));
		UserEventInbox eventInbox = cassandraOperations.selectOne(select,
				UserEventInbox.class);
		return eventInbox;
	}

	@Override
	public List<UserEventInbox> selectEventByUserIdsAndEventId(
			List<UUID> userIDs, UUID eventId) {
		if (userIDs == null || userIDs.isEmpty() || eventId == null)
			return new ArrayList<UserEventInbox>();
		Select select = QueryBuilder.select().from("user_my_events_inbox");
		select.where(QueryBuilder.in("user_id", userIDs.toArray())).and(
				QueryBuilder.eq("event_id", eventId));
		List<UserEventInbox> eventsInbox = cassandraOperations.select(select,
				UserEventInbox.class);
		return eventsInbox;
	}

	@Override
	public List<UUID> findConfirmedAndNotHistoryEventsbyUserId(UUID userID) {
		if (userID == null)
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_my_events_inbox")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userID));
		List<UserEventInbox> eventInboxList = cassandraOperations.select(
				select, UserEventInbox.class);
		List<UUID> confirmedEventIds = new ArrayList<UUID>();
		if (eventInboxList != null) {
			for (UserEventInbox confirmedEvent : eventInboxList)
				if (confirmedEvent.getIsEventConfirmed() != null
						&& confirmedEvent.getIsEventConfirmed()
						&& !confirmedEvent.getIsHistory())
					confirmedEventIds.add(confirmedEvent.getPk().getEventID());
		}

		return confirmedEventIds;
	}

	@Override
	public List<UUID> findChatOpened(UUID userID) {
		if (userID == null)
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_my_events_inbox")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userID));
		List<UserEventInbox> eventInboxList = cassandraOperations.select(
				select, UserEventInbox.class);
		List<UUID> chatOpenedEventIds = new ArrayList<UUID>();
		if (eventInboxList != null) {
			for (UserEventInbox c : eventInboxList)
				if (c.getIsChat())
					chatOpenedEventIds.add(c.getPk().getEventID());
		}

		return chatOpenedEventIds;
	}

	@Override
	public void save(UserEventInbox eventInbox) {
		if (eventInbox != null)
			cassandraOperations.insert(eventInbox);
	}

	@Override
	public void update(List<UserEventInbox> userEventInbox) {
		if (userEventInbox != null && !userEventInbox.isEmpty())
			cassandraOperations.update(userEventInbox);
	}

	@Override
	public void update(UserEventInbox userEventInbox) {
		if (userEventInbox != null)
			cassandraOperations.update(userEventInbox);
	}

	@Override
	public void delete(UserEventInbox userEventInbox) {
		if (userEventInbox != null)
			cassandraOperations.delete(userEventInbox);
	}

	@Override
	public void delete(List<UserEventInbox> userEventInboxs) {
		if (userEventInboxs != null && !userEventInboxs.isEmpty())
			cassandraOperations.delete(userEventInboxs);
	}

}
