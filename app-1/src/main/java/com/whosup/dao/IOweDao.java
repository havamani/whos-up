package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.IOwe;

public interface IOweDao {

	void save(IOwe iOwe);

	void save(List<IOwe> iOwe);

	void update(IOwe iOwe);

	List<IOwe> findByUserId(UUID userId);

	void delete(UUID userId, UUID eventId);

	void delete(UUID eventId);

	IOwe findByUserIdAndEventId(UUID userId, UUID eventId);

}
