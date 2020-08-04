package com.whosup.service;

import java.util.List;
import java.util.UUID;

import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.request.DiscoverRequest;
import com.whosup.model.response.DiscoverSource;
import com.whosup.model.response.EventViewResponse;
import com.whosup.model.response.Status;

public interface DiscoverService {

	DiscoverSource eventList(String location);

	DiscoverSource eventList(User user, String location);

	Status createEvent(DiscoverRequest request, User user);

	List<UserContact> gatherFrom(DiscoverRequest request, User user);

	EventViewResponse viewEvent(UUID eventId);

	Status buzzEvent(UUID eventId, UUID userId);

	EventViewResponse viewEventPublic(UUID eventId);

	DiscoverSource searchDiscover(String location, String search,
			String searchDate, String searchAfterEve);

	DiscoverSource searchDiscover(UUID userId, String location, String search,
			String searchDate, String searchAfterEve);

	Status publicKeyword(String location);

	Status privateKeyword(UUID userId, String location);

	Status friendsCount(UUID ideaId, UUID userId);

	DiscoverSource eventList(UUID userId, String location, Long timeInMills);

	DiscoverSource eventList(String location, Long timeInMills);

}
