package com.whosup.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.EventHistoryPicture;

public class EventHistoryPictureDaoImpl implements EventHistoryPictureDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public List<EventHistoryPicture> findByEventId(UUID eventId) {
		if (eventId == null)
			return new ArrayList<EventHistoryPicture>();
		Select select = QueryBuilder.select().from("event_history_picture");
		select.where(QueryBuilder.eq("event_id", eventId)).orderBy(
				QueryBuilder.asc("posted_date"));
		List<EventHistoryPicture> eventPicture = cassandraOperations.select(
				select, EventHistoryPicture.class);
		return eventPicture;
	}

	@Override
	public UUID findLastUploadedUserByEventId(UUID eventId) {
		if (eventId == null)
			return null;
		Select select = QueryBuilder.select().from("event_history_picture");
		select.where(QueryBuilder.eq("event_id", eventId)).limit(1);
		List<EventHistoryPicture> eventPicture = cassandraOperations.select(
				select, EventHistoryPicture.class);
		if (eventPicture != null && eventPicture.size() == 1)
			return eventPicture.get(0).getPostedBy();
		else
			return null;
	}   

	@Override
	public EventHistoryPicture findByEventAndPictureDate(UUID eventId,
			Date pictureDate) {
		if (eventId == null || pictureDate == null)
			return null;
		Select select = QueryBuilder.select().from("event_history_picture")
				.allowFiltering();
		select.where(QueryBuilder.eq("event_id", eventId)).and(
				QueryBuilder.eq("posted_date", pictureDate));
		EventHistoryPicture eventPicture = cassandraOperations.selectOne(
				select, EventHistoryPicture.class);
		return eventPicture;
	}

	@Override
	public void save(EventHistoryPicture eventPicture) {
		if (eventPicture != null)
			cassandraOperations.insert(eventPicture);
	}
}
