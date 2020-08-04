package com.whosup.resource;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.EventDao;
import com.whosup.dao.IdeaDao;
import com.whosup.dao.UserAccessTokenDao;
import com.whosup.dao.UserDao;
import com.whosup.model.Event;
import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.request.DiscoverRequest;
import com.whosup.model.response.DiscoverSource;
import com.whosup.model.response.EventViewResponse;
import com.whosup.model.response.Status;
import com.whosup.service.CommonService;
import com.whosup.service.DiscoverService;


@Path("/discover")
@Component
public class DiscoverResource {

	@Autowired
	private UserDao userDao;
	@Autowired
	private DiscoverService discoverService;
	@Autowired
	private UserAccessTokenDao userAccessTokenDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private IdeaDao ideaDao;

	/***
	 * This is App landing page API, it provides list of ideas and events
	 * 
	 * @param accessToken
	 *            - Whosup access token
	 * @return list of ideas and events
	 */
	@GET
	@Produces("application/json")
	@Path("/list")
	public DiscoverSource eventList(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "location") String location) {
		
		if (location == null || !location.contains(",")) {
			throw new AppException(new AppErrorMessage("400",
					"Please provide location"));
		}
		if (accessToken != null)
			return discoverService.eventList(validateAccessToken(accessToken),
					location);
		else
			return discoverService.eventList(location);
	}

	@GET
	@Produces("application/json")
	@Path("/reload")
	public DiscoverSource discoverScroll(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "location") String location,
			@QueryParam(value = "time_in_mills") String timeInMills) {
		long refreshTime = Long.parseLong(timeInMills);
		if (location == null || !location.contains(","))
			throw new AppException(new AppErrorMessage("400",
					"Please provide location"));
		if (accessToken != null) {
			User user = validateAccessToken(accessToken);
			return discoverService.eventList(user.getUserID(), location,
					refreshTime);
		} else {
			return discoverService.eventList(location, refreshTime);
		}
	}

	@GET
	@Produces("application/json")
	@Path("/keywords")
	public Status discoverReload(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "location") String location) {

		if (location == null || !location.contains(","))
			return new Status();
		if (accessToken != null) {
			User user = validateAccessToken(accessToken);
			return discoverService.privateKeyword(user.getUserID(), location);
		} else {
			return discoverService.publicKeyword(location);
		}
	}

	/***
	 * This API filters ideas and events based on given search items or it will
	 * provide list of ideas and events under particular category
	 * 
	 * Searches can be performed by Date, Day, friday, today soccer, 12/12/2015
	 * etc..
	 * 
	 * @param accessToken
	 *            - whosup access token
	 * @param search
	 *            - search term
	 * @param categoryID
	 *            - UUID of category
	 * @return list of events and ideas
	 */
	@GET
	@Produces("application/json")
	@Path("/search")
	public DiscoverSource search(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "location") String location,
			@QueryParam(value = "search") String search,
			@QueryParam(value = "searchDate") String searchDate,
			@QueryParam(value = "searchAfterEve") String searchAfterEve) {
		if (location == null || !location.contains(","))
			throw new AppException(new AppErrorMessage("400",
					"Please provide location"));
		if (accessToken != null) {
			User user = validateAccessToken(accessToken);
			return discoverService.searchDiscover(user.getUserID(), location,
					search, searchDate, searchAfterEve);
		} else {
			return discoverService.searchDiscover(location, search, searchDate,
					searchAfterEve);
		}

	}

	/***
	 * This API adds a user into an event after user buzzed a event
	 * 
	 * @param request
	 *            - access token and eventId are required
	 * @return success or failure message
	 */
	@GET
	@Produces("application/json")
	@Path("/details")
	public Status count(@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "idea_id") UUID ideaId) {
		User user = validateAccessToken(accessToken);
		return discoverService.friendsCount(ideaId, user.getUserID());
	}

	/***
	 * This API provides list of users under advance for choosing user who can
	 * see event after creation. list of users will be send based items selected
	 * under gatherfrom button while creating event
	 * 
	 * @param request
	 *            access_token and gatherfrom list which may contain(fb, ln,
	 *            whosup, my_net)
	 * @return list of users
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/event/advance")
	public List<UserContact> gatherFrom(DiscoverRequest request) {
		return discoverService.gatherFrom(request,
				validateAccessToken(request.getAccessToken()));
	}

	/***
	 * This API used for creating a event from idea and creating own events
	 * 
	 * @param request
	 *            - access_token and fields given while creating a event
	 * @return success or failure message
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/event/create")
	public Status createEvent(DiscoverRequest request) {
		return discoverService.createEvent(request,
				validateAccessToken(request.getAccessToken()));
	}

	/***
	 * This API returns Event settings and details
	 * 
	 * @param accessToken
	 *            - whosup access token
	 * @param eventId
	 *            - event's uuid for which settings and details are needed
	 * @return response contains event details
	 */
	@GET
	@Produces("application/json")
	@Path("/event/view")
	public EventViewResponse view(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "event_id") UUID eventId) {
		if (accessToken != null) {
			validateAccessToken(accessToken);
			return discoverService.viewEvent(eventId);
		} else
			return discoverService.viewEventPublic(eventId);
	}

	/***
	 * This API adds a user into an event after user buzzed a event
	 * 
	 * @param request
	 *            - access token and eventId are required
	 * @return success or failure message
	 */
	@POST
	@Produces("application/json")
	@Path("/event/buzz")
	public Status buzz(DiscoverRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return discoverService
				.buzzEvent(request.getEventId(), user.getUserID());
	}

	/***
	 * This API gives list of users other than users send under "/event/advance"
	 * to select few users from other categories
	 * 
	 * @param request
	 *            - access_token and other gatherfrom list which may contain(fb,
	 *            ln, whosup, my_net)
	 * @return list of users
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/event/add")
	public List<UserContact> add(DiscoverRequest request) {
		return discoverService.gatherFrom(request,
				validateAccessToken(request.getAccessToken()));
	}

	@GET
	@Produces("application/json")
	@Path("/event/notification")
	public Status pushNotification(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "event_id") UUID eventId) {
		// ideaDao.findRandomIdeas(10);
		Event event = eventDao.selectEvent(eventId);
		try {
//			JSONObject json = new JSONObject();
//			for(UUID userID:event.getInterestedUserIDs()){
//				json.put(userDao.findById(userID).getFullName(), userID);
//			}
			User user = userDao.findById(userAccessTokenDao
					.findUserIdByAccessToken(accessToken));
			JSONObject userJson=new JSONObject(user);
			JSONObject json1 = new JSONObject();
			json1.put("User Details ",userJson.toString());
			JSONObject eventJson=new JSONObject(event);
			json1.put("Event Details",eventJson.toString());
			System.out.println(json1);
			JSONObject json2 = new JSONObject();
			json2.put("type", "EVENT_CONFIRMED");
			json2.put("data", json1);
			commonService.notify(validateAccessToken(accessToken), json2.toString().replace("\\\"", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Status("200");

	}
	
	private User validateAccessToken(UUID accessToken) {
		User user = userDao.findById(userAccessTokenDao
				.findUserIdByAccessToken(accessToken));
		if (user != null) {
			if (user.getAccessTokenExpireDate().before(new Date()))
				throw new AppException(new AppErrorMessage("400",
						"Your Access Token has been Expired"));
			return user;
		} else
			throw new AppException(new AppErrorMessage("101",
					"Given Token is invalid"));
	}
}
