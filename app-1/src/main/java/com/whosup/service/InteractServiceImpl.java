package com.whosup.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.EventDao;
import com.whosup.dao.EventHistoryPictureDao;
import com.whosup.dao.IOweDao;
import com.whosup.dao.OwesMeDao;
import com.whosup.dao.UserContactDao;
import com.whosup.dao.UserDao;
import com.whosup.dao.UserEventFeedbackDao;
import com.whosup.dao.UserEventInboxDao;
import com.whosup.dao.UserInterestedEventDao;
import com.whosup.model.Event;
import com.whosup.model.EventHistoryPicture;
import com.whosup.model.EventHistoryPictureKey;
import com.whosup.model.IOwe;
import com.whosup.model.IOweKey;
import com.whosup.model.OwesMe;
import com.whosup.model.OwesMeKey;
import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.UserEventFeedback;
import com.whosup.model.UserEventFeedbackKey;
import com.whosup.model.UserEventInbox;
import com.whosup.model.UserEventInboxKey;
import com.whosup.model.UserInterestedEvent;
import com.whosup.model.UserInterestedEventKey;
import com.whosup.model.request.InteractRequest;
import com.whosup.model.response.ConfirmRequest;
import com.whosup.model.response.EventSettingResponse;
import com.whosup.model.response.PictureList;
import com.whosup.model.response.Status;
import com.whosup.model.response.UserEvent;
import com.whosup.model.response.UserEventDetails;

public class InteractServiceImpl implements InteractService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private IOweDao iOweDao;
	@Autowired
	private OwesMeDao owesMeDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserContactDao userContactDao;
	@Autowired
	private UserEventInboxDao userEventInboxDao;
	@Autowired
	private UserInterestedEventDao userInterestedEventDao;
	@Autowired
	private EventHistoryPictureDao eventHistoryPictureDao;
	@Autowired
	private UserEventFeedbackDao userEventFeedbackDao;

	@Override
	public UserEvent interactView(UUID userID) {
		UserEvent userEvents = new UserEvent();
		List<UUID> eventIds = userEventInboxDao.findChatOpened(userID);

		List<UUID> upcomingeventIds = userEventInboxDao
				.findConfirmedAndNotHistoryEventsbyUserId(userID);
		List<Event> events = eventDao.findEventsById(upcomingeventIds);
		Collections.sort(events, new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getEventDate().compareTo(o2.getEventDate());
			}
		});
		List<User> users = new ArrayList<User>();
		Map<UUID, User> usersById = new HashMap<UUID, User>();
		if (events.size() > 0) {
			users = userDao.findUserByIds(new ArrayList<UUID>(events.get(0)
					.getInterestedUserIDs()));
			if (users != null) {
				for (User u : users) {
					usersById.put(u.getUserID(), u);
				}
			}
			if (eventIds.contains(events.get(0).getEventID()))
				eventIds.remove(events.get(0).getEventID());
			UserEventDetails detail = new UserEventDetails(
					userEventInboxDao.selectEventByUserIdAndEventId(userID,
							events.get(0).getEventID()));
			detail.setIsThereOrganizer(true);
			if (events.get(0).getOrganizerUserID().equals(userID))
				detail.setIsOrganizer(true);
			else
				detail.setIsOrganizer(false);
			List<User> eventUsers = new ArrayList<User>();
			for (UUID u : events.get(0).getInterestedUserIDs()) {
				eventUsers.add(usersById.get(u));
			}
			detail.setUsers(eventUsers);
			detail.setEventStartTime(events.get(0).getEventStartTime());
			detail.setEventEndTime(events.get(0).getEventEndTime());
			detail.setEventPhotoPath(events.get(0).getSmallPhotoPath());
			detail.setMinPlaces(events.get(0).getMinPlaces());
			if (events.get(0).getQueuedUserIDs() != null
					&& events.get(0).getQueuedUserIDs().contains(userID))
				detail.setIsCurrentUserInQueue(true);
			userEvents.setUpcoming(detail);
		}

		List<UUID> otherEventIds = userEventInboxDao
				.findChatNotOpenedEventsByUserId(userID);
		if (otherEventIds != null) {
			if (eventIds != null)
				eventIds.addAll(otherEventIds);
			else
				eventIds = otherEventIds;
		}
		List<UserEventInbox> inboxs = userEventInboxDao
				.findByUserIdAndEventIds(userID, eventIds);
		events = eventDao.findEventsById(eventIds);

		HashMap<UUID, Event> eventsByIds = new HashMap<UUID, Event>();
		Set<UUID> userIds = new HashSet<UUID>();
		if (eventIds != null) {
			for (Event e : events) {
				userIds.addAll(e.getInterestedUserIDs());
				eventsByIds.put(e.getEventID(), e);
			}
		}
		HashMap<UUID, UserEventInbox> inboxsByIds = new HashMap<UUID, UserEventInbox>();
		if (eventIds != null) {
			for (UserEventInbox i : inboxs)
				inboxsByIds.put(i.getPk().getEventID(), i);
		}
		users = userDao.findUserByIds(new ArrayList<UUID>(userIds));
		usersById = new HashMap<UUID, User>();
		if (users != null) {
			for (User u : users) {
				usersById.put(u.getUserID(), u);
			}
		}

		List<UserEventDetails> orderedEvents = new ArrayList<UserEventDetails>();
		for (UUID id : eventIds) {
			UserEventDetails detail = new UserEventDetails(inboxsByIds.get(id));
			if (eventsByIds.get(id).getOrganizerUserID() != null) {
				detail.setIsThereOrganizer(true);
				if (eventsByIds.get(id).getOrganizerUserID().equals(userID))
					detail.setIsOrganizer(true);
				else
					detail.setIsOrganizer(false);
			} else {
				detail.setIsThereOrganizer(false);
			}
			if (eventsByIds.get(id).getGathered1stByUserID().equals(userID))
				detail.setIsFirstUser(true);
			if (eventsByIds.get(id).getIsFirstUserReject() == null ? false
					: eventsByIds.get(id).getIsFirstUserReject())
				detail.setAskFirstUser(false);
			detail.setIsPaid(eventsByIds.get(id).getIsPaid());
			detail.setWebLink(eventsByIds.get(id).getWebLink());
			List<User> eventUsers = new ArrayList<User>();
			for (UUID u : eventsByIds.get(id).getInterestedUserIDs()) {
				eventUsers.add(usersById.get(u));
			}
			if (eventsByIds.get(id).getIsHistory()) {
				UUID lastUploadedUser = eventHistoryPictureDao
						.findLastUploadedUserByEventId(id);
				if (lastUploadedUser != null)
					detail.setLastMessagedUser(usersById.get(lastUploadedUser));
			}
			detail.setMinPlaces(eventsByIds.get(id).getMinPlaces());
			detail.setUsers(eventUsers);
			detail.setEventStartTime(eventsByIds.get(id).getEventStartTime());
			detail.setEventEndTime(eventsByIds.get(id).getEventEndTime());
			detail.setEventPhotoPath(eventsByIds.get(id).getSmallPhotoPath());
			// detail.setEventPhotoPath(eventDao.selectEvent(id).getSmallPhotoPath());
			// detail.setEventPhotoPath(events.get(0).getSmallPhotoPath());
			if (eventsByIds.get(id).getQueuedUserIDs() != null
					&& eventsByIds.get(id).getQueuedUserIDs().contains(userID))
				detail.setIsCurrentUserInQueue(true);
			orderedEvents.add(detail);
		}
		userEvents.setRemaining(orderedEvents);
		return userEvents;
	}

	@Override
	public EventSettingResponse eventSetting(UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		UUID organizer = event.getOrganizerUserID();
		EventSettingResponse response = new EventSettingResponse(event);
		List<UUID> userIds = userEventInboxDao.findConfirmedUsers(eventId);
		if (userDao.findById(organizer) != null)
			response.setOrganizer(userDao.findById(organizer));
		if (userIds != null) {
			if (userIds.contains(organizer))
				userIds.remove(organizer);
			response.setConfirmed(userDao.findUserByIds(userIds));
		}
		userIds = userEventInboxDao.findUnConfirmedUsers(eventId);
		if (userIds != null) {
			if (userIds.contains(organizer))
				userIds.remove(organizer);
			response.setUnConfirmed(userDao.findUserByIds(userIds));
		}
		if(event.getVisibleToUserIDs() != null ) {
			if(userIds.contains(organizer))
				userIds.remove(organizer);
			response.setVisibleToUserIds(userDao.findUserByIds(event.getVisibleToUserIDs()));
		}
		if (event.getEventStartTime() != null
				&& event.getEventStartTime().before(new Date()))
			response.setIsEventCompleted(true);
		return response;
	}

	@Override
	public Status editSettings(InteractRequest request, UUID userId) {
		Event event = eventDao.selectEvent(request.getEventId());
		if (event.getOrganizerUserID() != null
				&& event.getOrganizerUserID().equals(userId)) {
			if (request.getMinAge() != null)
				event.setGatherAdvancedAgeLower(request.getMinAge());
			if (request.getMaxAge() != null)
				event.setGatherAdvancedAgeUpper(request.getMaxAge());
			event.setIsPublic(request.getIsPublic() == null ? false : request
					.getIsPublic());
			event.setGatherAdvancedFemaleOnly(request.getFemaleOnly() == null ? false
					: request.getFemaleOnly());
			if (request.getGatherRadius() != null) {
				event.setGatherAdvancedLocRadius(request.getGatherRadius());
				System.out.println("Radius:"+request.getGatherRadius());
			}
			if (request.getContactIDs() != null
					&& request.getContactIDs().size() != 0) {
//				List<UserContact> contacts = userContactDao.findByContactIds(
//						userId, request.getContactIDs());
//				List<UUID> userIds = new ArrayList<UUID>();
//				for (UserContact con : contacts) {
//					if (request.getMyCrowdOnly()) {
//						if (con.getIsStarred())
//							userIds.add(con.getContactUserID());
//					} else
//						userIds.add(con.getContactUserID());
//				}
//				if (userIds.size() > 0)
//					event.setVisibleToUserIDs(userIds);
//				else
//					throw new AppException(new AppErrorMessage("400",
//							"Please select some users"));
				event.setVisibleToUserIDs(request.getContactIDs());
			} else if (request.getGatherFrom() != null) {
				List<UserContact> contacts = userContactDao
						.findContactsByGatherFrom(userId,
								request.getGatherFrom());
				List<UUID> userIds = new ArrayList<UUID>();
				for (UserContact con : contacts) {
					if (request.getMyCrowdOnly()) {
						if (con.getIsStarred())
							userIds.add(con.getContactUserID());
					} else
						userIds.add(con.getContactUserID());
				}
				if (userIds.size() > 0)
					event.setVisibleToUserIDs(userIds);
				else
					throw new AppException(new AppErrorMessage("400",
							"Please select some users"));
			} else
				throw new AppException(new AppErrorMessage("400",
						"Please select some users"));

			if (!event.getHasOrganiserConfirmed()) {
				if (event.getInterestedUserIDs().size() > event.getMinPlaces()
						&& event.getInterestedUserIDs().size() > request
								.getMinPlaces())
					event.setMinPlaces(request.getMinPlaces());
				event.setMaxPlaces(request.getMaxPlaces());
			} else {
				if (event.getMinPlaces() > request.getMinPlaces())
					throw new AppException(new AppErrorMessage("400",
							"You cannot reduce min places after confirmation"));
				else {
					if (event.getInterestedUserIDs().size() > event
							.getMinPlaces()
							&& event.getInterestedUserIDs().size() > request
									.getMinPlaces())
						event.setMinPlaces(request.getMinPlaces());
				}
				if (event.getTotalAttending() > request.getMaxPlaces()) {
					throw new AppException(
							new AppErrorMessage("400",
									"You cannot reduce max places below confirmed People"));
				} else {
					event.setMaxPlaces(request.getMaxPlaces());
					if (event.getMaxPlaces() > event.getTotalAttending()) {
						List<UUID> queuedUserIDs = new ArrayList<UUID>();
						if (event.getQueuedUserIDs() != null)
							queuedUserIDs.addAll(event.getQueuedUserIDs());
						List<UUID> userIDs = new ArrayList<UUID>();
						List<UUID> confirmedUserIds = new ArrayList<UUID>();
						if (event.getConfirmedUserIDs() != null)
							confirmedUserIds
									.addAll(event.getConfirmedUserIDs());
						userIDs.add(event.getOrganizerUserID());
						while (event.getMaxPlaces() > event.getTotalAttending()
								&& event.getQueuedUserIDs() != null
								&& event.getQueuedUserIDs().size() > 0) {
							userIDs.add(queuedUserIDs.get(0));
							queuedUserIDs.remove(0);
							event.setTotalAttending(event.getTotalAttending() + 1);
						}
						event.setQueuedUserIDs(queuedUserIDs);

						List<UserEventInbox> userEventsInbox = userEventInboxDao
								.selectEventByUserIdsAndEventId(userIDs,
										event.getEventID());
						for (UserEventInbox userEvent : userEventsInbox) {
							userEvent.setIsUserConfirmed(true);
							confirmedUserIds.add(userEvent.getPk().getUserID());
						}
						event.setConfirmedUserIDs(confirmedUserIds);
						userEventInboxDao.update(userEventsInbox);
					}
				}
			}
			eventDao.update(event);
			List<UserEventInbox> eventsInbox = userEventInboxDao
					.findUsersByEventId(event.getEventID());
			for (UserEventInbox eventInbox : eventsInbox) {
				eventInbox.setIsFromPublic(event.getIsPublic());
				eventInbox.setPeopleGathered(event.getInterestedUserIDs()
						.size());
				eventInbox.setPeopleToGather(event.getMaxPlaces()
						- event.getInterestedUserIDs().size());
				eventInbox.setPlacesFilled(event.getTotalAttending());
				eventInbox.setPlacesToFill(event.getMaxPlaces()
						- event.getTotalAttending());
			}
			userEventInboxDao.update(eventsInbox);
			return new Status("Success");
		} else {
			throw new AppException(new AppErrorMessage("400",
					"You can't edit Event"));
		}
	}

	@Override
	public Status eventDate(InteractRequest request) {
		Event event = eventDao.selectEvent(request.getEventId());
		if (event != null) {
			event.setEventDate(request.getEventDate());
			event.setEventStartTime(request.getEventStartTime());
			event.setEventEndTime(request.getEventEndTime());
			eventDao.update(event);
			return new Status("Success");
		}
		return new Status("Failure, Event not exist");
	}

	@Override
	public Status deleteEvent(UUID userID, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		User user = userDao.findById(userID);
		if (event.getOrganizerUserID() != null
				&& event.getOrganizerUserID().equals(userID)) {
			if (event.getHasOrganiserConfirmed())
				throw new AppException(
						new AppErrorMessage("400",
								"You can't leave after confirming Event. please resign before leaving"));
			event.setOrganizerUserID(null);
		} else {
			if (event.getHasOrganiserConfirmed()
					&& event.getConfirmedUserIDs() != null
					&& event.getConfirmedUserIDs().contains(userID)) {
				throw new AppException(new AppErrorMessage("400",
						"Leave Event before Removing"));
			}
		}
		List<UUID> interestedUserIDs = new ArrayList<UUID>();
		if (event.getInterestedUserIDs() != null
				&& !event.getInterestedUserIDs().isEmpty()) {
			interestedUserIDs.addAll(event.getInterestedUserIDs());
			interestedUserIDs.remove(userID);
			event.setInterestedUserIDs(interestedUserIDs);
			System.out.println("Check for min users "
					+ (int) Math.ceil(0.5 * (event.getMinPlaces() + 1)));
			if (event.getInterestedUserIDs().size() >= (int) Math
					.ceil(0.5 * (event.getMinPlaces() + 1)))
				event.setIsChat(true);
			else
				event.setIsChat(false);
		}
		List<UUID> visibleUserIDs = new ArrayList<UUID>();
		if (event.getInterestedUserIDs() != null
				&& !event.getInterestedUserIDs().isEmpty())
			visibleUserIDs.addAll(event.getVisibleToUserIDs());
		visibleUserIDs.remove(userID);
		event.setVisibleToUserIDs(visibleUserIDs);
		eventDao.update(event);
		UserEventInbox userEventInbox = userEventInboxDao
				.selectEventByUserIdAndEventId(userID, eventId);
		userEventInboxDao.delete(userEventInbox);

		List<UserEventInbox> eventsInbox = userEventInboxDao
				.findUsersByEventId(eventId);
		for (UserEventInbox eventInbox : eventsInbox) {
			eventInbox.setIsChat(event.getIsChat());
			eventInbox.setPeopleGathered(event.getInterestedUserIDs().size());
			eventInbox.setPeopleToGather(event.getMaxPlaces()
					- event.getInterestedUserIDs().size());
		}
		userEventInboxDao.update(eventsInbox);
		UserInterestedEvent userInterestedEvent = userInterestedEventDao
				.selectByEventAndUserId(userID, eventId);
		userInterestedEventDao.delete(userInterestedEvent);
		if (event.getChatId() != null)
			commonService
					.removeUser(UUID.fromString(event.getChatId()), userID);
		if (event.getInterestedUserIDs() == null
				|| event.getInterestedUserIDs().isEmpty()) {
			event.setIsDeleted(true);
			eventDao.update(event);
		}
		try {
			if (!event.getHasOrganiserConfirmed()) {
				List<User> InterestedUsers = userDao.findUserByIds(event
						.getInterestedUserIDs());
				if (event.getInterestedUserIDs() != null
						&& !event.getInterestedUserIDs().isEmpty()) {
					for (User eventUser : InterestedUsers) {
						if (userID != eventUser.getUserID()) {
							JSONObject notification = new JSONObject();
							notification.put("type", "ATTENDEE_LEFT");
							notification.put("message",
									user.getFullName()
											+ " has left from the Event "
											+ event.getTitle());
							commonService.notify(eventUser,
									notification.toString());
						}
					}
				}
			}
			else if(event.getHasOrganiserConfirmed() && event.getOrganizerUserID().equals(userID)){
				List<User> InterestedUsers=userDao.findUserByIds(event.getInterestedUserIDs());
				for (User eventUser : InterestedUsers) {
					if (userID != eventUser.getUserID()) {
						JSONObject notification = new JSONObject();
						notification.put("type", "EVENT_DELETED");
						notification.put("message",user.getFullName()+ "  has deleted the Event "+ event.getTitle());
						commonService.notify(eventUser,notification.toString());
					}
				}
			} else {
					JSONObject notification = new JSONObject();
					notification.put("type", "ATTENDEE_LEFT");
					notification.put("message",user.getFullName()+ " has left from the Event "+ event.getTitle());
					commonService.notify(userDao.findById(event.getOrganizerUserID()),notification.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Status("Success");
	}

	@Override
	public ConfirmRequest confirmRequest(UUID userID, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		User user = userDao.findById(userID);
		if (!event.getHasOrganiserConfirmed())
			throw new AppException(new AppErrorMessage("400",
					"Event not confirmed yet"));
		ConfirmRequest response = new ConfirmRequest(userDao.findById(
				event.getOrganizerUserID()).getFullName(),
				event.getIsPaid() ? (event.getIsFixedAmount() ? event
						.getFixedCost() : event.getCostToSplit())
						: new BigDecimal(0), event.getTotalAttending(),
				event.getMaxPlaces());
		response.setCurrency(event.getCurrency());
		response.setPosition(0);
		response.setConfirmedUserIDs(event.getConfirmedUserIDs());
		UserEventInbox eventInbox = userEventInboxDao
				.selectEventByUserIdAndEventId(userID, eventId);
		if (eventInbox.getIsUserConfirmed()) {
			response.setIsConfirmed(true);
			if (event.getUnConfirmedUserIDs() != null
					&& event.getUnConfirmedUserIDs().contains(userID))
				response.setIsCancelled(true);
			response.setInQueue(false);
			commonService.confirmedMessage(event.getChatId(), user,"I was Successfully Added into EventQueue");
			return response;
		} else if (event.getQueuedUserIDs() != null
				&& event.getQueuedUserIDs().contains(userID)) {
			response.setInQueue(true);
			response.setPosition(event.getQueuedUserIDs().indexOf(userID) + 1);
			commonService.confirmedMessage(event.getChatId(), user, "I was Successfully Joined into Event ");
			return response;
		}
		return response;
	}

	@Override
	public Status confirmEvent(User user, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		if ((event.getTotalAttending() - (event.getUnConfirmedUserIDs() == null ? 0
				: event.getUnConfirmedUserIDs().size())) < event.getMaxPlaces()
				|| event.getMaxPlaces().equals(200)) {
			if ((event.getTotalAttending() >= event.getMinPlaces())
					&& event.getUnConfirmedUserIDs() != null
					&& event.getUnConfirmedUserIDs().size() > 0) {
				UUID unConfirmedUser = event.getUnConfirmedUserIDs().get(0);
				List<UUID> unConfirmedUserIDs = new ArrayList<UUID>();
				unConfirmedUserIDs.addAll(event.getUnConfirmedUserIDs());
				unConfirmedUserIDs.remove(unConfirmedUser);
				event.setUnConfirmedUserIDs(unConfirmedUserIDs);
				eventDao.update(event);
				deletePayment(event, unConfirmedUser, eventId);
			} else
				event.setTotalAttending(event.getTotalAttending() + 1);
			UserEventInbox userEventInbox = userEventInboxDao
					.selectEventByUserIdAndEventId(user.getUserID(), eventId);
			userEventInbox.setIsUserConfirmed(true);
			userEventInboxDao.update(userEventInbox);
			List<UUID> confirmedUserIDs = new ArrayList<UUID>();
			confirmedUserIDs.addAll(event.getConfirmedUserIDs());
			confirmedUserIDs.add(userEventInbox.getPk().getUserID());
			event.setConfirmedUserIDs(confirmedUserIDs);
			if (event.getIsFixedAmount() == null ? false : event
					.getIsFixedAmount()) {
				OwesMe payment = new OwesMe(new OwesMeKey(
						event.getOrganizerUserID(), event.getEventID(),
						user.getUserID()));
				payment.setAmountOwed(event.getFixedCost());
				payment.setCurrency(event.getCurrency());
				payment.setEventTitle(event.getTitle());
				payment.setEventDate(event.getEventDate());
				payment.setOwingUserPhotoPath(user.getPhotoPath());
				payment.setOwingUserName(user.getFullName());
				owesMeDao.save(payment);
				IOwe owe = new IOwe(new IOweKey(user.getUserID(),
						event.getEventID()), event.getOrganizerUserID(),
						event.getFixedCost());
				iOweDao.save(owe);
			}
			eventDao.update(event);
			List<UserEventInbox> eventsInbox = userEventInboxDao
					.findUsersByEventId(event.getEventID());
			for (UserEventInbox eventInbox : eventsInbox) {
				eventInbox.setPlacesFilled(event.getTotalAttending());
				eventInbox.setPlacesToFill(event.getMaxPlaces()
						- event.getTotalAttending());
			}
			userEventInboxDao.update(eventsInbox);
		} else {
			List<UUID> queuedUserIDs = new ArrayList<UUID>();
			if (event.getQueuedUserIDs() != null)
				queuedUserIDs.addAll(event.getQueuedUserIDs());
			queuedUserIDs.add(user.getUserID());
			event.setQueuedUserIDs(queuedUserIDs);
			eventDao.update(event);
		}
		try{
		JSONObject notification = new JSONObject();
		notification.put("type", "EVENT_CONFIRMED");
		notification.put("message", user.getFullName()+" has been Joined in "+event.getTitle());
		User organizer=userDao.findById(event.getOrganizerUserID());
		commonService.notify(organizer, notification.toString());
		}catch(Exception exception){
			exception.printStackTrace();
		}
		//commonService.confirmedMessage(event.getChatId(), event, user,Notification.EVENT_CONFIRM);
		return new Status("Success");
	}

	@Override
	public Status unConfirmEvent(UUID userID, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		User user = userDao.findById(userID);
		if (event.getQueuedUserIDs() != null
				&& event.getQueuedUserIDs().contains(userID)) {
			List<UUID> queuedUserIDs = new ArrayList<UUID>();
			queuedUserIDs.addAll(event.getQueuedUserIDs());
			queuedUserIDs.remove(userID);
			event.setQueuedUserIDs(queuedUserIDs);
			eventDao.update(event);
		} else if ((event.getTotalAttending() + (event.getQueuedUserIDs() == null ? 0
				: event.getQueuedUserIDs().size())) > event.getMinPlaces()) {
			if (event.getQueuedUserIDs() == null
					|| event.getQueuedUserIDs().size() == 0) {
				event.setTotalAttending(event.getTotalAttending() - 1);
				eventDao.update(event);
				deletePayment(event, userID, eventId);
				List<UserEventInbox> eventsInbox = userEventInboxDao
						.findUsersByEventId(event.getEventID());
				for (UserEventInbox eventInbox : eventsInbox) {
					eventInbox.setPlacesFilled(event.getTotalAttending());
					eventInbox.setPlacesToFill(event.getMaxPlaces()
							- event.getTotalAttending());
				}
				userEventInboxDao.update(eventsInbox);
			} else {
				deletePayment(event, userID, eventId);
				User queueUser = userDao.findById(event.getQueuedUserIDs().get(
						0));
				List<UUID> queuedUserIds = new ArrayList<UUID>();
				queuedUserIds.addAll(event.getQueuedUserIDs());
				queuedUserIds.remove(0);
				event.setQueuedUserIDs(queuedUserIds);
				UserEventInbox userEventInbox = userEventInboxDao
						.selectEventByUserIdAndEventId(queueUser.getUserID(),
								eventId);
				userEventInbox.setIsUserConfirmed(true);
				userEventInboxDao.update(userEventInbox);
				List<UUID> confirmedUserIDs = new ArrayList<UUID>();
				confirmedUserIDs.addAll(event.getConfirmedUserIDs());
				confirmedUserIDs.add(queueUser.getUserID());
				event.setConfirmedUserIDs(confirmedUserIDs);
				eventDao.update(event);
				if (event.getIsFixedAmount()) {
					OwesMe payment = new OwesMe(new OwesMeKey(
							event.getOrganizerUserID(), event.getEventID(),
							queueUser.getUserID()));
					payment.setAmountOwed(event.getFixedCost());
					payment.setCurrency(event.getCurrency());
					payment.setEventTitle(event.getTitle());
					payment.setEventDate(event.getEventDate());
					payment.setOwingUserPhotoPath(queueUser.getPhotoPath());
					payment.setOwingUserName(queueUser.getFullName());
					owesMeDao.save(payment);
					IOwe owe = new IOwe(new IOweKey(queueUser.getUserID(),
							event.getEventID()), event.getOrganizerUserID(),
							event.getFixedCost());
					iOweDao.save(owe);
				}

			}

		} else {
			List<UUID> unConfirmedUserIDs = new ArrayList<UUID>();
			if (event.getUnConfirmedUserIDs() != null)
				unConfirmedUserIDs.addAll(event.getUnConfirmedUserIDs());
			if (event.getConfirmedUserIDs().contains(userID)
					&& !unConfirmedUserIDs.contains(userID)) {
				unConfirmedUserIDs.add(userID);
				event.setUnConfirmedUserIDs(unConfirmedUserIDs);
				eventDao.update(event);
			}
		}
		try {
			User organizer = userDao.findById(event.getOrganizerUserID());
			JSONObject notification = new JSONObject();
			notification.put("type", "ATTENDEE_LEAVED");
			notification.put("message",user.getFullName() +" has left from the Event "+event.getTitle());
			commonService.notify(organizer, notification.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Status("Success");
	}

	protected void deletePayment(Event event, UUID userID, UUID eventId) {
		UserEventInbox userEventInbox = userEventInboxDao
				.selectEventByUserIdAndEventId(userID, eventId);
		userEventInbox.setIsUserConfirmed(false);
		userEventInboxDao.update(userEventInbox);
		List<UUID> confirmedUserIDs = new ArrayList<UUID>();
		if (event.getConfirmedUserIDs() != null) {
			confirmedUserIDs.addAll(event.getConfirmedUserIDs());
			confirmedUserIDs.remove(userID);
		}
		event.setConfirmedUserIDs(confirmedUserIDs);
		eventDao.save(event);
		OwesMe payment = owesMeDao.selectByEventIdAndUserID(eventId, userID);
		if (payment != null && payment.getDatePaidElectronically() == null
				&& payment.getDateMarkedAsPaid() == null) {
			iOweDao.delete(userID, eventId);
			owesMeDao.delete(payment);
		}
	}

	@Override
	public Status reject(UUID userID, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		User user = userDao.findById(userID);
		if (!event.getGathered1stByUserID().equals(userID))
			throw new AppException(new AppErrorMessage("400",
					"Failed, Not a First User"));
		event.setIsFirstUserReject(true);
		eventDao.update(event);
		eventDao.updateOrganizer(event.getEventID());
		commonService.confirmedMessage(event.getChatId(), user," I was rejected Organizer Position ..!");
		return new Status("Success");
	}

	@Override
	public Status resignOrganizer(UUID userID, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		User user = userDao.findById(userID);
		if (event.getOrganizerUserID().equals(userID)) {
			if (event.getHasOrganiserConfirmed()) {
				bookingCancel(user, eventId);
				eventDao.updateOrganizer(event.getEventID());
			} else {
				eventDao.updateOrganizer(event.getEventID());
			}
		} else
			throw new AppException(new AppErrorMessage("400",
					"Failed, Not a Organiser"));
		commonService.confirmedMessage(event.getChatId(), user,"I had Resigned my Organizer position.");
		return new Status("Success");
	}

	@Override
	public Status acceptOrganizer(UUID userID, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		if (event.getOrganizerUserID() != null) {
			if (event.getOrganizerUserID().equals(userID)
					&& event.getGathered1stByUserID().equals(userID))
				event.setIsFirstUserReject(true);
			else
				throw new AppException(new AppErrorMessage("400",
						"Event already has a organizer"));
		}
		event.setOrganizerUserID(userID);
		eventDao.update(event);
		commonService.confirmedMessage(event.getChatId(), userDao.findById(userID),"I had Confirmed as an Organizer.");
		return new Status("Success");
	}

	@Override
	public Status deleteUser(UUID userID, UUID eventId, UUID deleteUserId) {
		Event event = eventDao.selectEvent(eventId);
		User user = userDao.findById(userID);
		if (event != null && event.getOrganizerUserID().equals(userID)) {
			UserEventInbox userEventInbox = userEventInboxDao
					.selectEventByUserIdAndEventId(deleteUserId, eventId);
			if (!userEventInbox.getIsUserConfirmed()) {

				List<UUID> interestedUserIDs = new ArrayList<UUID>();
				interestedUserIDs.addAll(event.getInterestedUserIDs());
				interestedUserIDs.remove(deleteUserId);
				event.setInterestedUserIDs(interestedUserIDs);
				System.out.println("Check for min users "
						+ (int) Math.ceil(0.5 * (event.getMinPlaces() + 1)));
				if (event.getInterestedUserIDs().size() >= (int) Math
						.ceil(0.5 * (event.getMinPlaces() + 1))) 
					event.setIsChat(true);
				else
					event.setIsChat(false);
				List<UUID> visibleUserIDs = new ArrayList<UUID>();
				visibleUserIDs.addAll(event.getVisibleToUserIDs());
				visibleUserIDs.remove(deleteUserId);
				event.setVisibleToUserIDs(visibleUserIDs);
				eventDao.update(event);
				userEventInboxDao.delete(userEventInbox);
				List<UserEventInbox> eventsInbox = userEventInboxDao
						.findUsersByEventId(eventId);
				for (UserEventInbox eventInbox : eventsInbox) {
					eventInbox.setIsChat(event.getIsChat());
					eventInbox.setPeopleGathered(event.getInterestedUserIDs()
							.size());
					eventInbox.setPeopleToGather(event.getMaxPlaces()
							- event.getInterestedUserIDs().size());
				}
				userEventInboxDao.update(eventsInbox);
				UserInterestedEvent userInterestedEvent = userInterestedEventDao
						.selectByEventAndUserId(deleteUserId, eventId);
				userInterestedEventDao.delete(userInterestedEvent);
				if (event.getChatId() != null)
					commonService.removeUser(
							UUID.fromString(event.getChatId()), userID);
				try {
					for(UUID eventUsers:event.getInterestedUserIDs()) {
						if(event.getOrganizerUserID() != eventUsers) {
					JSONObject notification = new JSONObject();
					notification.put("type", "EVENT_UPDATED");
					notification.put("message", "Event "+event.getTitle()+ " has been updated by "+user.getFullName());
					commonService.notify(user, notification.toString());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new Status("Success");
			}
			return new Status("Failed, Confirmed users can't be deleted");
		}
		throw new AppException(new AppErrorMessage("400",
				"Failed, Not a Organiser"));
	}

	@Override
	public Status bookingConfirm(User user, InteractRequest request) {
		Event event = eventDao.selectEvent(request.getEventId());
		if (event != null
				&& (event.getOrganizerUserID() != null && event
						.getOrganizerUserID().equals(user.getUserID()))) {
			if (event.getEventStartTime() != null
					&& event.getEventStartTime().before(new Date()))
				throw new AppException(new AppErrorMessage("400",
						"You can't edit a started event"));
			if (event.getEventEndTime() != null
					&& event.getEventEndTime().before(new Date())) {
				if (event.getIsFixedAmount())
					throw new AppException(
							new AppErrorMessage("400",
									"You can't change fixed amount after event completion"));
				else {
					event.setCurrency(request.getCurrency());
					event.setCostToSplit(request.getCostToSplit());
					eventDao.update(event);
					List<UUID> userIDs = userEventInboxDao
							.findConfirmedUsers(event.getEventID());
					List<OwesMe> payments = new ArrayList<OwesMe>();
					List<IOwe> owes = new ArrayList<IOwe>();
					for (UUID userId : userIDs) {
						OwesMe payment = new OwesMe(new OwesMeKey(
								event.getOrganizerUserID(), event.getEventID(),
								userId));
						payment.setAmountOwed(new BigDecimal(event
								.getCostToSplit().doubleValue()
								/ event.getTotalAttending()));
						payment.setCurrency(event.getCurrency());
						payment.setEventTitle(event.getTitle());
						payment.setEventDate(event.getEventDate());
						payment.setOwingUserPhotoPath(user.getPhotoPath());
						payment.setOwingUserName(user.getFullName());
						payments.add(payment);
						IOwe owe = new IOwe(new IOweKey(userId,
								event.getEventID()),
								event.getOrganizerUserID(),
								event.getFixedCost());
						owes.add(owe);
					}
					owesMeDao.save(payments);
					iOweDao.save(owes);
				}
			}
			event.setCurrency(request.getCurrency());
			if (request.getSmallPhotoPath() != null
					&& request.getSmallPhotoPath() != "")
				event.setSmallPhotoPath(request.getSmallPhotoPath());
			if (request.getLargePhotoPath() != null
					&& request.getLargePhotoPath() != "")
				event.setSmallPhotoPath(request.getLargePhotoPath());
			if (request.getWebLink() != null && request.getWebLink() != "")
				event.setWebLink(request.getWebLink());
			if (request.getDescription() != null
					&& request.getDescription() != "")
				event.setDescription(request.getDescription());
			if (!event.getHasOrganiserConfirmed())
				event.setTotalAttending(1);
			event.setEventDate(request.getEventDate());
			event.setEventStartTime(request.getEventStartTime());
			event.setEventEndTime(request.getEventEndTime());
			if (request.getChosenLocation() != null
					&& request.getChosenLocation() != "") {
				event.setChosenLocationLatLong(request.getChosenLocation());
				event.setChosenLocAddr(request.getChosenLocationAddress());
				event.setChosenLocCountry(request.getChosenLocCountry());
				event.setChosenLocAdmin3(request.getChosenLocAdmin3());
				event.setChosenLocAdmin2(request.getChosenLocAdmin2());
				event.setChosenLocAdmin1(request.getChosenLocAdmin1());
			}
			Boolean change = false;
			if (event.getIsFixedAmount() != null) {
				if (event.getIsFixedAmount().equals(request.getIsFixedAmount())
						|| !event.getIsPaid().equals(request.getIsPaid()))
					change = true;
				else if (event.getIsFixedAmount()) {
					if (!event.getFixedCost().equals(request.getCostToSplit()))
						change = true;
				} else if (!event.getCostToSplit().equals(
						request.getCostToSplit()))
					change = true;
			}
			if (!request.getIsPaid())
				event.setIsPaid(false);
			else {
				event.setIsPaid(true);
				if (request.getIsFixedAmount()) {
					event.setFixedCost(request.getCostToSplit());
					event.setIsFixedAmount(true);
				} else {
					event.setCostToSplit(request.getCostToSplit());
					event.setIsFixedAmount(false);
				}
			}
			if (change) {
				event.setMinPlaces(request.getMinPlaces());
				event.setMaxPlaces(request.getMaxPlaces());
			} else {
				if (event.getMinPlaces() > request.getMinPlaces()
						&& event.getHasOrganiserConfirmed())
					throw new AppException(new AppErrorMessage("400",
							"You cannot reduce min places after confirmation"));
				else {
					if (event.getInterestedUserIDs().size() > event
							.getMinPlaces()
							&& event.getInterestedUserIDs().size() > request
									.getMinPlaces())
						event.setMinPlaces(request.getMinPlaces());
				}
				if (event.getTotalAttending() > request.getMaxPlaces()) {
					throw new AppException(
							new AppErrorMessage("400",
									"You cannot reduce max places below confirmed People"));
				} else
					event.setMaxPlaces(request.getMaxPlaces());
			}
			event.setHasOrganiserConfirmed(true);
			List<UUID> queuedUserIDs = new ArrayList<UUID>();
			if (event.getQueuedUserIDs() != null)
				queuedUserIDs.addAll(event.getQueuedUserIDs());
			List<UUID> userIDs = new ArrayList<UUID>();
			if (change) {
				event.setQueuedUserIDs(userIDs);
				event.setUnConfirmedUserIDs(userIDs);
				event.setConfirmedUserIDs(userIDs);
				userIDs.addAll(event.getInterestedUserIDs());
				userIDs.remove(event.getOrganizerUserID());
				event.setTotalAttending(1);
				List<UserEventInbox> userEventsInbox = userEventInboxDao
						.selectEventByUserIdsAndEventId(userIDs,
								event.getEventID());
				for (UserEventInbox userEvent : userEventsInbox) {
					userEvent.setIsUserConfirmed(false);
					userEvent.setPlacesFilled(event.getTotalAttending());
					userEvent.setPlacesToFill(event.getMaxPlaces()
							- event.getTotalAttending());
				}
				List<OwesMe> payments = owesMeDao.selectPaymentOfEvent(event
						.getEventID());
				for (OwesMe payment : payments) {
					if ((payment.getDatePaidElectronically() != null || payment
							.getDateMarkedAsPaid() != null)
							&& payment.getDateRefunded() == null)
						throw new AppException(new AppErrorMessage("400",
								"please refund before changing payment"));
				}
				userEventInboxDao.update(userEventsInbox);
				if (payments.size() > 0)
					owesMeDao.delete(payments);
				iOweDao.delete(event.getEventID());
				userIDs = new ArrayList<UUID>();
			} else {
				List<UUID> confirmedUserIds = new ArrayList<UUID>();
				if (event.getConfirmedUserIDs() != null)
					confirmedUserIds.addAll(event.getConfirmedUserIDs());
				userIDs.add(event.getOrganizerUserID());
				while (event.getMaxPlaces() > event.getTotalAttending()
						&& event.getQueuedUserIDs() != null
						&& event.getQueuedUserIDs().size() > 0) {
					userIDs.add(queuedUserIDs.get(0));
					queuedUserIDs.remove(0);
					event.setTotalAttending(event.getTotalAttending() + 1);
				}
				event.setQueuedUserIDs(queuedUserIDs);

				List<UserEventInbox> userEventsInbox = userEventInboxDao
						.selectEventByUserIdsAndEventId(userIDs,
								event.getEventID());
				for (UserEventInbox userEvent : userEventsInbox) {
					userEvent.setIsUserConfirmed(true);
					confirmedUserIds.add(userEvent.getPk().getUserID());
				}
				event.setConfirmedUserIDs(confirmedUserIds);
				userEventInboxDao.update(userEventsInbox);
				userIDs.remove(event.getOrganizerUserID());

				userEventsInbox = userEventInboxDao.findUsersByEventId(event
						.getEventID());
				for (UserEventInbox eventInbox : userEventsInbox) {
					eventInbox.setPlacesFilled(event.getTotalAttending());
					eventInbox.setPlacesToFill(event.getMaxPlaces()
							- event.getTotalAttending());
				}
				userEventInboxDao.update(userEventsInbox);
			}

			if (request.getIsFixedAmount()) {
				List<OwesMe> payments = new ArrayList<OwesMe>();
				List<IOwe> owes = new ArrayList<IOwe>();
				for (UUID userId : userIDs) {
					OwesMe payment = new OwesMe(new OwesMeKey(
							event.getOrganizerUserID(), event.getEventID(),
							userId));
					payment.setAmountOwed(event.getFixedCost());
					payment.setCurrency(event.getCurrency());
					payment.setEventTitle(event.getTitle());
					payment.setEventDate(event.getEventDate());
					payment.setOwingUserPhotoPath(user.getPhotoPath());
					payment.setOwingUserName(user.getFullName());
					payments.add(payment);
					IOwe owe = new IOwe(
							new IOweKey(userId, event.getEventID()),
							event.getOrganizerUserID(), event.getFixedCost());
					owes.add(owe);
				}
				owesMeDao.save(payments);
				iOweDao.save(owes);
			}
			eventDao.update(event);
			List<UserEventInbox> eventsInbox = userEventInboxDao
					.findUsersByEventId(event.getEventID());
			for (UserEventInbox eventInbox : eventsInbox) {
				eventInbox.setIsEventConfirmed(true);
				eventInbox.setEventDate(event.getEventDate());
			}
			userEventInboxDao.update(eventsInbox);
			commonService.confirmedMessage(event.getChatId(), user,"I have Confirmed the Details of this Event.");
			return new Status("Success");
		} else
			throw new AppException(new AppErrorMessage("400",
					"You are not allowed"));
	}

	@Override
	public Status editPlaces(UUID userId, InteractRequest request) {
		Event event = eventDao.selectEvent(request.getEventId());
		if (event != null
				&& (event.getOrganizerUserID() != null || event
						.getOrganizerUserID().equals(userId))) {
			if (event.getMinPlaces() > request.getMinPlaces()
					&& event.getHasOrganiserConfirmed())
				throw new AppException(new AppErrorMessage("400",
						"You cannot reduce min places after confirmation"));
			else {
				if (event.getInterestedUserIDs().size() > event.getMinPlaces()
						&& event.getInterestedUserIDs().size() > request
								.getMinPlaces())
					event.setMinPlaces(request.getMinPlaces());
			}
			if (event.getTotalAttending() > request.getMaxPlaces()) {
				throw new AppException(new AppErrorMessage("400",
						"You cannot reduce max places below confirmed People"));
			} else
				event.setMaxPlaces(request.getMaxPlaces());
			eventDao.update(event);
			List<UserEventInbox> eventsInbox = userEventInboxDao
					.findUsersByEventId(event.getEventID());
			for (UserEventInbox eventInbox : eventsInbox) {
				eventInbox.setIsFromPublic(event.getIsPublic());
				eventInbox.setPeopleGathered(event.getInterestedUserIDs()
						.size());
				eventInbox.setPeopleToGather(event.getMaxPlaces()
						- event.getInterestedUserIDs().size());
			}
			userEventInboxDao.update(eventsInbox);
			return new Status("Success");
		} else
			throw new AppException(new AppErrorMessage("400",
					"You are not allowed"));
	}

	@Override
	public Status bookingCancel(User user, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		if (event != null
				&& (event.getOrganizerUserID() != null || event
						.getOrganizerUserID().equals(user.getUserID()))) {
			if (event.getEventStartTime() != null
					&& event.getEventStartTime().before(new Date()))
				throw new AppException(new AppErrorMessage("400",
						"You can't cancel a started event"));
			if (event.getEventEndTime() != null
					&& event.getEventEndTime().before(new Date()))
				throw new AppException(new AppErrorMessage("400",
						"You can't cancel a finished event"));

			List<OwesMe> payments = owesMeDao.selectPaymentOfEvent(event
					.getEventID());
			for (OwesMe payment : payments) {
				if ((payment.getDatePaidElectronically() != null || payment
						.getDateMarkedAsPaid() != null)
						&& payment.getDateRefunded() == null)
					throw new AppException(new AppErrorMessage("400",
							"please refund before canceling event"));
			}
			event.setHasOrganiserConfirmed(false);
			event.setIsPaid(null);
			event.setIsFixedAmount(null);
			event.setCostToSplit(new BigDecimal("0.0"));
			event.setFixedCost(new BigDecimal("0.0"));
			List<UUID> userIDs = new ArrayList<UUID>();
			event.setQueuedUserIDs(userIDs);
			event.setUnConfirmedUserIDs(userIDs);
			event.setConfirmedUserIDs(userIDs);
			event.setTotalAttending(0);
			eventDao.update(event);
			List<UserEventInbox> eventsInbox = userEventInboxDao
					.findUsersByEventId(event.getEventID());
			for (UserEventInbox eventInbox : eventsInbox) {
				eventInbox.setIsUserConfirmed(false);
				eventInbox.setIsEventConfirmed(false);
				eventInbox.setEventDate(null);
				eventInbox.setPlacesFilled(event.getTotalAttending());
				eventInbox.setPlacesToFill(event.getMaxPlaces()
						- event.getTotalAttending());
			}
			userEventInboxDao.update(eventsInbox);
			if (event.getIsFixedAmount() != null && event.getIsFixedAmount()) {
				if (payments.size() > 0)
					owesMeDao.delete(payments);
				iOweDao.delete(event.getEventID());
			}
			commonService.confirmedMessage(event.getChatId(), user,"I have deleted the Details of this Event.");
             return new Status("Success");
		} else
			throw new AppException(new AppErrorMessage("400",
					"You are not allowed"));
	}

	@Override
	public Status gatherAgain(UUID userId, UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		event.setEventID(Event.getUUID());
		event.setGathered1stByUserID(userId);
		event.setGathered1stDate(new Date());
		List<UUID> interestedUserIDs = event.getInterestedUserIDs();
		interestedUserIDs.add(userId);
		event.setInterestedUserIDs(interestedUserIDs);
		event.setEventDate(null);
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 3);
		event.setEventExpiry(cal.getTime());
		event.setEventStartTime(null);
		event.setEventEndTime(null);
		event.setIsChat(false);
		event.setIsHistory(false);
		event.setIsDeleted(false);
		event.setInterestedUserIDs(new ArrayList<UUID>());
		event.setHasOrganiserConfirmed(false);
		event.setIsPaid(null);
		event.setCostToSplit(new BigDecimal("0.0"));
		event.setFixedCost(new BigDecimal("0.0"));
		eventDao.save(event);

		userInterestedEventDao.save(new UserInterestedEvent(
				new UserInterestedEventKey(userId, event.getEventID())));
		UserEventInbox eventInbox = new UserEventInbox(new UserEventInboxKey(
				userId, event.getEventID()), event.getIsPublic());
		eventInbox.setEventTitle(event.getTitle());
		eventInbox.setPeopleGathered(event.getInterestedUserIDs().size());
		eventInbox.setPeopleToGather(event.getMaxPlaces()
				- event.getInterestedUserIDs().size());
		eventInbox.setPlacesFilled(event.getTotalAttending());
		eventInbox.setPlacesToFill(event.getMaxPlaces()
				- event.getTotalAttending());
		eventInbox.setChatId(UUID.fromString(event.getChatId()));
		userEventInboxDao.save(eventInbox);
		return new Status("Success");
	}

	@Override
	public Status getFeedback(UUID userID, UUID eventId) {
		UserEventFeedback feedback = userEventFeedbackDao.selectUserFeedback(
				eventId, userID);
		Status status = new Status();
		if (feedback == null)
			return status;
		status.setRating(feedback.getRating());
		return status;
	}

	@Override
	public Status feedback(UUID userID, InteractRequest request) {
		UserEventFeedback feedback = userEventFeedbackDao.selectUserFeedback(
				request.getEventId(), userID);
		if (feedback == null) {
			feedback = new UserEventFeedback(new UserEventFeedbackKey(
					request.getEventId(), userID, new Date()),
					request.getRating(), request.getIssue());
			userEventFeedbackDao.save(feedback);
		} else {
			feedback.setRating(request.getRating());
			userEventFeedbackDao.update(feedback);
		}
		return new Status("Success");
	}

	@Override
	public Status getPicture(UUID eventId, UUID userId) {
		Status status = new Status();
		List<EventHistoryPicture> pictures = eventHistoryPictureDao
				.findByEventId(eventId);
		List<PictureList> pictureList = new ArrayList<PictureList>();
		if (pictures != null && pictures.size() > 0) {
			for (EventHistoryPicture e : pictures) {
				PictureList pic = new PictureList(e.getPk().getImagePath(), "",
						e.getLikedBy(), userDao.findById(e.getPostedBy())
								.getFullName(), e.getPk().getPostedDate(),
						e.getNumberOfLikes(), e.getComment());
				if (e.getLikedBy() != null)
					pic.setIsLiked(e.getLikedBy().contains(userId));
				pictureList.add(pic);
			}
		}
		status.setPictureList(pictureList);
		UserEventFeedback feedback = userEventFeedbackDao.selectUserFeedback(
				eventId, userId);
		if (feedback == null)
			return status;
		status.setRating(feedback.getRating());
		return status;
	}

	@Override
	public Status addPicture(UUID userID, InteractRequest request) {
		EventHistoryPicture picture = new EventHistoryPicture(
				new EventHistoryPictureKey(request.getEventId(), new Date(),
						request.getPicturePath()), userID);
		picture.setComment(request.getComment());
		eventHistoryPictureDao.save(picture);
		Event event = eventDao.selectEvent(request.getEventId());
		User user=userDao.findById(userID);
		commonService.confirmedMessage(event.getChatId(), user,"I Added One New Picture..! ");
		return new Status("Success");
	}

	@Override
	public Status unlikePicture(UUID eventId, Date pictureDate, UUID userId) {
		EventHistoryPicture eventPicture = eventHistoryPictureDao
				.findByEventAndPictureDate(eventId, pictureDate);
		List<UUID> likedBy = new ArrayList<UUID>();
		if (eventPicture.getLikedBy() != null)
			likedBy.addAll(eventPicture.getLikedBy());
		if (likedBy != null && likedBy.contains(userId))
			likedBy.remove(userId);
		eventPicture.setLikedBy(likedBy);
		eventPicture.setNumberOfLikes(eventPicture.getLikedBy().size());
		eventHistoryPictureDao.save(eventPicture);
		Event event = eventDao.selectEvent(eventId);
		User user=userDao.findById(userId);
		String postby=userDao.findById(eventPicture.getPostedBy()).getFullName();
		commonService.confirmedMessage(event.getChatId(), user ," UnLiked "+postby+" picture ");
		return new Status("Success");
	}

	@Override
	public Status likePicture(UUID eventId, Date pictureDate, UUID userId) {
		EventHistoryPicture eventPicture = eventHistoryPictureDao
				.findByEventAndPictureDate(eventId, pictureDate);
		List<UUID> likedBy = new ArrayList<UUID>();
		if (eventPicture.getLikedBy() != null)
			likedBy.addAll(eventPicture.getLikedBy());
		if (!likedBy.contains(userId))
			likedBy.add(userId);
		eventPicture.setLikedBy(likedBy);
		eventPicture.setNumberOfLikes(eventPicture.getLikedBy().size());
		eventHistoryPictureDao.save(eventPicture);
		Event event = eventDao.selectEvent(eventId);
		User user=userDao.findById(userId);
		String postby=userDao.findById(eventPicture.getPostedBy()).getFullName();
		commonService.confirmedMessage(event.getChatId(),user ,"I Likes this picture posted by "+postby);
		return new Status("Success");
	}

	@Override
	public Status eventLocation(InteractRequest request, UUID userId) {
		Event event = eventDao.selectEvent(request.getEventId());
		if (!event.getOrganizerUserID().equals(userId))
			throw new AppException(new AppErrorMessage("400",
					"You are not allowed"));
		event.setChosenLocationLatLong(request.getChosenLocation());
		event.setChosenLocAddr(request.getChosenLocationAddress());
		event.setChosenLocCountry(request.getChosenLocCountry());
		event.setChosenLocAdmin1(request.getChosenLocAdmin1());
		event.setChosenLocAdmin2(request.getChosenLocAdmin2());
		event.setChosenLocAdmin3(request.getChosenLocAdmin3());
		eventDao.update(event);

		return new Status("Success");
	}
}