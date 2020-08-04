package com.whosup.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserEventFeedback;

public class UserEventFeedbackDaoImpl implements UserEventFeedbackDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public UserEventFeedback selectUserFeedback(UUID eventId, UUID userId) {
		if (eventId == null || userId == null)
			return null;
		Select select = QueryBuilder.select().from("user_event_feedback");
		select.where(QueryBuilder.eq("event_id", eventId)).and(
				QueryBuilder.eq("user_id", userId));
		UserEventFeedback feedback = cassandraOperations.selectOne(select,
				UserEventFeedback.class);
		if (feedback != null)
			return feedback;
		else
			return null;
	}
	
	@Override
	public void save(UserEventFeedback feedback) {
		if (feedback != null)
			cassandraOperations.insert(feedback);
	}

	@Override
	public void update(UserEventFeedback feedback) {
		if (feedback != null)
			cassandraOperations.update(feedback);
	}
}
