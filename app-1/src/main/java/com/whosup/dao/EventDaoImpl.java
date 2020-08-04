package com.whosup.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.Event;
import com.whosup.model.User;

public class EventDaoImpl implements EventDao {

	@Autowired
	private UserInterestedEventDao userInterestedEventDao;

	@Autowired
	private CassandraOperations cassandraOperations;

	@Autowired
	@Qualifier("cassandra")
	private Session session;

	@Override
	public List<Event> findPublicEventsByLocation(String location) {
		JSONObject json = new JSONObject();
		json.put("q", "is_public:true");
		json.put("fq", "{!geofilt sfield=gather_advanced_loc_latlong pt="
				+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		return events;
	}

	@Override
	public List<Event> findEventsById(List<UUID> eventIds) {
		if (eventIds == null || eventIds.isEmpty())
			return new ArrayList<Event>();
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.in("event_id", eventIds.toArray()));
		List<Event> event = cassandraOperations.select(select, Event.class);
		return event;
	}

	@Override
	public List<Event> findEventsForSearch(List<UUID> eventIds) {
		if (eventIds == null || eventIds.isEmpty())
			return new ArrayList<Event>();
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.in("event_id", eventIds.toArray()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		List<Event> eventList = new ArrayList<Event>();
		for (Event e : events) {
			if (e.getEventStartTime() == null)
				eventList.add(e);
			else {
				if (e.getEventStartTime().after(new Date()))
					eventList.add(e);
			}
		}
		return eventList;
	}

	@Override
	public List<Event> findEventsById(List<UUID> eventIds, String search) {
		if (eventIds == null || eventIds.isEmpty())
			return new ArrayList<Event>();
		JSONObject json = new JSONObject();
		json.put("q", "event_id: ("
				+ eventIds.toString().replace(",", " ").replace("[", "")
						.replace("]", "") + ") AND (title:(" + search
				+ "~0.8) keywords:(" + search + "~0.8) description:(" + search
				+ "~0.8))");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> event = cassandraOperations.select(select, Event.class);
		return event;
	}

	@Override
	public Set<Event> findDiscoverEvents(UUID userId, String search) {
		JSONObject json = new JSONObject();
		if (search != null)
			json.put("q", "(visible_to_user_ids:" + userId
					+ " NOT interested_user_ids:" + userId
					+ ") AND (keywords:(" + search + "~0.8) description:("
					+ search + "~0.8) title:(" + search + "~0.8))");
		else
			json.put("q", "(visible_to_user_ids:" + userId
					+ " NOT interested_user_ids:" + userId + ")");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return new HashSet<Event>(events);
		else
			return new HashSet<Event>();
	}

	@Override
	public List<Event> findDomainEvents(String domain, UUID userId) {
		if (domain == null)
			return new ArrayList<Event>();
		JSONObject json = new JSONObject();
		json.put("q", "(verified_domain_email:" + domain + "$)");
		Select select = QueryBuilder.select().from("user");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<User> users = cassandraOperations.select(select, User.class);
		List<UUID> eventIds = new ArrayList<UUID>();
		for (User u : users) {
			eventIds = userInterestedEventDao.findEventsByUserId(u.getUserID());
		}
		List<Event> events = findEventsById(eventIds);
		if (events != null && events.size() > 0) {
			List<Event> filteredEvents = new ArrayList<Event>();
			for (Event e : events) {
				if (!e.getInterestedUserIDs().contains(userId)
						&& (e.getVisibleToUserIDs().contains(userId) || e
								.getIsPublic())) {
					filteredEvents.add(e);
				}
			}
			return filteredEvents;
		} else
			return new ArrayList<Event>();
	}

	@Override
	public List<Event> findStarredEvents(UUID userId, String search) {
		if (userId == null)
			return new ArrayList<Event>();
		Select select = QueryBuilder.select().from(
				"final_starred_friends_event_ranking");
		select.where(QueryBuilder.eq("user_id", userId));
		List<Row> starredEvent = session.execute(select).all();
		List<UUID> starredEventIds = new ArrayList<UUID>();
		for (Row r : starredEvent) {
			starredEventIds.add(r.getUUID(1));
		}
		List<Event> eventFromStarred = new ArrayList<Event>();
		if (search != null)
			eventFromStarred = findEventsById(starredEventIds, search);
		else
			eventFromStarred = findEventsById(starredEventIds);
		if (eventFromStarred == null || eventFromStarred.isEmpty())
			return new ArrayList<Event>();
		return eventFromStarred;
	}

	@Override
	public Set<Event> findDiscoverPublicEvents(UUID userId, String location,
			String search) {
		JSONObject json = new JSONObject();
		if (search != null) {
			json.put("q", "(is_public:true NOT interested_user_ids:" + userId
					+ ") AND (keywords:(" + search + "~0.8) description:("
					+ search + "~0.8) title:(" + search + "~0.8))");
		} else {
			json.put("q", "(is_public:true NOT interested_user_ids:" + userId
					+ ")");
		}
		json.put("fq", "{!geofilt sfield=gather_advanced_loc_latlong pt="
				+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return new HashSet<Event>(events);
		else
			return new HashSet<Event>();
	}

	@Override
	public List<Event> findByPublicSearch(String location) {
		JSONObject json = new JSONObject();
		json.put("q", "is_public:true");
		json.put("fq", "{!geofilt sfield=gather_advanced_loc_latlong pt="
				+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		return events;
	}

	@Override
	public Set<Event> findBySearchEvents(UUID userId, String location) {
		JSONObject json = new JSONObject();
		json.put("q", "visible_to_user_ids:" + userId
				+ " NOT interested_user_ids:" + userId);
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return new HashSet<Event>(events);
		else
			return new HashSet<Event>();
	}

	@Override
	public Set<Event> findBySearchPublicEvents(UUID userId, String location) {
		JSONObject json = new JSONObject();
		json.put("q", "is_public:true NOT interested_user_ids:" + userId);
		json.put("fq", "{!geofilt sfield=gather_advanced_loc_latlong pt="
				+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return new HashSet<Event>(events);
		else
			return new HashSet<Event>();
	}

	@Override
	public Event selectEvent(UUID eventId) {
		if (eventId == null)
			return null;
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("event_id", eventId));
		Event event = cassandraOperations.selectOne(select, Event.class);
		return event;
	}

	@Override
	public void save(Event event) {
		if (event != null)
			cassandraOperations.insert(event);
	}

	@Override
	public UUID updateOrganizer(UUID eventID) {
		if (eventID == null)
			return null;
		String cql = "UPDATE event SET organizer_user_id = null WHERE event_id ="
				+ eventID;
		cassandraOperations.execute(cql);
		return eventID;
	}

	@Override
	public UUID update(Event event) {
		if (event == null)
			return null;
		cassandraOperations.update(event);
		return event.getEventID();
	}

	@Override
	public void update(List<Event> events) {
		if (events != null && events.size() > 0)
			cassandraOperations.update(events);
	}

	@Override
	public List<Event> findEventsBySearchDate(String search, String searchDate,
			String location, UUID userId) {
		return getEventsByDate(search, searchDate, location, userId);
	}

	@Override
	public List<Event> findEventsBySearchDate(String search, String searchDate) {
		return getEventsByDate(searchDate, search);
	}

	@Override
	public List<Event> findEventsForUser(UUID userId) {
		JSONObject json = new JSONObject();
		json.put("q",
				"has_organizer_confirmed:true AND ((is_public:true NOT interested_user_ids:"
						+ userId + ") OR (visible_to_user_ids:" + userId
						+ " NOT interested_user_ids:" + userId + "))");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return events;
		else
			return null;
	}

	@Override
	public List<String> findEventFromEventBrite(UUID userID) {
		JSONObject json = new JSONObject();
		json.put("q", "interested_user_ids:" + userID
				+ " AND web_link:www.eventbrite.co*");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> eventFromEventBrite = cassandraOperations.select(select,
				Event.class);
		List<String> eventBriteWebLinks = new ArrayList<String>();
		if (eventFromEventBrite != null)
			for (Event e : eventFromEventBrite)
				eventBriteWebLinks.add(e.getWebLink());
		return eventBriteWebLinks;
	}

	private List<Event> getEventsByDate(String search, String searchDate,
			String location, UUID userId) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		JSONObject json = new JSONObject();
		json.put("q",
				"has_organizer_confirmed:true AND ((is_public:true NOT interested_user_ids:"
						+ userId + ") OR (visible_to_user_ids:" + userId
						+ " NOT interested_user_ids:" + userId
						+ ")) AND (keywords:(" + search + "~0.8) description:("
						+ search + "~0.8) title:(" + search + "~0.8))");
		json.put(
				"fq",
				"event_date:["
						+ cal.get(Calendar.YEAR)
						+ "-"
						+ (cal.get(Calendar.MONTH) + 1)
						+ "-"
						+ cal.get(Calendar.DATE)
						+ "T00:00:00Z TO "
						+ searchDate
						+ "T00:00:00Z] OR {!geofilt sfield=chosen_location_latlong pt="
						+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return filterEvents(events);
		else
			return new ArrayList<Event>();
	}

	private List<Event> getEventsByDate(String searchDate, String search) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		List<Event> events = new ArrayList<Event>();
		JSONObject json = new JSONObject();
		json.put("q", "has_organizer_confirmed:true AND (keywords:(" + search
				+ "~0.8) description:(" + search + "~0.8) title:(" + search
				+ "~0.8))");
		json.put(
				"fq",
				"event_date:[" + cal.get(Calendar.YEAR) + "-"
						+ (cal.get(Calendar.MONTH) + 1) + "-"
						+ cal.get(Calendar.DATE) + "T00:00:00Z TO "
						+ searchDate + "T00:00:00Z]");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return filterEvents(events);
		else
			return new ArrayList<Event>();
	}

	private List<Event> filterEvents(List<Event> events) {
		List<Event> filteredEvents = new ArrayList<Event>();
		for (Event e : events) {
			if (!e.getIsDeleted() && e.getEventStartTime().after(new Date()))
				filteredEvents.add(e);
		}
		return filteredEvents;
	}

	@Override
	public List<Event> findEventsBySearchDate(String searchDate,
			String location, UUID userId) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		JSONObject json = new JSONObject();
		json.put("q",
				"has_organizer_confirmed:true AND ((is_public:true NOT interested_user_ids:"
						+ userId + ") OR (visible_to_user_ids:" + userId
						+ " NOT interested_user_ids:" + userId + ")) ");
		json.put(
				"fq",
				"event_date:["
						+ cal.get(Calendar.YEAR)
						+ "-"
						+ (cal.get(Calendar.MONTH) + 1)
						+ "-"
						+ cal.get(Calendar.DATE)
						+ "T00:00:00Z TO "
						+ searchDate
						+ "T00:00:00Z] OR {!geofilt sfield=chosen_location_latlong pt="
						+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return filterEvents(events);
		else
			return new ArrayList<Event>();

	}

	@Override
	public List<Event> findEventsBySearchDate(String searchDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		List<Event> events = new ArrayList<Event>();
		JSONObject json = new JSONObject();
		json.put("q", "has_organizer_confirmed:true");
		json.put(
				"fq",
				"event_date:[" + cal.get(Calendar.YEAR) + "-"
						+ (cal.get(Calendar.MONTH) + 1) + "-"
						+ cal.get(Calendar.DATE) + "T00:00:00Z TO "
						+ searchDate + "T00:00:00Z]");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return filterEvents(events);
		else
			return new ArrayList<Event>();

	}

	@Override
	public List<Event> findEventsAfterEvening(UUID userId, String location,
			String search, String searchDate) {
		JSONObject json = new JSONObject();
		json.put("q",
				"has_organizer_confirmed:true AND ((is_public:true NOT interested_user_ids:"
						+ userId + ") OR (visible_to_user_ids:" + userId
						+ " NOT interested_user_ids:" + userId
						+ ")) AND (keywords:(" + search + "~0.8) description:("
						+ search + "~0.8) title:(" + search + "~0.8))");
		json.put(
				"fq",
				"event_start_time:["
						+ searchDate
						+ "T17:00:00Z TO "
						+ searchDate
						+ "T23:59:59Z] AND {!geofilt sfield=chosen_location_latlong pt="
						+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return filterEvents(events);
		else
			return new ArrayList<Event>();
	}

	@Override
	public List<Event> findEventsAfterEvening(UUID userId, String location,
			String searchDate) {
		JSONObject json = new JSONObject();
		json.put("q",
				"has_organizer_confirmed:true AND ((is_public:true NOT interested_user_ids:"
						+ userId + ") OR (visible_to_user_ids:" + userId
						+ " NOT interested_user_ids:" + userId + "))");
		json.put(
				"fq",
				"event_start_time:["
						+ searchDate
						+ "T17:00:00Z TO "
						+ searchDate
						+ "T23:59:59Z] AND {!geofilt sfield=chosen_location_latlong pt="
						+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return filterEvents(events);
		else
			return new ArrayList<Event>();
	}

	@Override
	public List<Event> findEventsAfterEvening(String location, String search,
			String searchDate) {
		JSONObject json = new JSONObject();
		json.put("q", "has_organizer_confirmed:true AND (keywords:(" + search
				+ "~0.8) description:(" + search + "~0.8) title:(" + search
				+ "~0.8))");
		json.put(
				"fq",
				"event_start_time:["
						+ searchDate
						+ "T17:00:00Z TO "
						+ searchDate
						+ "T23:59:59Z] AND {!geofilt sfield=chosen_location_latlong pt="
						+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return filterEvents(events);
		else
			return new ArrayList<Event>();
	}

	@Override
	public List<Event> findEventsAfterEvening(String location, String searchDate) {
		JSONObject json = new JSONObject();
		json.put("q", "has_organizer_confirmed:true");
		json.put(
				"fq",
				"event_start_time:["
						+ searchDate
						+ "T17:00:00Z TO "
						+ searchDate
						+ "T23:59:59Z] AND {!geofilt sfield=chosen_location_latlong pt="
						+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return filterEvents(events);
		else
			return new ArrayList<Event>();
	}

	@Override
	public List<Event> findDeletedEventsBetWeenTimeInterval(UUID userId,
			String location, Long timeInMills) {
		String q = "(visible_to_user_ids:" + userId
				+ " NOT interested_user_ids:" + userId
				+ ") AND is_deleted:true";
		return commonForFindEventsBetWeenTimeInterval(userId, location,
				timeInMills, q);
	}

	@Override
	public List<Event> findEventsBetWeenTimeInterval(UUID userId,
			String location, Long timeInMills) {
		String q = "(visible_to_user_ids:" + userId
				+ " NOT interested_user_ids:" + userId + ")";
		return commonForFindEventsBetWeenTimeInterval(userId, location,
				timeInMills, q);
	}

	private List<Event> commonForFindEventsBetWeenTimeInterval(UUID userId,
			String location, Long timeInMills, String q) {

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTimeInMillis(timeInMills);

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		JSONObject json = new JSONObject();
		json.put("q", q);
		json.put(
				"fq",
				"gathered_1st_date:[" + calendar.get(Calendar.YEAR) + "-"
						+ (calendar.get(Calendar.MONTH) + 1) + "-"
						+ calendar.get(Calendar.DATE) + "T"
						+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
						+ calendar.get(Calendar.MINUTE) + ":"
						+ calendar.get(Calendar.SECOND) + "Z TO "
						+ cal.get(Calendar.YEAR) + "-"
						+ (cal.get(Calendar.MONTH) + 1) + "-"
						+ cal.get(Calendar.DATE) + "T"
						+ cal.get(Calendar.HOUR_OF_DAY) + ":"
						+ cal.get(Calendar.MINUTE) + ":"
						+ cal.get(Calendar.SECOND) + "Z" + "]");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		System.out.println(select.toString());
		List<Event> events = cassandraOperations.select(select, Event.class);
		System.out.println("Is Events are Empty : " + events.isEmpty()
				+ " values " + events);
		if (events != null && events.size() > 0)
			return events;
		else
			return new ArrayList<Event>();
	}

	@Override
	public List<Event> findDeletedEventsBetWeenTimeInterval(String location,
			Long timeInMills) {
		String q = "has_organizer_confirmed:true AND is_deleted:true";
		return commonForFindEventsBetWeenTimeInterval(location, timeInMills, q);
	}

	@Override
	public List<Event> findEventsBetWeenTimeInterval(String location,
			Long timeInMills) {
		String q = "has_organizer_confirmed:true";
		return commonForFindEventsBetWeenTimeInterval(location, timeInMills, q);
	}

	private List<Event> commonForFindEventsBetWeenTimeInterval(String location,
			Long timeInMills, String q) {

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTimeInMillis(timeInMills);

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		JSONObject json = new JSONObject();
		json.put("q", q);
		json.put(
				"fq",
				"event_start_time:[" + cal.get(Calendar.YEAR) + "-"
						+ (cal.get(Calendar.MONTH) + 1) + "-"
						+ cal.get(Calendar.DATE) + "T"
						+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
						+ calendar.get(Calendar.MINUTE) + ":"
						+ calendar.get(Calendar.SECOND) + "Z TO "
						+ cal.get(Calendar.YEAR) + "-"
						+ (cal.get(Calendar.MONTH) + 1) + "-"
						+ cal.get(Calendar.DATE) + "T" + cal.get(Calendar.HOUR)
						+ ":" + cal.get(Calendar.MINUTE) + ":"
						+ cal.get(Calendar.SECOND) + "Z"
						+ "] AND {!geofilt sfield=chosen_location_latlong pt="
						+ location + " d=10}");
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null && events.size() > 0)
			return events;
		else
			return new ArrayList<Event>();
	}
}
