package com.whosup.dao;

import java.util.UUID;

import com.whosup.model.UserEventFeedback;

public interface UserEventFeedbackDao {

	void save(UserEventFeedback feedback);

	UserEventFeedback selectUserFeedback(UUID eventId, UUID userId);

	void update(UserEventFeedback feedback);

}
