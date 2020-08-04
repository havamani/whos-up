package com.whosup.resource;

import java.util.Date;
import java.util.List;
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
import com.whosup.dao.UserContactDao;
import com.whosup.dao.UserDao;
import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.UserContactRequest;
import com.whosup.model.request.ContactDetail;
import com.whosup.model.request.NetworkRequest;
import com.whosup.model.response.CommonInterest;
import com.whosup.model.response.ContactCount;
import com.whosup.model.response.Domain;
import com.whosup.model.response.LoginResponse;
import com.whosup.model.response.MyCrowd;
import com.whosup.model.response.Profile;
import com.whosup.model.response.SettingsResponse;
import com.whosup.model.response.Status;
import com.whosup.model.response.Suggestion;
import com.whosup.service.CommonService;
import com.whosup.service.FacebookService;
import com.whosup.service.LinkedInService;
import com.whosup.service.NetworkService;

@Path("/network")
@Component
public class NetworkResource {

	@Autowired
	private FacebookService fbService;
	@Autowired
	private LinkedInService lnService;
	@Autowired
	private NetworkService networkService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAccessTokenDao userAccessTokenDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserContactDao userContactDao;

	/***
	 * This API authenticates facebook user and creates a new whosup user or
	 * adds facebook account to existing user if accessToken is provided
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @param fb_accessToken
	 *            - facebook access Token
	 * @param deviceID
	 *            - mobile deviceID for sending notification
	 * @return Whosup acess Token
	 */
	@GET
	@Produces("application/json")
	@Path("/fb/login")
	public LoginResponse fbLogin(
			@QueryParam(value = "fb_access_token") String fb_accessToken,
			@QueryParam(value = "device_ID") String deviceID,
			@QueryParam(value = "deviceType") String deviceType) {

		if (fb_accessToken == null || fb_accessToken.isEmpty())
			throw new AppException(new AppErrorMessage("400",
					"Please provide Facebook Access Token"));

		return fbService.facebookLogin(fb_accessToken, deviceID, deviceType);
	}

	/***
	 * This API authenticates linkedIn user and creates a new whosup user or
	 * adds linkedIn account to existing user if accessToken is provided
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @param linkedInAccessToken
	 *            - linkedIn access Token
	 * @param deviceID
	 *            - mobile deviceID for sending notification
	 * @return Whosup acess Token
	 */
	@POST
	@Produces("application/json")
	@Path("/linked/login")
	public SettingsResponse linkedLogin(NetworkRequest request) {

		if (request.getAccessToken() == null)
			throw new AppException(new AppErrorMessage("400",
					"Please provide Access Token"));
		if (request.getLinkedInAccessToken() == null
				|| request.getLinkedInAccessToken().isEmpty())
			throw new AppException(new AppErrorMessage("400",
					"Please provide linkedIn Access Token"));
		validateAccessToken(request.getAccessToken());
		User user = userDao.findById(userAccessTokenDao
				.findUserIdByAccessToken(lnService.likedInLogin(
						request.getAccessToken(),
						request.getLinkedInAccessToken())));
		return new SettingsResponse(user, networkService.getContactCount(user
				.getUserID()), networkService.emailDomain(user)
				.getActiveUserCount());
	}

	/***
	 * User's profile details will be provided if valid access Token is provided
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @return Profile details of user
	 */
	@GET
	@Produces("application/json")
	@Path("/settings")
	public SettingsResponse login(
			@QueryParam(value = "access_token") UUID accessToken) {

		User user = validateAccessToken(accessToken);
		SettingsResponse settingResponse = new SettingsResponse(user,
				networkService.getContactCount(user.getUserID()),
				networkService.emailDomain(user).getActiveUserCount());
		return settingResponse;
	}

	/***
	 * This API is used for updating profile picture of a user
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @param picturePath
	 *            - path of picture used to update profile picture
	 * @return success or failure message
	 */
	@POST
	@Produces("application/json")
	@Path("/profile/picture")
	public Status updateProfilePicture(NetworkRequest request) {
		return networkService.updateProfilePicture(
				validateAccessToken(request.getAccessToken()),
				request.getPicturePath());
	}

	/***
	 * This API is used for updating user's current location
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @param picturePath
	 *            - user's current location in latLong
	 * @return success or failure message
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/location")
	public Status updateUserLocation(NetworkRequest request) {
		return networkService.updateUserLocation(
				validateAccessToken(request.getAccessToken()), request);
	}

	/***
	 * This API is used to get list of contacts of a user and contacts will be
	 * filter based on given param
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @param filterName
	 *            - filter param will be "fb", "ln", "all", "my_nte",
	 *            "my_crowd", "whosup"
	 * @return list of contacts
	 */
	@GET
	@Produces("application/json")
	@Path("/contacts")
	public List<UserContact> contacts(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "filter_name") String filterName) {

		User user = validateAccessToken(accessToken);
		if (filterName == null)
			throw new AppException(new AppErrorMessage("400",
					"Please provide filter_name"));
		return networkService.contacts(user.getUserID(), filterName);
	}

	@GET
	@Produces("application/json")
	@Path("/contact/search")
	public List<User> contactSearch(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "search") String search) {
       System.out.print("in");
		User user = validateAccessToken(accessToken);
		if (search == null || search.length() < 1)
			throw new AppException(new AppErrorMessage("400",
					"Please provide search term"));
		List<User> users = networkService.contactSearch(user.getUserID(),
				search);
		return users;
	}

	/***
	 * This API used for saving user's favourite contacts If User is already
	 * Friend then save into user's favourite contacts else if User tries to
	 * make Fav from the Domain list, we make autofriend to the user and
	 * Favourite
	 * 
	 * @param request
	 *            - require fields are accessToken, contactId and favourite or
	 *            not
	 * @return success or failure message
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/starred")
	public Status starContact(NetworkRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		UserContact friendContact = userContactDao.findByContactId(
				user.getUserID(), request.getContactId());
		if (friendContact != null
				&& friendContact.getConnectionStatusID().equals(new Integer(0)))
			return networkService.setFavourite(user.getUserID(),
					request.getContactId(), request.getIsFavourite());
		else
			return networkService.autoFriend(user, request.getContactId());
	}

	/***
	 * user can get their saved emailDomain from this API
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @return EmailDomain with no. of users in same domain will be provided
	 */
	@GET
	@Produces("application/json")
	@Path("/emaildomain")
	public Domain emailDomains(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return networkService.emailDomain(user);
	}

	/***
	 * This API adds email domain or mobile number depending upon tag field in
	 * request param and sends verification code
	 * 
	 * @param request
	 *            - id, tag and accesstoken are required fields
	 * @return success or failure message
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/add")
	public Status add(NetworkRequest request) throws Exception {

		User user = validateAccessToken(request.getAccessToken());
		if (request.getId() == null)
			return new Status("Please provide ID");
		switch (request.getTag()) {
		case "email":
			return networkService.addEmail(user, request.getId());
		case "mobile":
			return networkService.addMobile(user, request.getId());
		default:
			throw new AppException(new AppErrorMessage("400",
					"Please provide proper tag"));
		}
	}

	/***
	 * This API verifies email domain or mobile number depending upon tag field
	 * in request param
	 * 
	 * @param request
	 *            - id, tag, verification code and accesstoken are required
	 *            fields
	 * @return success or failure message
	 * @throws Exception
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/verify")
	public Status verify(NetworkRequest request) {

		User user = validateAccessToken(request.getAccessToken());
		if (request.getId() == null)
			return new Status("Please provide ID");
		if (request.getTag().equals("email"))
			return networkService.verifyEmail(user, request.getId(),
					request.getCode());
		else
			return networkService.verifyMobile(user, request.getId(),
					request.getCode());
	}

	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/betaDomain")
	public Status addWithoutEmail(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return networkService.verifyEmail(user, "", "WITHOUTDOMAIN");
	}

	/***
	 * This API user for removing current email domain
	 * 
	 * @param request
	 *            - access token and emailID
	 * @return success or failure message
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/domain/remove")
	public Status removeEmail(NetworkRequest request) {

		User user = validateAccessToken(request.getAccessToken());
		if (request.getId() == null)
			return new Status("Please provide email ID");
		return networkService.removeEmail(user, request.getId());
	}

	/***
	 * This API provides user's no of contacts and no of contact requests
	 * 
	 * @param accessToken
	 *            - whosup access token
	 * @return contact count and request count
	 */
	@GET
	@Produces("application/json")
	@Path("/contact/count")
	public ContactCount contactDetails(
			@QueryParam(value = "access_token") UUID accessToken) {

		User user = validateAccessToken(accessToken);
		return networkService.getContactCount(user.getUserID());
	}

	/***
	 * For accepting and rejecting a contact request this APi is used
	 * 
	 * @param request
	 *            - access token, contactId and accept/reject
	 * @return success or failure message
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/contact/response")
	public List<UserContactRequest> contactResponse(NetworkRequest request) {
		validateAccessToken(request.getAccessToken());
		return networkService.contactResponse(request);
	}

	/***
	 * This API is used to send contact request to a user inside whosup
	 * 
	 * @param request
	 *            - access token and contactId
	 * @return success or failure message
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/contact/request")
	public Suggestion contactRequest(NetworkRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return networkService.contactRequest(user, request);
	}

	/***
	 * To get user's list of contact request this API is used
	 * 
	 * @param accessToken
	 *            - whosup access token
	 * @return success or failure message
	 */
	@GET
	@Produces("application/json")
	@Path("/contact/requestlist")
	public List<UserContactRequest> requestList(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return networkService.requestList(user.getUserID());
	}

	/***
	 * This API updates user's profile settings
	 * 
	 * @param request
	 *            - field that requires to update
	 * @return success or failure message
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/settings/update")
	public Status updateSettings(NetworkRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return networkService.updateProfile(user, request);
	}

	/***
	 * This API is used to import list of mobile contacts from mobile or all
	 * contacts from gmail server to whosup
	 * 
	 * @param request
	 *            - tag, isImport, access token, gmail access token and list of
	 *            contacts depends of situation
	 * @return success or failure message
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/import")
	public Suggestion importContact(NetworkRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		if (request.getIsImport()) {
			if (request.getTag().equals("gmail"))
				networkService.importGmail(user.getUserID(), request);
			else if (request.getTag().equals("mobile")) {
				if (request.getContacts() == null
						|| request.getContacts().isEmpty())
					return new Suggestion("Please provide some Contacts");
				networkService.importMobile(user.getUserID(), request);
			} else
				return new Suggestion("Please provide proper Tag Name");
			return networkService.suggestion(user.getUserID());
		} else
			return networkService.suggestion(user.getUserID());
	}

	
//	@GET
//	@Produces("application/json")
//	@Path("local/test")
//	public Suggestion check(@QueryParam(value = "access_token") UUID accessToken) {
//		User user = validateAccessToken(accessToken);
//		return networkService.suggestion(user.getUserID());
//	}
	/***
	 * This API is used to delete Contact suggesstions
	 * 
	 * @param request
	 *            - access token and contactId
	 * @return success or failure message
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/import/delete")
	public Status deleteSuggestion(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "contact_id") UUID contactId,
			@QueryParam(value = "mobile_or_email_id") String mobileOrEmailId) {
		User user = validateAccessToken(accessToken);
		if (user.getUserID() != null && contactId != null) {
			return networkService.deleteSuggestion(user.getUserID(), contactId,
					mobileOrEmailId);
		}
		return new Status("400");
	}

	/***
	 * To invite a user's friend to whosup using their email ID
	 * 
	 * @param request
	 *            - access token and contactId
	 * @return success or failure message
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/invite")
	public Suggestion inviteContact(NetworkRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return networkService.inviteContact(user, request.getId());
	}

	/***
	 * This API is used to get profile details of friend and their common
	 * interest, friends
	 * 
	 * @param accessToken
	 *            - whosup access token
	 * @param contactId
	 *            - contact user's ID
	 * @return friends profile and common interest
	 */
	@GET
	@Produces("application/json")
	@Path("/friendProfile")
	public Profile friendProfile(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "contactId") UUID contactId) {
		User user = validateAccessToken(accessToken);
		return networkService.friendProfile(user.getUserID(), contactId);
	}

	/***
	 * To signout a user from a device this API is used
	 * 
	 * @param accessToken
	 *            - whosup access token
	 * @return success or failure message
	 */
	@GET
	@Produces("application/json")
	@Path("/signout")
	public Status signOut(@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return networkService.signOut(user);
	}

	/***
	 * This API is used to save list of favourite contacts in user's contact
	 * list
	 * 
	 * @param request
	 *            - accesstoken and list of selected favourite users
	 * @return list of users
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/myCrowd")
	public MyCrowd myCrowd(NetworkRequest request) {
		User user = validateAccessToken(request.getAccessToken());
		return networkService.selectCrowd(user.getUserID(), request);
	}

	@GET
	@Produces("application/json")
	@Path("/appStore/link")
	public CommonInterest appStoreLink() {
		CommonInterest appStoreLink = new CommonInterest();
		appStoreLink.setAppStoreLink("Coming soon in App Store.");
		return appStoreLink;
	}

	@GET
	@Produces("application/json")
	@Path("/checkCode/expired")
	public User checkCodeExpiration(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		if (user != null) {
			if (user.getMobileVerificationExpiry().before(new Date()))
				return new User();
		}
		return user;
	}

	@GET
	@Produces("application/json")
	@Path("/getUser")
	public User test(@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return user;
	}

	@POST
	@Consumes("application/json")
	@Path("/mail")
	public List<ContactDetail> check(NetworkRequest request) {
		List<ContactDetail> gmailContacts = commonService.gmailContacts(request
				.getGmailAccessToken());
		return gmailContacts;
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
