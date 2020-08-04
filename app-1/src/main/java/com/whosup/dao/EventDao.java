package com.whosup.dao;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.whosup.model.Event;

public interface EventDao {

	List<Event> findPublicEventsByLocation(String location);

	List<Event> findEventsById(List<UUID> eventIds);

	void save(Event event);

	UUID update(Event event);

	Event selectEvent(UUID eventId);

	void update(List<Event> events);

	List<Event> findDomainEvents(String domain, UUID userId);

	UUID updateOrganizer(UUID eventID);

	List<Event> findByPublicSearch(String location);

	Set<Event> findBySearchEvents(UUID userId, String location);

	Set<Event> findDiscoverPublicEvents(UUID userId, String location,
			String Search);

	Set<Event> findDiscoverEvents(UUID userId, String search);

	List<Event> findStarredEvents(UUID userId, String search);

	List<Event> findEventsById(List<UUID> eventIds, String search);

	Set<Event> findBySearchPublicEvents(UUID userId, String location);

	List<Event> findEventsBySearchDate(String search, String searchDate);

	List<Event> findEventsBySearchDate(String searchDate);

	List<Event> findEventsBySearchDate(String search, String searchDate,
			String location, UUID userId);

	List<Event> findEventsBySearchDate(String searchDate, String location,
			UUID userId);

	List<Event> findEventsForUser(UUID userId);

	List<Event> findEventsForSearch(List<UUID> eventIds);

	List<String> findEventFromEventBrite(UUID userId);

	List<Event> findEventsAfterEvening(UUID userId, String location,
			String search, String searchDate);

	List<Event> findEventsAfterEvening(UUID userId, String location,
			String searchDate);

	List<Event> findEventsAfterEvening(String location, String search,
			String searchDate);

	List<Event> findEventsAfterEvening(String location, String searchDate);

	List<Event> findEventsBetWeenTimeInterval(UUID userId, String location,
			Long timeInMills);

	List<Event> findEventsBetWeenTimeInterval(String location, Long timeInMills);

	List<Event> findDeletedEventsBetWeenTimeInterval(UUID userId,
			String location, Long timeInMills);

	List<Event> findDeletedEventsBetWeenTimeInterval(String location,
			Long timeInMills);

}
