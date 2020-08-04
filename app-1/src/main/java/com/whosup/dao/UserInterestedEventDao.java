package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserInterestedEvent;

public interface UserInterestedEventDao {

	List<UUID> findEventsByUserId(UUID userID);

	void save(UserInterestedEvent userEvent);

	UserInterestedEvent selectByEventAndUserId(UUID userID, UUID eventID);

	void delete(UserInterestedEvent userInterestedEvent);

	List<UUID> findEventsByUserIds(List<UUID> userIDs);

	List<UserInterestedEvent> findByEventIds(List<UUID> eventIDs);

	void delete(List<UserInterestedEvent> userInterestedEvents);
}
