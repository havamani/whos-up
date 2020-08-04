package com.whosup.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.whosup.model.EventHistoryPicture;

public interface EventHistoryPictureDao {

	void save(EventHistoryPicture eventPicture);

	EventHistoryPicture findByEventAndPictureDate(UUID eventId, Date pictureDate);

	List<EventHistoryPicture> findByEventId(UUID eventId);

	UUID findLastUploadedUserByEventId(UUID eventId);

}
