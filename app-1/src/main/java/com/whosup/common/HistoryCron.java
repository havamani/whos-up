package com.whosup.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.dao.EventDao;
import com.whosup.dao.UserDao;
import com.whosup.dao.UserEventInboxDao;
import com.whosup.dao.UserInterestedEventDao;
import com.whosup.model.Event;
import com.whosup.model.UserEventInbox;
import com.whosup.model.UserInterestedEvent;
import com.whosup.service.CommonService;

@Service
@EnableScheduling
public class HistoryCron {

	@Autowired
	private CassandraOperations cassandraOperations;
	@Autowired
	private UserInterestedEventDao userInterestedEventDao;
	@Autowired
	private UserEventInboxDao userEventInboxDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserDao userDao;

	@Scheduled(cron = "0 0 0/1 * * ?")
	public void timeoutCron() {
		List<UUID> eventIds = new ArrayList<UUID>();
		String query = "is_chat:false AND is_deleted:false";
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("solr_query", query));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null) {
			for (Event e : events) {
				if (e.getEventExpiry() != null
						&& e.getEventExpiry().before(new Date())) {
					e.setIsDeleted(true);
					eventIds.add(e.getEventID());
				}
			}
		}
		List<UserEventInbox> inboxs = userEventInboxDao
				.findUsersByEventIds(eventIds);
		if (inboxs.size() > 0) {
			userEventInboxDao.delete(inboxs);
		}
		List<UserInterestedEvent> interestedEvents = userInterestedEventDao
				.findByEventIds(eventIds);
		if (interestedEvents.size() > 0) {
			userInterestedEventDao.delete(interestedEvents);
		}
		if (events.size() > 0) {
			eventDao.update(events);
		}
		for (Event e : events) {
			try {
//				commonService.notify(
//						userDao.findById(e.getGathered1stByUserID()),
//						e.getTitle() + " event is Expired on "
//								+ e.getEventExpiry());
				if(e.getIsDeleted()){
					for(UUID uuid:e.getInterestedUserIDs())
				      commonService.notify(userDao.findById(uuid),e.getTitle() + " event is Expired on "+ e.getEventExpiry());
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println(new Date() + " - TimeOut Cron job (" + events.size()
				+ ")");
	}

	@Scheduled(cron = "0 0 4,12,20 * * ?")
	public void historyCron() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		Calendar date = Calendar.getInstance();
		date.clear();
		date.setTimeZone(TimeZone.getTimeZone("GMT"));
		date.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE));
		List<UUID> eventIds = new ArrayList<UUID>();
		List<UUID> deleteEventIds = new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("event_date", date.getTime()));
		List<Event> events = cassandraOperations.select(select, Event.class);
		if (events != null) {
			for (Event e : events) {
				if (e.getEventEndTime() != null
						&& e.getEventEndTime().before(cal.getTime())) {
					if (e.getHasOrganiserConfirmed()) {
						e.setIsHistory(true);
						eventIds.add(e.getEventID());
					} else {
						e.setIsDeleted(true);
						deleteEventIds.add(e.getEventID());
					}
				}
			}
		}
		List<UserEventInbox> inboxs = userEventInboxDao
				.findUsersByEventIds(eventIds);
		if (inboxs.size() > 0) {
			for (UserEventInbox i : inboxs) {
				i.setIsHistory(true);
			}
		}
		List<UserEventInbox> deleteInboxs = userEventInboxDao
				.findUsersByEventIds(deleteEventIds);
		if (deleteInboxs.size() > 0) {
			userEventInboxDao.delete(deleteInboxs);
		}
		List<UserInterestedEvent> interestedEvents = userInterestedEventDao
				.findByEventIds(deleteEventIds);
		if (interestedEvents.size() > 0) {
			userInterestedEventDao.delete(interestedEvents);
		}
		if (events.size() > 0) {
			eventDao.update(events);
			userEventInboxDao.update(inboxs);
		}

		for (Event e : events) {
			try {
				commonService.notify(userDao.findById(e
						.getGathered1stByUserID()), e.getEventID().toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		date.add(Calendar.DATE, -1);
		eventIds = new ArrayList<UUID>();
		deleteEventIds = new ArrayList<UUID>();
		select = QueryBuilder.select().from("event");
		select.where(QueryBuilder.eq("event_date", date.getTime()));
		events = cassandraOperations.select(select, Event.class);
		if (events != null) {
			for (Event e : events) {
				if (e.getEventEndTime() != null
						&& e.getEventEndTime().before(cal.getTime())) {
					if (e.getHasOrganiserConfirmed()) {
						e.setIsHistory(true);
						eventIds.add(e.getEventID());
					} else {
						e.setIsDeleted(true);
						deleteEventIds.add(e.getEventID());
					}
				}
			}
		}
		inboxs = userEventInboxDao.findUsersByEventIds(eventIds);
		if (inboxs.size() > 0) {
			for (UserEventInbox i : inboxs) {
				i.setIsHistory(true);
			}
		}
		deleteInboxs = userEventInboxDao.findUsersByEventIds(deleteEventIds);
		if (deleteInboxs.size() > 0) {
			userEventInboxDao.delete(deleteInboxs);
		}
		interestedEvents = userInterestedEventDao
				.findByEventIds(deleteEventIds);
		if (interestedEvents.size() > 0) {
			userInterestedEventDao.delete(interestedEvents);
		}
		if (events.size() > 0) {
			eventDao.update(events);
			userEventInboxDao.update(inboxs);
		}
		for (Event e : events) {
			for (UUID u : e.getConfirmedUserIDs()) {
				try {
					commonService.notify(userDao.findById(u), e.getTitle()
							+ " event is Finished ");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		System.out.println(date.getTime() + " - History Cron job ("
				+ events.size() + ")");
	}

	public void kafkaConsumer() {
		/*
		 * System.out.println("Consumer Started"); String zooKeeper =
		 * "52.30.54.63:2181"; String groupId = "test-consumer-group"; String
		 * topic = "notify"; SimpleConsumer example = new
		 * SimpleConsumer(zooKeeper, groupId, topic); example.run(1); try {
		 * Thread.sleep(10000); } catch (InterruptedException ie) {
		 * 
		 * } example.shutdown(); System.out.println("Consumer Stopped");
		 */}

	/*
	 * @Scheduled(cron = "0 0 12 * * ?") public void historyDeleteCron() {
	 * Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	 * Calendar date = Calendar.getInstance(); date.clear();
	 * date.setTimeZone(TimeZone.getTimeZone("GMT"));
	 * date.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
	 * cal.get(Calendar.DATE)); date.add(Calendar.DATE, -15); List<UUID>
	 * eventIds = new ArrayList<UUID>(); Select select =
	 * QueryBuilder.select().from("event");
	 * select.where(QueryBuilder.eq("event_date", date.getTime())); List<Event>
	 * events = cassandraOperations.select(select, Event.class); if (events !=
	 * null) { for (Event e : events) { if (e.getIsHistory()) {
	 * e.setIsDeleted(true); eventIds.add(e.getEventID()); } } }
	 * List<UserEventInbox> inboxs = userEventInboxDao
	 * .findUsersByEventIds(eventIds); if (inboxs.size() > 0) {
	 * userEventInboxDao.delete(inboxs); } List<UserInterestedEvent>
	 * interestedEvents = userInterestedEventDao .findByEventIds(eventIds); if
	 * (interestedEvents.size() > 0) {
	 * userInterestedEventDao.delete(interestedEvents); } if (events.size() > 0)
	 * { eventDao.update(events); } System.out.println(new Date() +
	 * " - History Delete Cron job (" + events.size() + ")"); }
	 */
}
