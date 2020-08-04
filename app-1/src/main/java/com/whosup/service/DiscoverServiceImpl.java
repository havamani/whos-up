package com.whosup.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.datastax.driver.core.Row;
import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.EventDao;
import com.whosup.dao.IdeaDao;
import com.whosup.dao.RegionPopularSearchDao;
import com.whosup.dao.UserContactDao;
import com.whosup.dao.UserDao;
import com.whosup.dao.UserEventInboxDao;
import com.whosup.dao.UserFrequentSearchDao;
import com.whosup.dao.UserInterestedEventDao;
import com.whosup.model.Event;
import com.whosup.model.Idea;
import com.whosup.model.RegionPopularSearch;
import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.UserDiscoverInbox;
import com.whosup.model.UserDiscoverInboxKey;
import com.whosup.model.UserEventInbox;
import com.whosup.model.UserEventInboxKey;
import com.whosup.model.UserFrequentSearch;
import com.whosup.model.UserInterestedEvent;
import com.whosup.model.UserInterestedEventKey;
import com.whosup.model.request.DiscoverRequest;
import com.whosup.model.request.NetworkRequest;
import com.whosup.model.response.DiscoverList;
import com.whosup.model.response.DiscoverSource;
import com.whosup.model.response.EventViewResponse;
import com.whosup.model.response.Status;

public class DiscoverServiceImpl implements DiscoverService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private IdeaDao ideaDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private NetworkService networkService;
	@Autowired
	private UserContactDao userContactDao;
	@Autowired
	private UserEventInboxDao userEventInboxDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserInterestedEventDao userInterestedEventDao;
	@Autowired
	private UserFrequentSearchDao userFrequentSearchDao;
	@Autowired
	private RegionPopularSearchDao regionPopularSearchDao;

	@Override
	public DiscoverSource eventList(String location) {

		DiscoverSource discoverSource = new DiscoverSource();
		List<DiscoverList> discoverEvent = findForEvent(eventDao
				.findPublicEventsByLocation(location));
		discoverSource
				.setEventSource4(discoverEvent.size() > 32 ? discoverEvent
						.subList(0, 32) : discoverEvent);
		NetworkRequest request = commonService.locationDetails(location);
		Map<Idea, Double> ideaSource;
		if (request != null && request.getAdmin1() != null
				&& !request.getAdmin1().isEmpty()
				&& request.getAdmin1().length() > 1
				&& request.getAdmin2() != null
				&& !request.getAdmin2().isEmpty()
				&& request.getAdmin2().length() > 1
				&& request.getAdmin3() != null
				&& !request.getAdmin3().isEmpty()
				&& request.getAdmin3().length() > 1)
			ideaSource = ideaDao.getIdeaFromSource3(request.getAdmin1(),
					request.getAdmin2(), request.getAdmin3(),
					request.getCountry(), null);
		else
			ideaSource = new HashMap<Idea, Double>();
		List<DiscoverList> discoverIdea = findForIdea(ideaSource);
		List<DiscoverList> discoverIdeaSource3 = sortingDiscoverList(discoverIdea);
		ideaSource = ideaDao.getIdeaFromSource4(request.getCountry(), null);
		discoverIdea = findForIdea(ideaSource);
		List<DiscoverList> discoverIdeaSource4 = sortingDiscoverList(discoverIdea);

		int ideaSize = discoverIdeaSource3.size() + discoverIdeaSource4.size();
		List<DiscoverList> ideasWithoutDuplicate = new ArrayList<DiscoverList>();
		if (ideaSize != 0) {
			discoverSource
					.setIdeaSource3(discoverIdeaSource3.size() > 5 ? discoverEvent
							.subList(0, 5) : discoverEvent);
			discoverSource
					.setIdeaSource4(discoverIdeaSource4.size() > 5 ? discoverIdeaSource4
							.subList(0, 5) : discoverEvent);
			ideasWithoutDuplicate.addAll(discoverIdeaSource3);
			List<DiscoverList> currentIdeaSource = new ArrayList<DiscoverList>();
			currentIdeaSource.addAll(discoverIdeaSource4);
			for (DiscoverList currentIdea : currentIdeaSource) {
				for (DiscoverList duplicateremoval : ideasWithoutDuplicate) {
					if (currentIdea
							.getInbox()
							.getPk()
							.getEventID()
							.equals(duplicateremoval.getInbox().getPk()
									.getEventID())) {
						discoverIdeaSource4.remove(currentIdea);
						break;
					}
				}
			}
			ideasWithoutDuplicate.addAll(discoverIdeaSource4);
			if (ideasWithoutDuplicate.size() < 10) {
				// List<DiscoverList> ideaSource5 = findForIdea(ideaDao
				// .findAll(10 - ideaSize));
				List<DiscoverList> ideaSource5 = findForIdea(ideaDao
						.findRandomIdeas(10 - ideasWithoutDuplicate.size()));
				discoverIdea = new ArrayList<DiscoverList>();
				for (DiscoverList d : ideaSource5) {
					boolean isAlreadyAvailable = false;
					for (DiscoverList d1 : ideasWithoutDuplicate) {
						if (d1.getInbox().getPk().getEventID()
								.equals(d.getInbox().getPk().getEventID())) {
							isAlreadyAvailable = true;
							break;
						}
					}
					if (!isAlreadyAvailable) {
						discoverIdea.add(d);
					}
				}
				discoverSource.setIdeaSource5(ideaSource5);
			}
		} else {
			// List<DiscoverList> ideaSource5 =
			// findForIdea(ideaDao.findAll(10));
			List<DiscoverList> ideaSource5 = findForIdea(ideaDao
					.findRandomIdeas(10));
			discoverIdea = new ArrayList<DiscoverList>();
			for (DiscoverList d : ideaSource5) {
				boolean isAlreadyAvailable = false;
				for (DiscoverList d1 : ideasWithoutDuplicate) {
					if (d1.getInbox().getPk().getEventID()
							.equals(d.getInbox().getPk().getEventID())) {
						isAlreadyAvailable = true;
						break;
					}
				}
				if (!isAlreadyAvailable) {
					discoverIdea.add(d);
				}
			}
			discoverSource.setIdeaSource5(ideaSource5);
		}
		return discoverSource;
	}

	@Override
	public DiscoverSource eventList(User user, String location) {

		List<UserContact> starredContacts = userContactDao.findMyCrowd(user
				.getUserID());
		/* Fetching and storing user's Starred Contact */
		List<UUID> starredContactIds = new ArrayList<UUID>();
		for (UserContact e : starredContacts) {
			starredContactIds.add(e.getPk().getContactUserID());
		}

		/* Getting location details using Lat Long */
		NetworkRequest request = commonService.locationDetails(location);

		/* Getting Idea Source from user starred contacts */
		Map<Idea, Double> ideaSource = ideaDao.getIdeaFromSource1(
				starredContactIds, null);
		List<DiscoverList> discoverIdea = findForIdea(ideaSource);
		List<DiscoverList> discoverIdeaSource1 = sortingDiscoverList(discoverIdea);

		/* Getting idea source from user's domain */
		ideaSource = ideaDao.getIdeaFromSource2(
				user.getVerifiedDomainEmail() != null
						&& !user.getVerifiedDomainEmail().isEmpty() ? user
						.getVerifiedDomainEmail().substring(
								user.getVerifiedDomainEmail().indexOf('@') + 1)
						: "", null);
		discoverIdea = findForIdea(ideaSource);
		List<DiscoverList> discoverIdeaSource2 = sortingDiscoverList(discoverIdea);

		ideaSource = ideaDao.getIdeaFromSource4(request.getCountry(), null);

		/* Getting Idea Source From retrieved location details */
		if (request != null && request.getAdmin1() != null
				&& !request.getAdmin1().isEmpty()
				&& request.getAdmin1().length() > 1
				&& request.getAdmin2() != null
				&& !request.getAdmin2().isEmpty()
				&& request.getAdmin2().length() > 1
				&& request.getAdmin3() != null
				&& !request.getAdmin3().isEmpty()
				&& request.getAdmin3().length() > 1)
			ideaSource = ideaDao.getIdeaFromSource3(request.getAdmin1(),
					request.getAdmin2(), request.getAdmin3(),
					request.getCountry(), null);
		else
			ideaSource = new HashMap<Idea, Double>();
		discoverIdea = findForIdea(ideaSource);
		List<DiscoverList> discoverIdeaSource3 = sortingDiscoverList(discoverIdea);

		/* Getting idea source using the country fetched in location details */
		ideaSource = ideaDao.getIdeaFromSource4(request.getCountry(), null);
		discoverIdea = findForIdea(ideaSource);
		List<DiscoverList> discoverIdeaSource4 = sortingDiscoverList(discoverIdea);

		DiscoverSource discoverSource = new DiscoverSource();
		int ideaSize = discoverIdeaSource1.size() + discoverIdeaSource2.size()
				+ discoverIdeaSource3.size() + discoverIdeaSource4.size();
		List<DiscoverList> ideasWithoutDuplicate = new ArrayList<DiscoverList>();

		/* Arranging idea's and removing repeated ideas from various sources */
		if (ideaSize != 0) {
			discoverSource
					.setIdeaSource1(discoverIdeaSource1.size() > 5 ? discoverIdeaSource1
							.subList(0, 5) : discoverIdeaSource1);
			discoverSource
					.setIdeaSource2(discoverIdeaSource2.size() > 5 ? discoverIdeaSource2
							.subList(0, 5) : discoverIdeaSource2);
			discoverSource
					.setIdeaSource3(discoverIdeaSource3.size() > 5 ? discoverIdeaSource3
							.subList(0, 5) : discoverIdeaSource3);
			discoverSource
					.setIdeaSource4(discoverIdeaSource4.size() > 5 ? discoverIdeaSource4
							.subList(0, 5) : discoverIdeaSource4);

			discoverSource.setIdeaSource2(discoverIdeaSource2);
			discoverSource.setIdeaSource3(discoverIdeaSource3);
			discoverSource.setIdeaSource4(discoverIdeaSource4);
			List<DiscoverList> currentIdeaSource = new ArrayList<DiscoverList>();
			currentIdeaSource.addAll(discoverIdeaSource1);
			Set<UUID> ideasWithoutDuplicateEventIDs = new HashSet<UUID>();
			for (DiscoverList currentIdea : currentIdeaSource) {
				if (!ideasWithoutDuplicateEventIDs.add(currentIdea.getInbox()
						.getPk().getEventID())) {
					discoverIdeaSource1.remove(currentIdea);
				}
			}
			discoverSource.setIdeaSource1(discoverIdeaSource1);
			ideasWithoutDuplicate.addAll(discoverIdeaSource1);
			currentIdeaSource.clear();
			currentIdeaSource.addAll(discoverIdeaSource2);
			for (DiscoverList currentIdea : currentIdeaSource) {
				for (DiscoverList duplicateremoval : ideasWithoutDuplicate) {
					if (currentIdea
							.getInbox()
							.getPk()
							.getEventID()
							.equals(duplicateremoval.getInbox().getPk()
									.getEventID())) {
						discoverIdeaSource2.remove(currentIdea);
						break;
					}
				}
			}
			ideasWithoutDuplicate.addAll(discoverIdeaSource2);
			currentIdeaSource.clear();
			currentIdeaSource.addAll(discoverIdeaSource3);
			for (DiscoverList currentIdea : currentIdeaSource) {
				for (DiscoverList duplicateremoval : ideasWithoutDuplicate) {
					if (currentIdea
							.getInbox()
							.getPk()
							.getEventID()
							.equals(duplicateremoval.getInbox().getPk()
									.getEventID())) {
						discoverIdeaSource3.remove(currentIdea);
						break;
					}
				}
			}
			ideasWithoutDuplicate.addAll(discoverIdeaSource3);
			currentIdeaSource.clear();
			currentIdeaSource.addAll(discoverIdeaSource4);
			for (DiscoverList currentIdea : currentIdeaSource) {
				for (DiscoverList duplicateremoval : ideasWithoutDuplicate) {
					if (currentIdea
							.getInbox()
							.getPk()
							.getEventID()
							.equals(duplicateremoval.getInbox().getPk()
									.getEventID())) {
						discoverIdeaSource4.remove(currentIdea);
						break;
					}
				}
			}
			ideasWithoutDuplicate.addAll(discoverIdeaSource4);

			if (ideasWithoutDuplicate.size() < 10) {
				// List<DiscoverList> ideaSource5 = findForIdea(ideaDao
				// .findAll(10 - ideaSize));
				List<DiscoverList> ideaSource5 = findForIdea(ideaDao
						.findRandomIdeas(10 - ideasWithoutDuplicate.size()));
				discoverIdea = new ArrayList<DiscoverList>();
				for (DiscoverList d : ideaSource5) {
					boolean isAlreadyAvailable = false;
					for (DiscoverList d1 : ideasWithoutDuplicate) {
						if (d1.getInbox().getPk().getEventID()
								.equals(d.getInbox().getPk().getEventID())) {
							isAlreadyAvailable = true;
							break;
						}
					}
					if (!isAlreadyAvailable) {
						discoverIdea.add(d);
					}
				}
				discoverSource.setIdeaSource5(discoverIdea);
			}
		} else {
			// List<DiscoverList> ideaSource5 =
			// findForIdea(ideaDao.findAll(10));
			List<DiscoverList> ideaSource5 = findForIdea(ideaDao
					.findRandomIdeas(10));
			// discoverIdea = new ArrayList<DiscoverList>();
			// for (DiscoverList d : ideaSource5) {
			// boolean isAlreadyAvailable = false;
			// for (DiscoverList d1 : ideasWithoutDuplicate) {
			// if (d1.getInbox().getPk().getEventID()
			// .equals(d.getInbox().getPk().getEventID())) {
			// isAlreadyAvailable = true;
			// break;
			// }
			// }
			// if (!isAlreadyAvailable) {
			// discoverIdea.add(d);
			// }
			// }
			discoverSource.setIdeaSource5(ideaSource5);
		}
		List<Event> eventSource = eventDao.findStarredEvents(user.getUserID(),
				null);
		List<DiscoverList> discoverEvent1 = findForEvent(eventSource);
		eventSource = new ArrayList<Event>(eventDao.findDiscoverEvents(
				user.getUserID(), null));
		List<DiscoverList> discoverEvent2 = findForEvent(eventSource);
		eventSource = eventDao.findDomainEvents(
				user.getVerifiedDomainEmail() != null
						&& !user.getVerifiedDomainEmail().isEmpty() ? user
						.getVerifiedDomainEmail().substring(
								user.getVerifiedDomainEmail().indexOf('@') + 1)
						: null, user.getUserID());
		List<DiscoverList> discoverEvent3 = findForEvent(eventSource);
		eventSource = new ArrayList<Event>(eventDao.findDiscoverPublicEvents(
				user.getUserID(), location, null));
		List<DiscoverList> discoverEvent4 = findForEvent(eventSource);
		if (discoverEvent1.size() > 8 && discoverEvent2.size() > 8) {
			discoverSource
					.setEventSource1(discoverEvent1.size() > 8 ? discoverEvent1
							.subList(0, 8) : discoverEvent1);
			discoverSource
					.setEventSource2(discoverEvent2.size() > 8 ? discoverEvent2
							.subList(0, 8) : discoverEvent2);
		} else if (discoverEvent1.size() < 8 && discoverEvent2.size() < 8) {
			discoverSource.setEventSource1(discoverEvent1);
			discoverSource.setEventSource2(discoverEvent2);
		} else if (discoverEvent1.size() > 8 && discoverEvent2.size() < 8) {
			int temp = 8 - discoverEvent2.size();
			discoverSource
					.setEventSource1(discoverEvent1.size() > temp ? discoverEvent1
							.subList(0, temp) : discoverEvent1);
			discoverSource.setEventSource2(discoverEvent2);
		} else if (discoverEvent1.size() < 8 && discoverEvent2.size() > 8) {
			int temp = 8 - discoverEvent1.size();
			discoverSource
					.setEventSource2(discoverEvent2.size() > temp ? discoverEvent2
							.subList(0, temp) : discoverEvent2);
			discoverSource.setEventSource1(discoverEvent1);
		}
		discoverSource
				.setEventSource3(discoverEvent3.size() > 8 ? discoverEvent3
						.subList(0, 8) : discoverEvent3);
		discoverSource
				.setEventSource4(discoverEvent4.size() > 8 ? discoverEvent4
						.subList(0, 8) : discoverEvent4);
		List<String> eventsFromEventBrite = eventDao
				.findEventFromEventBrite(user.getUserID());
		discoverSource.setEventBriteWebLinks(eventsFromEventBrite);
		return discoverSource;

	}

	protected List<DiscoverList> sortingDiscoverList(List<DiscoverList> discover) {
		Collections.sort(discover, new Comparator<DiscoverList>() {
			public int compare(DiscoverList o1, DiscoverList o2) {
				return o1.getMomentum().compareTo(o2.getMomentum());
			}
		});
		return discover;
	}

	@Override
	public Status createEvent(DiscoverRequest request, User user) {
		Event event = new Event(Event.getUUID(), user.getUserID(), new Date(),
				request.getIdeaID());
		Idea idea = ideaDao.findById(request.getIdeaID());
		List<String> keywords = new ArrayList<String>();
		if (idea != null) {
			if (idea.getTagsPeople() != null && !idea.getTagsPeople().isEmpty())
				keywords.add(idea.getTagsPeople());
			if (idea.getTagsExperience() != null
					&& !idea.getTagsExperience().isEmpty())
				keywords.add(idea.getTagsExperience());
			if (idea.getTagsEnvironment() != null
					&& !idea.getTagsEnvironment().isEmpty())
				keywords.add(idea.getTagsEnvironment());
			if (idea.getPrimaryCategory() != null
					&& !idea.getPrimaryCategory().isEmpty())
				keywords.add(idea.getPrimaryCategory());
			if (idea.getSecondaryCategory() != null
					&& !idea.getSecondaryCategory().isEmpty())
				keywords.add(idea.getSecondaryCategory());
		}
		event.setKeywords(keywords);
		event.setTitle(request.getEventName());
		event.setSmallPhotoPath(photoResize(request.getSmallPhotoPath()));
		event.setLargePhotoPath(request.getLargePhotoPath());
		event.setDescription(request.getDescription());
		event.setWebLink(request.getWebLink());

		if (request.getEventDate() != null)
			event.setEventDate(request.getEventDate());
		event.setChosenLocationLatLong(request.getChosenLocation());
		event.setChosenLocAddr(request.getChosenAddress());
		event.setChosenLocCountry(request.getChosenLocCountry());
		event.setChosenLocAdmin1(request.getChosenLocAdmin1());
		event.setChosenLocAdmin2(request.getChosenLocAdmin2());
		event.setChosenLocAdmin3(request.getChosenLocAdmin3());
		List<UUID> interestedUserIDs = new ArrayList<UUID>();
		interestedUserIDs.add(user.getUserID());
		event.setInterestedUserIDs(interestedUserIDs);
		event.setQueuedUserIDs(new ArrayList<UUID>());
		event.setUnConfirmedUserIDs(new ArrayList<UUID>());
		event.setConfirmedUserIDs(new ArrayList<UUID>());
		event.setIsPublic(request.getIsPublic() == null ? false : request
				.getIsPublic());
		if (request.getEventStartTime() != null
				&& request.getEventEndTime() != null) {
			event.setEventStartTime(request.getEventStartTime());
			event.setEventEndTime(request.getEventEndTime());
		}
		event.setMinPlaces((request.getMinPlaces() == null || request
				.getMinPlaces() < 2) ? 3 : request.getMinPlaces());
		event.setMaxPlaces((request.getMaxPlaces() == null || request
				.getMaxPlaces() == 0) ? 25 : request.getMaxPlaces());
		event.setGatherAdvancedLocAddr(request.getGatherAddress());
		if (request.getGatherLocation() != null
				&& request.getGatherLocation().contains(","))
			event.setGatherAdvancedLocationLatLong(request.getGatherLocation());
		event.setGatherAdvancedLocCountry(request.getGatherLocCountry());
		event.setGatherAdvancedLocAdmin1(request.getGatherLocAdmin1());
		event.setGatherAdvancedLocAdmin2(request.getGatherLocAdmin2());
		event.setGatherAdvancedLocAdmin3(request.getGatherLocAdmin3());
		event.setGatherAdvancedLocRadius(request.getGatherRadius());
		event.setGatherAdvancedAgeLower(request.getMinAge() == null ? 13
				: request.getMinAge());
		event.setGatherAdvancedAgeUpper(request.getMaxAge() == null ? 80
				: request.getMaxAge());
		event.setGatherAdvancedFemaleOnly(request.getFemaleOnly() == null ? false
				: request.getFemaleOnly());
		if (request.getGatherFrom() != null) {
			if (request.getGatherFrom().contains("fb"))
				event.setGatherAdvancedIsFbSelected(true);
			if (request.getGatherFrom().contains("ln"))
				event.setGatherAdvancedIsLiSelected(true);
			if (request.getGatherFrom().contains("whosup"))
				event.setGatherAdvancedIsWuSelected(true);
			if (request.getGatherFrom().contains("my_net"))
				event.setGatherAdvancedIsNetworkSelected(true);
			if (request.getMyCrowdOnly())
				event.setGatherAdvancedIsStarredFilterSelected(true);
		}
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 3);
		cal.set(Calendar.MINUTE, 0);
		event.setEventExpiry(cal.getTime());
		if (request.getContactIDs() != null) {
			if (request.getContactIDs().size() > 0)
				event.setVisibleToUserIDs(request.getContactIDs());
			else
				throw new AppException(new AppErrorMessage("400",
						"Please select some users"));
		} else if (request.getGatherFrom() != null) {
			List<UserContact> contacts = userContactDao
					.findContactsByGatherFrom(user.getUserID(),
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
		event.setMass(1.0 / event.getMaxPlaces());
		eventDao.save(event);
		event.setChatId(commonService.createChatRoom(event));
		eventDao.update(event);
		userInterestedEventDao
				.save(new UserInterestedEvent(new UserInterestedEventKey(user
						.getUserID(), event.getEventID())));
		UserEventInbox eventInbox = new UserEventInbox(new UserEventInboxKey(
				user.getUserID(), event.getEventID()), event.getIsPublic());
		eventInbox.setEventTitle(event.getTitle());
		eventInbox.setPeopleGathered(event.getInterestedUserIDs().size());
		eventInbox.setPeopleToGather(event.getMaxPlaces()
				- event.getInterestedUserIDs().size());
		eventInbox.setPlacesFilled(event.getTotalAttending());
		eventInbox.setPlacesToFill(event.getMaxPlaces()
				- event.getTotalAttending());
		eventInbox.setEventDate(event.getEventDate());
		eventInbox.setChatId(UUID.fromString(event.getChatId()));
		userEventInboxDao.save(eventInbox);
		List<UserContact> starredContacts = userContactDao.findMyCrowd(user
				.getUserID());
		for (UserContact userContact : starredContacts) {
			ideaDao.updateStarredIdeaMomentum(userContact.getPk().getUserID(),
					request.getIdeaID());
		}
		String companyDomain = !user.getVerifiedDomainEmail().isEmpty() ? user
				.getVerifiedDomainEmail().substring(
						user.getVerifiedDomainEmail().indexOf('@') + 1) : null;
		NetworkRequest locationDetails = commonService.locationDetails(request
				.getChosenLocation());
		if (request.getIdeaID() != null && companyDomain != null)
			ideaDao.updateIdeaDomainMomentum(request.getIdeaID(), companyDomain);
		if (request.getIdeaID() != null && locationDetails.getCountry() != null)
			ideaDao.updateIdeaLocationMomentum(request.getIdeaID(),
					locationDetails);
		JSONObject joiners = new JSONObject();
		for (UUID userID : event.getInterestedUserIDs()) {
			joiners.put(userDao.findById(userID).getFullName(), userID);
		}

		JSONObject notification = new JSONObject();
		notification.put("type", "EVENT_CREATED");
		notification.put("message", user.getFullName()
				+ " has created a New Event " + event.getTitle());
		List<UserContact> myCrowd = userContactDao
				.findMyCrowd(user.getUserID());
		List<UUID> myCrowdIds = new ArrayList<UUID>();
		for (UserContact userContact : myCrowd) {
			if (userContact.getIsConnectedViaEmailDomain() == null
					|| !userContact.getIsConnectedViaEmailDomain())
				myCrowdIds.add(userContact.getContactUserID());
		}
		List<User> myCrowdUsers = userDao.findUserByIds(myCrowdIds);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(10000);
					for (User userForNotification : myCrowdUsers) {
						commonService.notify(userForNotification,
								notification.toString());
					}
				} catch (Exception e) {
					System.out.println("Exception " + e);
				}
			}
		}).start();

		Status status = new Status("Success");
		status.setEventID(event.getEventID());
		return status;
	}

	@Override
	public List<UserContact> gatherFrom(DiscoverRequest request, User user) {
		return userContactDao.findContactsByGatherFrom(user.getUserID(),
				request.getGatherFrom());
	}

	@Override
	public DiscoverSource searchDiscover(String location, String search,
			String searchDate, String searchAfterEve) {
		/*
		 * This block (From line 457 to 464) returns List of Events when Search
		 * is done by using Day/Date Eg:today, today soccer, 26/12/2015 etc..
		 */
		if (searchAfterEve != null && !searchAfterEve.equals("")) {
			if (searchDate == null || searchDate.equals("")) {
				Calendar cal = Calendar
						.getInstance(TimeZone.getTimeZone("GMT"));
				searchDate = cal.get(Calendar.YEAR) + "-"
						+ (cal.get(Calendar.MONTH) + 1) + "-"
						+ cal.get(Calendar.DATE);
			}
			if (search != null && !search.equals("")) {
				List<Event> eventsAfterEvening = eventDao
						.findEventsAfterEvening(location, search, searchDate);
				DiscoverSource discoversource = new DiscoverSource();
				List<DiscoverList> discoverEvent = findForEvent(eventsAfterEvening);
				discoversource.setEventSource1(discoverEvent);
				discoversource.setSearchBy("eveningSearch");
				return discoversource;
			} else {
				List<Event> eventsAfterEvening = eventDao
						.findEventsAfterEvening(location, searchDate);
				DiscoverSource discoversource = new DiscoverSource();
				List<DiscoverList> discoverEvent = findForEvent(eventsAfterEvening);
				discoversource.setEventSource1(discoverEvent);
				discoversource.setSearchBy("evening");
				return discoversource;
			}
		} else if (searchDate != null && !searchDate.equals("")) {
			if (search != null && !search.equals("")) {
				List<Event> findEventsBySearchDate = eventDao
						.findEventsBySearchDate(search, searchDate);
				DiscoverSource discoverSource = new DiscoverSource();
				List<DiscoverList> discoverEvent = findForEvent(findEventsBySearchDate);
				discoverSource.setEventSource1(discoverEvent);
				discoverSource.setSearchBy("date");
				return discoverSource;
			} else {
				List<Event> findEventsBySearchDate = eventDao
						.findEventsBySearchDate(searchDate);
				DiscoverSource discoverSource = new DiscoverSource();
				List<DiscoverList> discoverEvent = findForEvent(findEventsBySearchDate);
				discoverSource.setEventSource1(discoverEvent);
				discoverSource.setSearchBy("date");
				return discoverSource;
			}
		} else {
			DiscoverSource discoverSource = new DiscoverSource();
			/*
			 * This block (From line 471 to 472) returns List of Events nearby
			 * the searched location of the user
			 */
			List<DiscoverList> discoverEvent = findForEvent(eventDao
					.findPublicEventsByLocation(location));
			discoverSource
					.setEventSource4(discoverEvent.size() > 32 ? discoverEvent
							.subList(0, 32) : discoverEvent);
			NetworkRequest request = commonService.locationDetails(location);

			Map<Idea, Double> ideaSource;
			if (request != null && request.getAdmin1() != null
					&& !request.getAdmin1().isEmpty()
					&& request.getAdmin1().length() > 1
					&& request.getAdmin2() != null
					&& !request.getAdmin2().isEmpty()
					&& request.getAdmin2().length() > 1
					&& request.getAdmin3() != null
					&& !request.getAdmin3().isEmpty()
					&& request.getAdmin3().length() > 1)
				/*
				 * This block (From line 492 to 494) returns List of idea in
				 * user's location matches with search term
				 */
				ideaSource = ideaDao.getIdeaFromSource3(request.getAdmin1(),
						request.getAdmin2(), request.getAdmin3(),
						request.getCountry(), search);
			else
				ideaSource = new HashMap<Idea, Double>();
			List<DiscoverList> discoverIdea = findForIdea(ideaSource);
			List<DiscoverList> discoverIdeaSource3 = sortingDiscoverList(discoverIdea);
			/*
			 * This block (From line 503 to 505) returns List of idea in user's
			 * country matches with search term
			 */
			ideaSource = ideaDao.getIdeaFromSource4(request.getCountry(),
					search);
			discoverIdea = findForIdea(ideaSource);
			List<DiscoverList> discoverIdeaSource4 = sortingDiscoverList(discoverIdea);
			ideaSource = ideaDao.findBySearch(search);
			discoverIdea = findForIdea(ideaSource);
			List<DiscoverList> discoverIdeaSource5 = sortingDiscoverList(discoverIdea);

			discoverSource.setIdeaSource3(discoverIdeaSource3);
			discoverSource.setIdeaSource4(discoverIdeaSource4);
			discoverSource.setIdeaSource5(discoverIdeaSource5);
			discoverSource.setSearchBy("keywords");
			return discoverSource;
		}
	}

	@Override
	public DiscoverSource searchDiscover(UUID userId, String location,
			String search, String searchDate, String searchAfterEve) {
		/*
		 * This block (From line 542 to 550) returns List of Events when Search
		 * is done by using Day/Date Eg:today, today soccer, 26/12/2015 etc..
		 */
		if (searchAfterEve != null && !searchAfterEve.equals("")) {
			if (searchDate == null || searchDate.equals("")) {
				Calendar cal = Calendar
						.getInstance(TimeZone.getTimeZone("GMT"));
				searchDate = cal.get(Calendar.YEAR) + "-"
						+ (cal.get(Calendar.MONTH) + 1) + "-"
						+ cal.get(Calendar.DATE);
			}
			if (search != null && !search.equals("")) {
				List<Event> eventsAfterEvening = eventDao
						.findEventsAfterEvening(userId, location, search,
								searchDate);
				DiscoverSource discoversource = new DiscoverSource();
				List<DiscoverList> discoverEvent = findForEvent(eventsAfterEvening);
				discoversource.setEventSource1(discoverEvent);
				discoversource.setSearchBy("date");
				return discoversource;
			} else {
				List<Event> eventsAfterEvening = eventDao
						.findEventsAfterEvening(userId, location, searchDate);
				DiscoverSource discoversource = new DiscoverSource();
				List<DiscoverList> discoverEvent = findForEvent(eventsAfterEvening);
				discoversource.setEventSource1(discoverEvent);
				discoversource.setSearchBy("date");
				return discoversource;
			}
		} else if (searchDate != null && !searchDate.equals("")) {
			if (search != null && !search.equals("")) {
				List<Event> eventsBySearchDate = eventDao
						.findEventsBySearchDate(search, searchDate, location,
								userId);
				DiscoverSource discoverSource = new DiscoverSource();
				List<DiscoverList> discoverEvent = findForEvent(eventsBySearchDate);
				discoverSource.setEventSource1(discoverEvent);
				discoverSource.setSearchBy("date");
				return discoverSource;
			} else {
				List<Event> eventsBySearchDate = eventDao
						.findEventsBySearchDate(searchDate, location, userId);
				DiscoverSource discoverSource = new DiscoverSource();
				List<DiscoverList> discoverEvent = findForEvent(eventsBySearchDate);
				discoverSource.setEventSource1(discoverEvent);
				discoverSource.setSearchBy("date");
				return discoverSource;
			}
		} else {
			/*
			 * This block (From line 555 to 569) returns List of user Events,
			 * when search is performed using UserName
			 */
			List<UUID> userIds = userDao.findByName(search);
			DiscoverSource discoverSourceName = new DiscoverSource();
			List<UUID> eventIds = new ArrayList<UUID>();
			List<Event> userEvents = new ArrayList<Event>();
			if (userIds != null && userIds.size() > 0) {
				userIds = userContactDao
						.findEventSharedUserIds(userId, userIds);
				eventIds = userInterestedEventDao.findEventsByUserIds(userIds);
				if (eventIds != null && eventIds.size() > 0)
					userEvents = eventDao.findEventsForSearch(eventIds);
				if (userIds != null && userIds.size() > 0) {
					List<DiscoverList> discoverEvent = findForEvent(userEvents);
					discoverSourceName.setEventSource1(discoverEvent);
					discoverSourceName.setSearchBy("people");
				}
				return discoverSourceName;
			} else {
				if (userId != null && search != null)
					userFrequentSearchDao
							.saveOrUpdateSearchTerm(userId, search);
				User user = userDao.findById(userId);
				List<UserContact> starredContacts = userContactDao
						.findMyCrowd(user.getUserID());
				List<UUID> starredContactIds = new ArrayList<UUID>();
				for (UserContact e : starredContacts) {
					starredContactIds.add(e.getPk().getContactUserID());
				}
				NetworkRequest request = commonService
						.locationDetails(location);
				/*
				 * This block (From line 588 to 591) returns List of idea from
				 * starred contacts with matching the search term
				 */
				Map<Idea, Double> ideaSource = ideaDao.getIdeaFromSource1(
						starredContactIds, search);
				List<DiscoverList> discoverIdea = findForIdea(ideaSource);
				List<DiscoverList> discoverIdeaSource1 = sortingDiscoverList(discoverIdea);
				/*
				 * This block (From line 597 to 607) return List of idea, which
				 * are matched to User Domain with the Search domain from the
				 * table idea_domain_stats
				 */
				ideaSource = ideaDao
						.getIdeaFromSource2(
								user.getVerifiedDomainEmail() != null
										&& !user.getVerifiedDomainEmail()
												.isEmpty() ? user
										.getVerifiedDomainEmail().substring(
												user.getVerifiedDomainEmail()
														.indexOf('@') + 1) : "",
								search);
				discoverIdea = findForIdea(ideaSource);
				List<DiscoverList> discoverIdeaSource2 = sortingDiscoverList(discoverIdea);
				if (request != null && request.getAdmin1() != null
						&& !request.getAdmin1().isEmpty()
						&& request.getAdmin1().length() > 1
						&& request.getAdmin2() != null
						&& !request.getAdmin2().isEmpty()
						&& request.getAdmin2().length() > 1
						&& request.getAdmin3() != null
						&& !request.getAdmin3().isEmpty()
						&& request.getAdmin3().length() > 1)
					/*
					 * This block (From line 621 to 627) returns List of idea
					 * when search is performed by using some Location...
					 */
					ideaSource = ideaDao.getIdeaFromSource3(
							request.getAdmin1(), request.getAdmin2(),
							request.getAdmin3(), request.getCountry(), search);
				else
					ideaSource = new HashMap<Idea, Double>();
				discoverIdea = findForIdea(ideaSource);
				List<DiscoverList> discoverIdeaSource3 = sortingDiscoverList(discoverIdea);
				/*
				 * This block (From line 632 to 635) return List of idea that
				 * are available in the Country that searched by user
				 */
				ideaSource = ideaDao.getIdeaFromSource4(request.getCountry(),
						search);
				discoverIdea = findForIdea(ideaSource);
				List<DiscoverList> discoverIdeaSource4 = sortingDiscoverList(discoverIdea);

				ideaSource = ideaDao.findBySearch(search);
				discoverIdea = findForIdea(ideaSource);
				List<DiscoverList> discoverIdeaSource5 = sortingDiscoverList(discoverIdea);

				DiscoverSource discoverSource = new DiscoverSource();
				discoverSource.setIdeaSource1(discoverIdeaSource1);
				discoverSource.setIdeaSource2(discoverIdeaSource2);
				discoverSource.setIdeaSource3(discoverIdeaSource3);
				discoverSource.setIdeaSource4(discoverIdeaSource4);
				discoverSource.setIdeaSource5(discoverIdeaSource5);
				/*
				 * This block (From line 678 to 680) returns List of events from
				 * starred events matches with search key
				 */
				List<Event> eventSource = eventDao.findStarredEvents(
						user.getUserID(), search);
				List<DiscoverList> discoverEvent1 = findForEvent(eventSource);
				/*
				 * This block (From line 685 to 687) returns List of events
				 * visible to users matches with search key
				 */
				eventSource = new ArrayList<Event>(eventDao.findDiscoverEvents(
						user.getUserID(), search));
				List<DiscoverList> discoverEvent2 = findForEvent(eventSource);
				/*
				 * This block (From line 692 to 701) returns List of events from
				 * user's verified domain
				 */
				eventSource = eventDao
						.findDomainEvents(
								user.getVerifiedDomainEmail() != null
										&& !user.getVerifiedDomainEmail()
												.isEmpty() ? user
										.getVerifiedDomainEmail().substring(
												user.getVerifiedDomainEmail()
														.indexOf('@') + 1)
										: null, userId);
				List<DiscoverList> discoverEvent3 = findForEvent(eventSource);
				/*
				 * This block (From line 706 to 709) returns List of public
				 * events matches with search key
				 */
				eventSource = new ArrayList<Event>(
						eventDao.findDiscoverPublicEvents(user.getUserID(),
								location, search));
				List<DiscoverList> discoverEvent4 = findForEvent(eventSource);
				/*
				 * This block (From line 714 to 739) will limit the resulting
				 * idea by 10
				 */
				if (discoverEvent1.size() > 8 && discoverEvent2.size() > 8) {
					discoverSource
							.setEventSource1(discoverEvent1.size() > 8 ? discoverEvent1
									.subList(0, 8) : discoverEvent1);
					discoverSource
							.setEventSource2(discoverEvent2.size() > 8 ? discoverEvent2
									.subList(0, 8) : discoverEvent2);
				} else if (discoverEvent1.size() < 8
						&& discoverEvent2.size() < 8) {
					discoverSource.setEventSource1(discoverEvent1);
					discoverSource.setEventSource2(discoverEvent2);
				} else if (discoverEvent1.size() > 8
						&& discoverEvent2.size() < 8) {
					int temp = 8 - discoverEvent2.size();
					discoverSource
							.setEventSource1(discoverEvent1.size() > temp ? discoverEvent1
									.subList(0, temp) : discoverEvent1);
					discoverSource.setEventSource2(discoverEvent2);
				} else if (discoverEvent1.size() < 8
						&& discoverEvent2.size() > 8) {
					int temp = 8 - discoverEvent1.size();
					discoverSource
							.setEventSource2(discoverEvent2.size() > temp ? discoverEvent2
									.subList(0, temp) : discoverEvent2);
					discoverSource.setEventSource1(discoverEvent1);
				}
				discoverSource
						.setEventSource3(discoverEvent3.size() > 8 ? discoverEvent3
								.subList(0, 8) : discoverEvent3);
				discoverSource
						.setEventSource4(discoverEvent4.size() > 8 ? discoverEvent4
								.subList(0, 8) : discoverEvent4);
				discoverSource.setSearchBy("keywords");
				return discoverSource;
			}
		}
	}

	protected List<DiscoverList> filterDiscoverList(
			List<DiscoverList> discoverEvent, List<DiscoverList> discoverIdea) {
		List<DiscoverList> filterInboxs = new ArrayList<DiscoverList>();
		int ideaCount = 0, eventCount = 0;
		for (int i = 0; i < discoverEvent.size() + discoverIdea.size(); i++) {
			if (i % 2 == 0 && eventCount < discoverEvent.size()) {
				filterInboxs.add(discoverEvent.get(eventCount));
				eventCount++;
			} else {
				DiscoverList temp = discoverIdea.get(ideaCount);
				if (ideaCount != 0 && ideaCount % 3 == 0) {
					temp.setIsBreak(true);
					filterInboxs.add(temp);
				} else
					filterInboxs.add(temp);
				ideaCount++;
			}
		}
		return filterInboxs;
	}

	@Override
	public Status publicKeyword(String location) {
		Status status = new Status();
		Set<String> keywords = new HashSet<String>();
		List<Event> events = eventDao.findByPublicSearch(location);
		if (events != null) {
			for (Event e : events) {
				keywords.add(e.getTitle());
			}
		}
		// List<Idea> ideas = ideaDao.findAll(20);
		List<Idea> ideas = ideaDao.findRandomIdeas(20);
		if (ideas != null) {
			for (Idea i : ideas) {
				keywords.add(i.getTitle());
				keywords.add(i.getPrimaryCategory());
				keywords.add(i.getSecondaryCategory());
				keywords.add(i.getTagsEnvironment());
				keywords.add(i.getTagsExperience());
				keywords.add(i.getTagsPeople());
			}
		}
		NetworkRequest locationDetails = commonService
				.locationDetails(location);
		String country = locationDetails.getCountry();
		List<RegionPopularSearch> popularSearchs = new ArrayList<RegionPopularSearch>();
		if (country != null)
			popularSearchs = regionPopularSearchDao
					.findAllPopularSearch(locationDetails.getCountry());
		List<String> allPopularSearches = new ArrayList<String>();
		for (RegionPopularSearch r : popularSearchs) {
			String popularSearch = r.getPk().getPopularSearchTerm();
			allPopularSearches.add(popularSearch);
		}
		status.setPopularSearch(allPopularSearches);
		status.setKeywords(new ArrayList<String>(keywords));
		return filterKeyworks(status);
	}

	@Override
	public Status privateKeyword(UUID userId, String location) {
		Status status = new Status();
		Set<String> keywords = new HashSet<String>();
		Set<Event> events = eventDao.findBySearchEvents(userId, location);
		Set<Event> publicEvents = eventDao.findBySearchPublicEvents(userId,
				location);
		events.addAll(publicEvents);
		System.out.println("the keywords 1 are : " + keywords);
		if (events != null) {
			for (Event e : events) {
				keywords.add(e.getTitle());
			}
		}
		System.out.println("the keywords are : " + keywords);
		List<UserContact> contacts = networkService.contacts(userId, "all");
		if (contacts != null) {
			for (UserContact u : contacts) {
				keywords.add(u.getContactFullName());
			}
		}
		List<Idea> ideas = ideaDao.findAllIdeas();
		if (ideas != null) {
			for (Idea i : ideas) {
				keywords.add(i.getTitle());
				keywords.add(i.getPrimaryCategory());
				keywords.add(i.getSecondaryCategory());
				keywords.add(i.getTagsEnvironment());
				keywords.add(i.getTagsExperience());
				keywords.add(i.getTagsPeople());
			}
		}
		List<UserFrequentSearch> userFrequentSearchs = userFrequentSearchDao
				.findSearchTermByUserId(userId);
		List<String> searchTerms = new ArrayList<String>();
		for (UserFrequentSearch u : userFrequentSearchs) {
			String searchTerm = u.getPk().getSearchTerm();
			searchTerms.add(searchTerm);
		}
		NetworkRequest locationDetails = commonService
				.locationDetails(location);
		List<RegionPopularSearch> popularSearchs = regionPopularSearchDao
				.findAllPopularSearch(locationDetails.getCountry());
		List<String> allPopularSearches = new ArrayList<String>();
		for (RegionPopularSearch r : popularSearchs) {
			String popularSearch = r.getPk().getPopularSearchTerm();
			allPopularSearches.add(popularSearch);
		}
		status.setFrequentSearch(searchTerms);
		status.setPopularSearch(allPopularSearches);
		status.setKeywords(new ArrayList<String>(keywords));
		return filterKeyworks(status);
	}

	protected Status filterKeyworks(Status status) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Set<String> keywords = new HashSet<String>();
		if (status.getKeywords() != null && !status.getKeywords().isEmpty()) {
			for (String s : status.getKeywords()) {
				if (s == null)
					continue;
				if (s.contains(" ")) {
					String[] temp = s.split(" ");
					for (String str : temp) {
						Matcher m = p.matcher(str);
						if (str.length() > 3 && !m.find())
							keywords.add(str);
					}
				} else {
					Matcher m = p.matcher(s);
					if (s.length() > 3 && !m.find())
						keywords.add(s);
				}

			}
		}
		status.setKeywords(new ArrayList<String>(keywords));
		return status;
	}

	protected List<DiscoverList> findForEvent(List<Event> events) {
		List<DiscoverList> filterInboxs = new ArrayList<DiscoverList>();
		if (events != null && events.size() > 0) {
			Set<UUID> userIds = new HashSet<UUID>();
			if (events != null) {
				for (Event e : events) {
					if (e.getInterestedUserIDs() == null)
						continue;
					userIds.addAll(e.getInterestedUserIDs());
				}
			}
			List<User> users = userDao.findUserByIds(new ArrayList<UUID>(
					userIds));
			Map<UUID, String> userName = new HashMap<UUID, String>();
			Map<UUID, String> userPhoto = new HashMap<UUID, String>();
			if (users != null) {
				for (User u : users) {
					userName.put(u.getUserID(), u.getFullName());
					userPhoto.put(u.getUserID(), u.getPhotoPath());
				}
			}
			for (Event e : events) {
				if (!e.getIsDeleted()) {
					DiscoverList in = new DiscoverList();
					in.setMomentum(e.getMomentum());
					UserDiscoverInbox inbox = new UserDiscoverInbox();
					UserDiscoverInboxKey key = new UserDiscoverInboxKey();
					key.setEventID(e.getEventID());
					key.setIsEvent(true);
					inbox.setPk(key);
					inbox.setTitle(e.getTitle());
					inbox.setSmallPhotoPath(e.getSmallPhotoPath());
					inbox.setLargePhotoPath(e.getLargePhotoPath());
					List<String> names = new ArrayList<String>();
					List<String> photos = new ArrayList<String>();
					if (e.getInterestedUserIDs() != null) {
						for (UUID u : e.getInterestedUserIDs()) {
							String name = userName.get(u);
							String photo = userPhoto.get(u);
							names.add(name);
							if (photos.size() <= 3)
								photos.add(photo);
						}
					}
					inbox.setPeoplePhotos(photos);
					in.setIsChat(e.getIsChat());
					in.setIsConfirmed(e.getHasOrganiserConfirmed() == null ? false
							: e.getHasOrganiserConfirmed());
					in.setEventExpiry(e.getEventExpiry());
					in.setEventStartTime(e.getEventStartTime());
					in.setEventDate(e.getEventDate());
					if (!in.getIsChat()) {
						if (e.getInterestedUserIDs() != null) {
							int chat = (e.getMinPlaces().intValue() < 4) ? 1
									: ((int) Math
											.ceil(0.5 * (e.getMinPlaces() + 1)) - e
											.getInterestedUserIDs().size());
							inbox.setMessage(e.getInterestedUserIDs().size()
									+ " PEOPLE INTERESTED  +" + chat
									+ " FOR CHAT");
						}
					}
					if (in.getIsConfirmed()) {
						inbox.setMessage(e.getTotalAttending() + " OF "
								+ e.getMaxPlaces() + " PLACES FILLED");
					}
					in.setInbox(inbox);
					in.setNames(names);
					in.setIsBreak(false);
					filterInboxs.add(in);
				}
			}
		}
		return filterInboxs;
	}

	protected List<DiscoverList> findForIdea(List<Idea> ideas) {
		List<DiscoverList> filterInboxs = new ArrayList<DiscoverList>();
		if (ideas != null && ideas.size() > 0) {
			for (Idea i : ideas) {
				DiscoverList in = new DiscoverList();
				UserDiscoverInbox inbox = new UserDiscoverInbox();
				UserDiscoverInboxKey key = new UserDiscoverInboxKey();
				key.setEventID(i.getIdeaID());
				key.setIsEvent(false);
				inbox.setPk(key);
				inbox.setTitle(i.getTitle());
				inbox.setSmallPhotoPath("https://s3-eu-west-1.amazonaws.com"
						+ i.getSmallPhotoPath().substring(4));
				inbox.setLargePhotoPath("https://s3-eu-west-1.amazonaws.com"
						+ i.getLargePhotoPath().substring(4));
				in.setInbox(inbox);
				in.setMomentum(1.0);
				in.setDescription(i.getDescription());
				filterInboxs.add(in);
			}
		}
		return filterInboxs;
	}

	protected List<DiscoverList> findForIdea(Map<Idea, Double> discoverMonentum) {
		if (discoverMonentum.isEmpty())
			return new ArrayList<DiscoverList>();
		List<DiscoverList> filterInboxs = new ArrayList<DiscoverList>();
		if (discoverMonentum != null && discoverMonentum.size() > 0) {
			for (Map.Entry<Idea, Double> i : discoverMonentum.entrySet()) {
				DiscoverList in = new DiscoverList();
				UserDiscoverInbox inbox = new UserDiscoverInbox();
				UserDiscoverInboxKey key = new UserDiscoverInboxKey();
				key.setEventID(i.getKey().getIdeaID());
				key.setIsEvent(false);
				inbox.setPk(key);
				inbox.setTitle(i.getKey().getTitle());
				inbox.setSmallPhotoPath("https://s3-eu-west-1.amazonaws.com"
						+ i.getKey().getSmallPhotoPath().substring(4));
				inbox.setLargePhotoPath("https://s3-eu-west-1.amazonaws.com"
						+ i.getKey().getLargePhotoPath().substring(4));
				System.out.println("The web link is : "
						+ i.getKey().getWebLink());
				inbox.setWebLink(i.getKey().getWebLink());
				in.setInbox(inbox);
				in.setMomentum(i.getValue());
				in.setDescription(i.getKey().getDescription());
				filterInboxs.add(in);
			}
		}
		return filterInboxs;
	}

	@Override
	public EventViewResponse viewEventPublic(UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		if (event.getIsPublic()) {
			User organizer = new User();
			List<UUID> userIds = new ArrayList<UUID>();
			userIds.addAll(event.getInterestedUserIDs());
			if (event.getOrganizerUserID() != null) {
				organizer = userDao.findById(event.getOrganizerUserID());
				userIds.remove(event.getOrganizerUserID());
			}
			List<User> users = userDao.findUserByIds(userIds);
			Integer count = 0;
			if (!event.getIsChat())
				count = (event.getMinPlaces().intValue() < 4) ? 1 : ((int) Math
						.ceil(0.5 * (event.getMinPlaces() + 1)) - event
						.getInterestedUserIDs().size());
			return new EventViewResponse(event, organizer, users, count);
		} else
			throw new AppException(new AppErrorMessage("400",
					"Please sign in to see this event"));
	}

	@Override
	public EventViewResponse viewEvent(UUID eventId) {
		Event event = eventDao.selectEvent(eventId);
		if (event.getIsDeleted()) {
			throw new AppException(new AppErrorMessage("400",
					"Event is no more available"));
		}
		User organizer = new User();
		List<UUID> userIds = new ArrayList<UUID>();
		userIds.addAll(event.getInterestedUserIDs());
		if (event.getOrganizerUserID() != null) {
			organizer = userDao.findById(event.getOrganizerUserID());
			userIds.remove(event.getOrganizerUserID());
		}
		List<User> users = userDao.findUserByIds(userIds);
		List<User> visibleToUserIds = userDao.findUserByIds(event
				.getVisibleToUserIDs());
		Integer count = 0;
		if (!event.getIsChat())
			count = (event.getMinPlaces().intValue() < 4) ? 1 : ((int) Math
					.ceil(0.5 * (event.getMinPlaces() + 1)) - event
					.getInterestedUserIDs().size());
		return new EventViewResponse(event, organizer, users, count,
				visibleToUserIds);
	}

	@Override
	public Status friendsCount(UUID ideaId, UUID userId) {
		Status status = new Status(0L);
		status.setFacebookCount(userContactDao.findFacebookCount(userId));
		status.setFacebookStarredCount(userContactDao
				.findFacebookStarredCount(userId));
		List<UserContact> userContacts = userContactDao
				.findContactsByGatherFrom(userId, Arrays.asList("my_net"));
		if (userContacts != null) {
			Long count = 0L;
			if (userContacts.size() > 0) {
				for (UserContact u : userContacts) {
					if (u.getIsStarred() != null && u.getIsStarred() == true)
						count++;
				}
				status.setNetworkStarredCount(count);
			}
			status.setNetworkCount((long) userContacts.size());
		}
		userContacts = networkService.contacts(userId, "whosup");
		if (userContacts != null) {
			Long count = 0L;
			if (userContacts.size() > 0) {
				for (UserContact u : userContacts) {
					if (u.getIsStarred())
						count++;
				}
				status.setWhosupStarredCount(count);
			}
			status.setWhosupCount((long) userContacts.size());
		}
		status.setIdea(ideaDao.findById(ideaId));
		return status;
	}

	@Override
	public Status buzzEvent(UUID eventId, UUID userId) {
		Event event = eventDao.selectEvent(eventId);
		if (event.getIsDeleted() == true)
			throw new AppException(new AppErrorMessage("400",
					"Sorry, Event has been removed..!"));
		if (event.getGatherAdvancedFemaleOnly()
				&& userDao.findById(userId).getGender()
						.equalsIgnoreCase("male"))
			throw new AppException(new AppErrorMessage("400",
					"You are not eligible for this event"));
		if (!event.getIsPublic()
				&& !event.getVisibleToUserIDs().contains(userId))
			throw new AppException(new AppErrorMessage("400",
					"You are not eligible for this event"));
		if (event.getEventExpiry() != null
				&& event.getEventExpiry().before(new Date()))
			throw new AppException(new AppErrorMessage("400",
					"Sorry, Event has been Expired"));
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(event.getEventExpiry());
		cal.add(Calendar.DATE, 1);
		event.setEventExpiry(cal.getTime());
		List<UUID> interestedUserID = new ArrayList<UUID>();
		if (event.getInterestedUserIDs() != null)
			interestedUserID.addAll(event.getInterestedUserIDs());
		if (!interestedUserID.contains(userId)) {
			interestedUserID.add(userId);
			event.setInterestedUserIDs(interestedUserID);
			userInterestedEventDao.save(new UserInterestedEvent(
					new UserInterestedEventKey(userId, eventId)));
			System.out.println("Check for min users "
					+ (int) Math.ceil(0.5 * (event.getMinPlaces() + 1)));
			if (event.getInterestedUserIDs().size() >= (int) Math
					.ceil(0.5 * (event.getMinPlaces() + 1))) {
				event.setIsChat(true);
				List<UserEventInbox> eventsInbox = userEventInboxDao
						.findUsersByEventId(eventId);
				for (UserEventInbox eventInbox : eventsInbox) {
					eventInbox.setIsChat(true);
				}
				userEventInboxDao.update(eventsInbox);
			}
			List<UserEventInbox> eventsInbox = userEventInboxDao
					.findUsersByEventId(eventId);
			for (UserEventInbox eventInbox : eventsInbox) {
				eventInbox.setPeopleGathered(event.getInterestedUserIDs()
						.size());
				eventInbox.setPeopleToGather(event.getMaxPlaces()
						- event.getInterestedUserIDs().size());
			}
			userEventInboxDao.update(eventsInbox);
			UserEventInbox eventInbox = new UserEventInbox(
					new UserEventInboxKey(userId, eventId), event.getIsPublic());
			eventInbox.setEventTitle(event.getTitle());
			if (event.getIsChat())
				eventInbox.setIsChat(true);
			if (event.getHasOrganiserConfirmed() == null ? false : event
					.getHasOrganiserConfirmed())
				eventInbox.setIsEventConfirmed(true);
			eventInbox.setPeopleGathered(event.getInterestedUserIDs().size());
			eventInbox.setPeopleToGather(event.getMaxPlaces()
					- event.getInterestedUserIDs().size());
			eventInbox.setPlacesFilled(event.getTotalAttending());
			eventInbox.setPlacesToFill(event.getMaxPlaces()
					- event.getTotalAttending());
			eventInbox.setEventDate(event.getEventDate());
			eventInbox.setChatId(UUID.fromString(event.getChatId()));
			userEventInboxDao.save(eventInbox);
			MomentumProperties momentum = new MomentumProperties(
					(event.getInterestedUserIDs().size() / event.getMaxPlaces() + 0.3),
					event.getMomentum(), event.getCompoundEffect(),
					MomentumProperties.dateDiff(event.getGathered1stDate(),
							event.getMomentumLastUpdated()));
			momentum = momentum.updateMomentum(MomentumProperties.dateDiff(
					event.getGathered1stDate(), new Date()));
			event = momentum.updateEvent(event);
			eventDao.update(event);
			User user = userDao.findById(userId);
			String companyDomain = !user.getVerifiedDomainEmail().isEmpty() ? user
					.getVerifiedDomainEmail().substring(
							user.getVerifiedDomainEmail().indexOf('@') + 1)
					: null;
			if (userId != null && event.getIdeaID() != null)
				ideaDao.updateStarredIdeaMomentum(userId, event.getIdeaID());
			if (event.getIdeaID() != null && companyDomain != null) {
				Row one = ideaDao.selectIdeaDomainMomentum(event.getIdeaID(),
						companyDomain);
				if (one != null && one.getUUID("idea_id") != null
						&& one.getString("company_domain") != null)
					ideaDao.ideaDomainUpdate(event.getIdeaID(), companyDomain,
							one);
			}
			// String location = event.getGatherAdvancedLocAddr();
			// NetworkRequest locationDetails = commonService
			// .locationDetails(location);

			// // commonService.kafkaProducerBuzz(userId, eventId, false);
			if (event.getChatId() != null)
				commonService.addUser(UUID.fromString(event.getChatId()),
						userId);
			return new Status("Success");
		}
		return new Status("Fail");
	}

	private String photoResize(String smallPhotoPath) {
		try {
			URL url = new URL(smallPhotoPath);
			BufferedImage img = ImageIO.read(url);
			if (img.getHeight() >= 300 && img.getWidth() >= 300) {
				BufferedImage scaledImage = Scalr.resize(img,
						Scalr.Mode.AUTOMATIC, 300, 300);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(scaledImage, "jpg", os);
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				smallPhotoPath = commonService.uploadSmallImage(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return smallPhotoPath;
	}

	@Override
	public DiscoverSource eventList(UUID userId, String location,
			Long timeInMills) {
		List<Event> eventBetweenTimeInterval = eventDao
				.findEventsBetWeenTimeInterval(userId, location, timeInMills);
		DiscoverSource discoversource = new DiscoverSource();
		List<DiscoverList> discoverEvent = findForEvent(eventBetweenTimeInterval);
		discoversource.setEventSource1(discoverEvent);
		// List<Event> eventsDeletedBetweenTimeInterval = eventDao
		// .findDeletedEventsBetWeenTimeInterval(userId, location, timeInMills);
		// discoversource
		// .setDeletedEvents(findForEvent(eventsDeletedBetweenTimeInterval));
		discoversource.setSearchBy("timeInterval");
		return discoversource;
	}

	@Override
	public DiscoverSource eventList(String location, Long timeInMills) {
		List<Event> eventBetweenTimeInterval = eventDao
				.findEventsBetWeenTimeInterval(location, timeInMills);
		DiscoverSource discoversource = new DiscoverSource();
		List<DiscoverList> discoverEvent = findForEvent(eventBetweenTimeInterval);
		discoversource.setEventSource1(discoverEvent);
		// List<Event> eventsDeletedBetweenTimeInterval = eventDao
		// .findDeletedEventsBetWeenTimeInterval(location, timeInMills);
		// discoversource
		// .setDeletedEvents(findForEvent(eventsDeletedBetweenTimeInterval));
		discoversource.setSearchBy("timeInterval");
		return discoversource;
	}

}
