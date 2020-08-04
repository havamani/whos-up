package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.IOwe;
import com.whosup.model.OwesMe;

public interface OwesMeDao {

	void save(OwesMe owesMe);

	OwesMe selectByEventIdAndUserID(UUID eventId, UUID owingUserId);

	void update(OwesMe owesMe);

	void delete(OwesMe owesMe);

	void save(List<OwesMe> owesMe);

	List<OwesMe> selectOwesMe(UUID userId);

	List<OwesMe> selectPaymentOfEvent(UUID eventId);

	void delete(List<OwesMe> owesMe);

	OwesMe selectByEventAndUserAndOrganizer(IOwe owe);

	OwesMe selectByEventAndUserAndOrganizer(UUID organizerId, UUID eventId,
			UUID owingUserId);

}
