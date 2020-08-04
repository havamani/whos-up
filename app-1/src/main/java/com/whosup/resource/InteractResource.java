package com.whosup.resource;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.UserAccessTokenDao;
import com.whosup.dao.UserDao;
import com.whosup.model.User;
import com.whosup.model.request.InteractRequest;
import com.whosup.model.response.ConfirmRequest;
import com.whosup.model.response.EventSettingResponse;
import com.whosup.model.response.Status;
import com.whosup.model.response.UserEvent;
import com.whosup.service.CommonService;
import com.whosup.service.InteractService;

@Path("/interact")
@Component
public class InteractResource {

	@Autowired
	private InteractService interactService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAccessTokenDao userAccessTokenDao;
	@Autowired
	private CommonService commonService;
	
	/***
	 * This API is used to view the event.
	 * 
	 * @param accessToken
	 *            - Whosup access token
	 * @return list of events
	 */
	@GET
	@Produces("application/json")
	@Path("/view")
	public UserEvent view(@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return interactService.interactView(user.getUserID());
	}
	
	/***
	 * This API is used to delete the event.
	 * 
	 * @param request
	 *            - Whosup access token,eventID
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/delete/event")
	public Status deleteEvent(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.deleteEvent(user.getUserID(),
				request.getEventId());
	}
	
	/***
	 * This API is used to reject the organizer.
	 * 
	 * @param request
	 *            - Whosup access token,eventID
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/organizer/reject")
	public Status rejected(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.reject(user.getUserID(), request.getEventId());
	}
	
	/***
	 * This API is used to accept the organizer.
	 * 
	 * @param request
	 *            - Whosup access token,eventID
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/organizer/accept")
	public Status confirmed(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.acceptOrganizer(user.getUserID(),
				request.getEventId());
	}
	
	/***
	 * This API is used to resign the organizer.
	 * 
	 * @param request
	 *            - Whosup access token,eventID
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/organizer/resign")
	public Status notConfirmed(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.resignOrganizer(user.getUserID(),
				request.getEventId());
	}
	
	/***
	 * This API is used to conform the event request.
	 * 
	 * @param 
	 *       - Whosup access token,eventID
	 *       
	 * @return ConfirmRequest
	 */
	@GET
	@Produces("application/json")
	@Path("/event/confirm/request")
	public ConfirmRequest confirmRequest(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "event_id") UUID eventId) {
		User user = validateAccessToken(accessToken);
		return interactService.confirmRequest(user.getUserID(), eventId);
	}
	
	/***
	 * This API is used to conform event.
	 * 
	 * @param 
	 *       - Whosup access token,eventID.
	 *       
	 * @return Status
	 */

	@POST
	@Produces("application/json")
	@Path("/event/confirm")
	public Status confirmEvent(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.confirmEvent(user, request.getEventId());
	}
	
	/***
	 * This API is used to unConform event.
	 * 
	 * @param 
	 *       - Whosup access token,eventID.
	 *       
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/event/unconfirm")
	public Status unConfirmEvent(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.unConfirmEvent(user.getUserID(),
				request.getEventId());
	}
	
	/***
	 * This API is used to see the event settings.
	 * 
	 * @param 
	 *       - Whosup access token,eventID
	 *       
	 * @return EventSettingResponse
	 */
	@GET
	@Produces("application/json")
	@Path("/event/settings")
	public EventSettingResponse view(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "event_id") UUID eventId) {
		validateAccessToken(accessToken);
		EventSettingResponse response = interactService.eventSetting(eventId);
		return response;
	}
	
	/***
	 * This API is used to edit the event settings.
	 * 
	 * @param 
	 *       - Whosup access token,eventID
	 *       
	 * @return Status
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/event/advanced")
	public Status editSettings(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.editSettings(request, user.getUserID());
	}
	
	/***
	 * This API is used to update event location.
	 * 
	 * @param 
	 *       - Whosup access token,eventID and required fields.
	 *       
	 * @return Status
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/location")
	public Status eventLocation(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.eventLocation(request, user.getUserID());
	}
	
	/***
	 * This API is used to delete user from event.
	 * 
	 * @param 
	 *       - Whosup access token,eventID.
	 *       
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/delete/user")
	public Status deleteUser(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.deleteUser(user.getUserID(),
				request.getEventId(), request.getDeleteUserId());
	}
	
	/***
	 * This API is used to booking the event.
	 * 
	 * @param 
	 *       - Whosup access token,eventID and required fields.
	 *       
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/booking")
	public Status booking(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		if (request.getIsCancel()) {
			return interactService.bookingCancel(user, request.getEventId());
		}
		return interactService.bookingConfirm(user, request);
	}
	
	/***
	 * This API is view picture list of event.
	 * 
	 * @param 
	 *       - Whosup access token,eventID
	 *       
	 * @return Status
	 */
	@GET
	@Produces("application/json")
	@Path("/picture/list")
	public Status getPictures(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "event_id") UUID eventId) {
		User user = validateAccessToken(accessToken);
		return interactService.getPicture(eventId, user.getUserID());
	}
	
	/***
	 * This API is get feedback.
	 * 
	 * @param 
	 *       - Whosup access token,eventID
	 *       
	 * @return Status
	 */
	@GET
	@Produces("application/json")
	@Path("/feedback/get")
	public Status getFeedback(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "event_id") UUID eventId) {
		User user = validateAccessToken(accessToken);
		return interactService.getFeedback(user.getUserID(), eventId);
	}
	
	/***
	 * This API is set feedback.
	 * 
	 * @param 
	 *       - Whosup access token,eventID
	 *       
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/feedback")
	public Status feedback(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.feedback(user.getUserID(), request);
	}
	
	/***
	 * This API is used to add the picture.
	 * 
	 * @param 
	 *       - Whosup access token,eventID and picturePath.
	 *       
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/picture/add")
	public Status addPicture(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.addPicture(user.getUserID(), request);
	}
	
	/***
	 * This API is used to like and unlike the event picture.
	 * 
	 * @param 
	 *       - Whosup access token,eventID and picturePath.
	 *       
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/picture/like")
	public Status likePicture(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		if (request.getIsLike())
			return interactService.likePicture(request.getEventId(),
					request.getPictureDate(), user.getUserID());
		else
			return interactService.unlikePicture(request.getEventId(),
					request.getPictureDate(), user.getUserID());
	}
	
	/***
	 * This API is used to edit max place and min place in event.
	 * 
	 * @param 
	 *       - Whosup access token,eventID,maxPlace and minPlace.
	 *       
	 * @return Status
	 */
	@POST
	@Produces("application/json")
	@Path("/places/edit")
	public Status editPlaces(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.editPlaces(user.getUserID(), request);
	}
	
	/***
	 * This API is used to edit the event date,start time and end time.
	 * 
	 * @param 
	 *       - Whosup access token,eventID,eventDate,eventStartTime and eventEndTime.
	 *       
	 * @return Status
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/date")
	public Status eventDate(InteractRequest request) {
		validateAccessToken(request.getAccessToken());
		return interactService.eventDate(request);
	}

	@POST
	@Produces("application/json")
	@Path("/gather")
	public Status gatherAgain(InteractRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return interactService.gatherAgain(user.getUserID(),
				request.getEventId());
	}
	
	@GET
	@Produces("application/json")
	@Path("/test")
	public String checkMsg() {
		User user = new User();
		String str=commonService.confirmedMessage("6ce38528-b57f-4e06-a34e-e0ef775ed1c3", user, "Success");
		return str;
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