package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserEventInbox;

public interface UserEventInboxDao {

	void save(UserEventInbox eventInbox);

	UserEventInbox selectEventByUserIdAndEventId(UUID userID, UUID eventId);

	void update(UserEventInbox userEventInbox);

	void delete(UserEventInbox userEventInbox);

	List<UserEventInbox> findUsersByEventId(UUID eventId);

	List<UUID> findChatNotOpenedEventsByUserId(UUID userId);

	List<UUID> findConfirmedAndNotHistoryEventsbyUserId(UUID userID);

	void update(List<UserEventInbox> userEventInbox);

	List<UserEventInbox> selectEventByUserIdsAndEventId(List<UUID> userIDs,
			UUID eventId);

	List<UserEventInbox> findByUserIdAndEventIds(UUID userId,
			List<UUID> eventIds);

	List<UUID> findConfirmedUsers(UUID eventId);

	List<UUID> findUnConfirmedUsers(UUID eventId);

	List<UserEventInbox> findUsersByEventIds(List<UUID> eventIds);

	List<UUID> findChatOpened(UUID userID);

	void delete(List<UserEventInbox> userEventInboxs);
}
