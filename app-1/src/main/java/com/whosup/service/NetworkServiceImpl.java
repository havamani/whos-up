package com.whosup.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.DomainMasterKeyDao;
import com.whosup.dao.EventDao;
import com.whosup.dao.OwesMeDao;
import com.whosup.dao.UserAccessTokenDao;
import com.whosup.dao.UserContactDao;
import com.whosup.dao.UserContactInviteDao;
import com.whosup.dao.UserContactRequestDao;
import com.whosup.dao.UserDao;
import com.whosup.dao.UserInterestedEventDao;
import com.whosup.dao.UserLocationLogDao;
import com.whosup.dao.UserSuggestedContactDao;
import com.whosup.dao.UserSuggestedInviteDao;
import com.whosup.dao.UserVerifiedDomainDao;
import com.whosup.dao.UserVerifiedGmailAddressDao;
import com.whosup.dao.UserVerifiedMobileNumberDao;
import com.whosup.dao.WeatherDao;
import com.whosup.model.DomainMasterKey;
import com.whosup.model.Event;
import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.UserContactKey;
import com.whosup.model.UserContactRequest;
import com.whosup.model.UserLocationLog;
import com.whosup.model.UserLocationLogKey;
import com.whosup.model.UserSuggestedContact;
import com.whosup.model.UserSuggestedContactKey;
import com.whosup.model.UserSuggestedInvite;
import com.whosup.model.UserSuggestedInviteKey;
import com.whosup.model.UserVerifiedDomain;
import com.whosup.model.UserVerifiedDomainKey;
import com.whosup.model.UserVerifiedGmailAddress;
import com.whosup.model.UserVerifiedMobileNumber;
import com.whosup.model.Weather;
import com.whosup.model.WeatherKey;
import com.whosup.model.userVerifiedMobileNumberkey;
import com.whosup.model.request.ContactDetail;
import com.whosup.model.request.NetworkRequest;
import com.whosup.model.response.CommonInterest;
import com.whosup.model.response.ContactCount;
import com.whosup.model.response.Domain;
import com.whosup.model.response.MyCrowd;
import com.whosup.model.response.Profile;
import com.whosup.model.response.Status;
import com.whosup.model.response.SuggestList;
import com.whosup.model.response.Suggestion;

public class NetworkServiceImpl implements NetworkService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserContactDao userContactDao;
	@Autowired
	private UserAccessTokenDao userAccessTokenDao;
	@Autowired
	private UserLocationLogDao userLocationLogDao;
	@Autowired
	private UserContactInviteDao userContactInviteDao;
	@Autowired
	private UserInterestedEventDao userInterestedEventDao;
	@Autowired
	private UserSuggestedContactDao userSuggestedContactDao;
	@Autowired
	private UserSuggestedInviteDao userSuggestedInviteDao;
	@Autowired
	private UserContactRequestDao userContactRequestDao;
	@Autowired
	private UserVerifiedMobileNumberDao userVerifiedMobileNumberDao;
	@Autowired
	private UserVerifiedGmailAddressDao userVerifiedGmailAddressDao;
	@Autowired
	private UserVerifiedDomainDao userVerifiedDomainDao;
	@Autowired
	private DomainMasterKeyDao domainMasterKeyDao;
	@Autowired
	private WeatherDao weatherDao;
	@Autowired
	private OwesMeDao owesMeDao;

	@Override
	public Status updateProfilePicture(User user, String picturePath) {
		user.setPhotoPath(picturePath);
		userDao.update(user);
		List<UserContact> updatePhotoPathForUserContacts = userContactDao
				.findOtherContacts(user.getUserID());
		for (UserContact userContact : updatePhotoPathForUserContacts) {
			userContact.setContactPhotoPath(picturePath);
		}
		userContactDao.update(updatePhotoPathForUserContacts);
		return new Status("Success");

	}

	@Override
	public Status updateUserLocation(User user, NetworkRequest request) {
		user.setRecentLocLatLong(request.getLatLong());
		userDao.update(user);
		userLocationLogDao.save(new UserLocationLog(new UserLocationLogKey(user
				.getUserID(), new Date()), request.getLatLong()));
		List<UserContact> usercontacts = userContactDao.findOtherContacts(user
				.getUserID());
		if (usercontacts != null && usercontacts.size() > 0) {
			for (UserContact u : usercontacts) {
				u.setLastKnownLatlong(request.getLatLong());
			}
			userContactDao.update(usercontacts);
		}

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.add(Calendar.HOUR_OF_DAY, 8);
		Weather recentWeather = weatherDao.selectRecentWeather(request);
		if (recentWeather != null
				&& recentWeather.getPk().getDateSubmitted()
						.before(cal.getTime())) {
			saveUserWeather(user, recentWeather);
		} else {
			JSONObject response = commonService.getCurrentWeather(request
					.getLatLong());
			Weather weather = new Weather(new WeatherKey(request.getAdmin3(),
					request.getAdmin2(), request.getAdmin1(),
					request.getCountry(), new Date()), request.getLatLong(),
					response.optJSONArray("weather").optJSONObject(0)
							.optString("main"), response
							.optJSONArray("weather").optJSONObject(0)
							.optString("description"), response.optJSONObject(
							"main").optDouble("temp_min"), response
							.optJSONObject("main").optDouble("temp_max"),
					response.optJSONObject("wind").optDouble("speed"));
			weatherDao.save(weather);
			saveUserWeather(user, weather);
		}

		// commonService.kafkaProducerUser(user.getUserID());
		return new Status("Success");
	}

	protected void saveUserWeather(User user, Weather weather) {
		user.setWeatherLastUpdated(new Date());
		user.setMain(weather.getMain());
		user.setDescription(weather.getDescription());
		user.setMinTemp(weather.getMinTemp());
		user.setMaxTemp(weather.getMaxTemp());
		user.setWindSpeed(weather.getWindSpeed());
		userDao.update(user);
	}

	@Override
	public List<UserContact> contacts(UUID userID, String filterName) {
		List<UserContact> userContacts;
		switch (filterName) {
		case "fb":
			userContacts = userContactDao.findContactsByFb(userID);
			break;
		case "ln":
			userContacts = userContactDao.findContactsByLn(userID);
			break;
		case "all":
			userContacts = userContactDao.findContacts(userID);
			userContacts = findSameDomainContacts(userID, userContacts);
			break;
		case "whosup":
			userContacts = userContactDao.findContactsByWhosUp(userID);
			break;
		case "my_net":
			List<UUID> userIDs = userVerifiedDomainDao
					.findUsersByDomain(userVerifiedDomainDao.findByUser(userID)
							.getDomainName());
			userContacts = userContactDao
					.findContactsByNetwork(userID, userIDs);
			userContacts = findSameDomainContacts(userID, userContacts);
			break;
		case "my_crowd":
			userContacts = userContactDao.findMyCrowd(userID);
			break;
		default:
			throw new AppException(new AppErrorMessage("400",
					"Please provide proper filter_name"));
		}

		return userContacts;
	}

	private List<UserContact> findSameDomainContacts(UUID userID,
			List<UserContact> userContacts) {
		UserVerifiedDomain userdomain = userVerifiedDomainDao
				.findByUser(userID);
		List<UUID> sameDomainUserIDs = new ArrayList<UUID>();
		if (userdomain != null) {
			sameDomainUserIDs = userVerifiedDomainDao
					.findUsersByDomain(userdomain.getDomainName());
			if (sameDomainUserIDs.contains(userID)) {
				sameDomainUserIDs.remove(userID);
			}
		}
		for (UUID usersInSameDomain : sameDomainUserIDs) {
			boolean ifUserIsNotThere = true;
			for (UserContact u : userContacts) {
				if (u.getPk().getContactUserID().equals(usersInSameDomain)) {
					ifUserIsNotThere = false;
					break;
				}
			}
			if (ifUserIsNotThere) {
				User newUser = userDao.findById(usersInSameDomain);
				if (newUser != null) {
					UserContact newUserContact = new UserContact();
					UserContactKey newUserContactkey = new UserContactKey();
					newUserContactkey.setUserID(usersInSameDomain);
					newUserContactkey.setContactUserID(newUser.getUserID());
					newUserContact.setPk(newUserContactkey);
					newUserContact.setContactFullName(newUser.getFullName());
					newUserContact.setContactPhotoPath(newUser.getPhotoPath());
					newUserContact.setIsStarred(false);
					userContacts.add(newUserContact);
				}
			}
		}
		return userContacts;
	}

	@Override
	public List<User> contactSearch(UUID userID, String search) {
		return userDao.findBySearch(search, userID);

	}

	@Override
	public List<UserContactRequest> contactResponse(NetworkRequest request) {
		if (acceptRequest(request) == 200) {
			User user = userDao.findById(userAccessTokenDao
					.findUserIdByAccessToken(request.getAccessToken()));
			User contact = userDao.findById(request.getContactId());
			UserContact userContact = userContactDao.findByContactId(
					user.getUserID(), contact.getUserID());
			try {
				JSONObject notification = new JSONObject();
				notification.put("type", "REQUEST_ACCEPTED");
				notification.put(
						"message",
						"Your Friend requested was accepted by "
								+ user.getFullName());
				commonService.notify(
						userDao.findById(userContact.getContactUserID()),
						notification.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return requestList(userAccessTokenDao
					.findUserIdByAccessToken(request.getAccessToken()));
		} else
			throw new AppException(new AppErrorMessage("400",
					"Error processing request"));
	}

	protected Integer acceptRequest(NetworkRequest request) {
		User user = userDao.findById(userAccessTokenDao
				.findUserIdByAccessToken(request.getAccessToken()));
		User contact = userDao.findById(request.getContactId());

		UserSuggestedContact suggestContact = userSuggestedContactDao
				.findByContactId(user.getUserID(), contact.getUserID());
		UserContact userContact = userContactDao.findByContactId(
				user.getUserID(), contact.getUserID());
		if (request.getAccept()) {
			userContact.setConnectionStatusID(0);
			userContact.setShareInterest(request.getShareInterest());
			userContact.setDateConnected(new Date());
			userContact.setIsConnectedViaWhosup(true);
		} else
			userContact.setConnectionStatusID(5);
		userContactDao.update(userContact);
		if (suggestContact != null)
			userSuggestedContactDao.delete(suggestContact);

		suggestContact = userSuggestedContactDao.findByContactId(
				contact.getUserID(), user.getUserID());

		userContact = userContactDao.findByContactId(contact.getUserID(),
				user.getUserID());
		if (request.getAccept()) {
			userContact.setConnectionStatusID(0);
			userContact.setShareInterest(request.getShareInterest());
			userContact.setDateConnected(new Date());
			userContact.setIsConnectedViaWhosup(true);
		} else
			userContact.setConnectionStatusID(4);
		userContactDao.update(userContact);
		if (suggestContact != null)
			userSuggestedContactDao.delete(suggestContact);
		userContactRequestDao.delete(userContactRequestDao.findByContactId(
				user.getUserID(), contact.getUserID()));
		return 200;
	}

	@Override
	public Suggestion contactRequest(User user, NetworkRequest contactRequest) {
		if (user.getUserID().equals(contactRequest.getContactId())) {
			return suggestion(user.getUserID());
		}
		User contact = userDao.findById(contactRequest.getContactId());
		if (userContactRequestDao.findByContactId(user.getUserID(),
				contactRequest.getContactId()) != null) {
			NetworkRequest request = new NetworkRequest();
			request.setAccessToken(user.getAccessToken());
			request.setContactId(contactRequest.getContactId());
			request.setAccept(true);
			request.setShareInterest(false);
			acceptRequest(request);
			return suggestion(user.getUserID());
		}
		if (userContactRequestDao.findByContactId(
				contactRequest.getContactId(), user.getUserID()) != null)
			throw new AppException(new AppErrorMessage("400",
					"you have already send request to this user"));

		UserContact userContact = userContactDao.findByContactId(
				user.getUserID(), contactRequest.getContactId());
		UserContact otherContact = userContactDao.findByContactId(
				contactRequest.getContactId(), user.getUserID());
		if (userContact != null
				&& userContact.getConnectionStatusID().intValue() == 0)
			throw new AppException(new AppErrorMessage("400",
					"you are already friend with this user"));
		if (otherContact != null
				&& otherContact.getConnectionStatusID().intValue() == 0) {
			if (userContact != null) {
				userContact.setConnectionStatusID(0);
				userContact.setShareInterest(false);
				userContact.setDateConnected(new Date());
				userContact.setIsConnectedViaEmailDomain(true);
				userContactDao.update(userContact);
			} else {
				userContact = new UserContact(new UserContactKey(
						user.getUserID(), contact.getUserID()),
						contact.getFullName(), contact.getPhotoPath());
				userContact.setDateRequested(new Date());
				userContact.setConnectionStatusID(0);
				userContact.setShareInterest(false);
				userContact.setDateConnected(new Date());
				userContact.setIsConnectedViaEmailDomain(true);
				userContactDao.save(userContact);
			}
			try {
				JSONObject notification = new JSONObject();
				notification.put("type", "REQUEST_RECEIVED");
				notification.put("message",
						"Request received from " + user.getFullName());
				commonService.notify(
						userDao.findById(contactRequest.getContactId()),
						notification.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return suggestion(user.getUserID());
		}

		UserContactRequest request = new UserContactRequest(
				new UserSuggestedContactKey(contactRequest.getContactId(),
						user.getUserID()), user.getFullName(),
				user.getPhotoPath(), new Date());
		userContactRequestDao.save(request);
		UserSuggestedContact suggestContact = userSuggestedContactDao
				.findByContactId(user.getUserID(),
						contactRequest.getContactId());
		if (suggestContact != null) {
			suggestContact.setIsRequestSent(true);
			userSuggestedContactDao.save(suggestContact);
		}

		if (userContact != null) {
			userContact.setConnectionStatusID(2);
			userContact.setDateRequested(new Date());
			userContactDao.update(userContact);
		} else {
			UserContact newContact = new UserContact(new UserContactKey(
					user.getUserID(), contact.getUserID()),
					contact.getFullName(), contact.getPhotoPath());
			newContact.setDateRequested(new Date());
			newContact.setConnectionStatusID(2);
			userContactDao.save(newContact);
		}

		if (otherContact != null) {
			otherContact.setConnectionStatusID(1);
			otherContact.setDateRequested(new Date());
			userContactDao.update(otherContact);
		} else {
			UserContact newContact = new UserContact(new UserContactKey(
					contact.getUserID(), user.getUserID()), user.getFullName(),
					user.getPhotoPath());
			newContact.setDateRequested(new Date());
			newContact.setConnectionStatusID(1);
			userContactDao.save(newContact);
		}
		try {
			JSONObject notification = new JSONObject();
			notification.put("type", "REQUEST_RECEIVED");
			notification.put("message",
					"Request received from " + user.getFullName());
			commonService.notify(
					userDao.findById(contactRequest.getContactId()),
					notification.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return suggestion(user.getUserID());
	}

	@Override
	public List<UserContactRequest> requestList(UUID userID) {
		return userContactRequestDao.findByUserId(userID);
	}

	@Override
	public Status updateProfile(User user, NetworkRequest profile) {
		user.setIsAvailableSearch(profile.getIsAvailableSearch());
		user.setIsSilent(profile.getIsSilent());
		user.setSendInvitesToEmail(profile.getSendEmail());
		if (profile.getIsPayPalRemoved()) {
			// user.setStripeId("");
			user.setStripeSignInToken("");
		}
		user.setSystemLanguage(profile.getSystemLanguage());
		user.setAdditionalLanguage(profile.getAdditionalLanguages());
		userDao.update(user);
		return new Status("Success");
	}

	@Override
	public ContactCount getContactCount(UUID userID) {
		return new ContactCount(userContactDao.findContactCount(userID),
				userContactDao.findRequestCount(userID));
	}

	@Override
	public Suggestion suggestion(UUID userID) {
		Suggestion suggestion = new Suggestion("201");
		List<UserSuggestedContact> add = userSuggestedContactDao
				.findByUserId(userID);
		List<UserSuggestedInvite> invite = userSuggestedInviteDao
				.findByUserId(userID);
		System.out.println("Add size:" + add.size());
		if (add.size() > 0) {
			add = deleteFriends(userID, add);
		}

		System.out.println("Invite size:" + invite.size());
		if (invite.size() > 0) {
			invite = deleteUsersAlreadyFriend(userID, invite);
		}
		List<SuggestList> response = new ArrayList<SuggestList>();
		if (add != null) {
			for (UserSuggestedContact u : add) {
				SuggestList sug = new SuggestList();
				sug.setContactFullName(u.getContactName());
				sug.setContactDetail("");
				sug.setContactPhotoPath(u.getContactPhotoPath());
				sug.setContactUserID(u.getContactID());
				sug.setOnWhosUp(true);
				sug.setMessage(u.getSuggestionReason());
				if (u.getIsRequestSent())
					sug.setIsInvitedAlready(true);
				else
					sug.setIsInvitedAlready(false);
				response.add(sug);
			}
		}
		if (invite != null) {
			for (UserSuggestedInvite inv : invite) {
				if (inv.getContactName() == null
						|| inv.getContactName().isEmpty())
					continue;
				SuggestList sug = new SuggestList();
				sug.setContactFullName(inv.getContactName());
				sug.setContactDetail(inv.getMobileOrEmailId());
				sug.setContactPhotoPath(inv.getContactPhotoPath());
				sug.setOnWhosUp(false);
				sug.setMessage(inv.getSuggestionReason());
				if (inv.getMobileOrEmailId().contains("@"))
					sug.setIsMobile(false);
				else
					sug.setIsMobile(true);
				if (inv.getCodeLastGenerated() == null
						|| inv.getCodeLastGenerated().isEmpty())
					sug.setIsInvitedAlready(false);
				else
					sug.setIsInvitedAlready(true);
				response.add(sug);
			}
		}
		Collections.sort(response, new Comparator<SuggestList>() {
			public int compare(SuggestList o1, SuggestList o2) {
				return o1.getContactFullName().compareToIgnoreCase(
						o2.getContactFullName());
			}
		});
		suggestion.setResponse(response);
		return suggestion;
	}

	@Override
	public Status deleteSuggestion(UUID userId, UUID contactId,
			String mobileOrEmailId) {
		UserSuggestedContact suggestedContact = userSuggestedContactDao
				.findByContactId(userId, contactId);
		UserSuggestedInvite findByUserIdAndOtherId = userSuggestedInviteDao
				.findByUserIdAndOtherId(userId, mobileOrEmailId);
		if (suggestedContact != null) {
			userSuggestedContactDao.delete(suggestedContact);
			return new Status("200");
		} else if (findByUserIdAndOtherId != null) {
			userSuggestedInviteDao.delete(findByUserIdAndOtherId);
			return new Status("200");
		}

		return new Status("400");
	}

	@Override
	public Domain emailDomain(User user) {
		if (!user.getVerifiedDomainEmail().isEmpty()
				&& user.getVerifiedDomainEmail() != null) {
			Domain domain = new Domain(user.getVerifiedDomainEmail(), true,
					userVerifiedDomainDao.findDomainCount(userVerifiedDomainDao
							.findByUser(user.getUserID()).getDomainName()));
			return domain;
		}
		return new Domain();
	}

	@Override
	public Status addEmail(User user, String emailID) {
		if (user.getVerifiedDomainEmail() != null
				&& !user.getVerifiedDomainEmail().isEmpty())
			throw new AppException(
					new AppErrorMessage("400",
							"you have already registered with a domain. Please signout that"));

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, 30);
		user.setDomainEmail(emailID);
		user.setDomainVerificationCode(getRandomCode());
		user.setDomainVerificationExpiry(cal.getTime());
		userDao.update(user);
		DomainMasterKey domain = domainMasterKeyDao.selectDomain(emailID
				.substring(emailID.indexOf('@') + 1));
		if (domain == null)
			commonService.sendMail(emailID, "Who's Up Confirmation Code",
					"Hi,\n\tYour confirmation code for email verification is "
							+ user.getDomainVerificationCode());
		return new Status("Success");
	}

	@Override
	public Status verifyEmail(User user, String emailID, String code) {
		List<UUID> userIds = new ArrayList<UUID>();
		if (code.equals("WITHOUTDOMAIN")) {
			user.setVerifiedDomainEmail("betatest@earlyaccessbeta.com");
			userDao.update(user);
			UserVerifiedDomain domain = new UserVerifiedDomain(
					new UserVerifiedDomainKey(user.getUserID(),
							user.getVerifiedDomainEmail()));
			userIds = userVerifiedDomainDao.findUsersByDomain(domain
					.getDomainName());
			userVerifiedDomainDao.save(domain);
		} else {
			DomainMasterKey domainKey = domainMasterKeyDao.selectDomain(emailID
					.substring(emailID.indexOf('@') + 1));
			if (user.getDomainVerificationExpiry().before(new Date()))
				throw new AppException(new AppErrorMessage("400",
						"Verification Code Expires"));
			else if (user.getDomainVerificationCode().equals(code)
					|| (domainKey != null && domainKey.getMasterKey().equals(
							code))) {
				if (user.getDomainEmail().equals(emailID)) {
					user.setVerifiedDomainEmail(user.getDomainEmail());
					userDao.update(user);
					UserVerifiedDomain domain = new UserVerifiedDomain(
							new UserVerifiedDomainKey(user.getUserID(), user
									.getVerifiedDomainEmail().substring(
											user.getVerifiedDomainEmail()
													.indexOf('@') + 1)));
					userIds = userVerifiedDomainDao.findUsersByDomain(domain
							.getDomainName());
					userVerifiedDomainDao.save(domain);
				} else
					throw new AppException(new AppErrorMessage("400",
							"Verification Code Mismatch"));
			} else
				throw new AppException(new AppErrorMessage("400",
						"Domain Email Mismatch"));
		}

		if (userIds.contains(user.getUserID()))
			userIds.remove(user.getUserID());
		List<UUID> otherUserIds = new ArrayList<UUID>();
		if (userIds != null) {
			otherUserIds.addAll(userIds);
			List<UserContact> userContacts = userContactDao.findByContactIds(
					user.getUserID(), userIds);
			if (userContacts != null && userContacts.size() > 0) {
				for (UserContact u : userContacts) {
					if (userIds.contains(u.getContactUserID()))
						userIds.remove(u.getContactUserID());
				}
			}
			List<UserSuggestedContact> suggestContacts = userSuggestedContactDao
					.findByContactIds(user.getUserID(), userIds);
			if (suggestContacts != null) {
				for (UserSuggestedContact sug : suggestContacts) {
					if (userIds.contains(sug.getContactID()))
						userIds.remove(sug.getContactID());
				}
			}
			List<User> otherUsers = userDao.findUserByIds(userIds);
			suggestContacts = new ArrayList<UserSuggestedContact>();
			if (otherUsers != null && otherUsers.size() > 0) {
				for (User sugUser : otherUsers) {
					UserSuggestedContact userSuggestedContact = new UserSuggestedContact(
							new UserSuggestedContactKey(user.getUserID(),
									sugUser.getUserID()),
							sugUser.getFullName(), sugUser.getPhotoPath(),
							new Date(), "Matching : Same Domain");
					suggestContacts.add(userSuggestedContact);
				}
				userSuggestedContactDao.save(suggestContacts);
			}

			userContacts = userContactDao.findOtherContactsByUser(otherUserIds,
					user.getUserID());
			if (userContacts != null && userContacts.size() > 0) {
				for (UserContact u : userContacts) {
					if (otherUserIds.contains(u.getContactUserID()))
						otherUserIds.remove(u.getContactUserID());
				}
			}
			suggestContacts = userSuggestedContactDao.findByOtherContactIds(
					otherUserIds, user.getUserID());
			if (suggestContacts != null) {
				for (UserSuggestedContact sug : suggestContacts) {
					if (otherUserIds.contains(sug.getPk().getUserID()))
						otherUserIds.remove(sug.getPk().getUserID());
				}
			}
			suggestContacts = new ArrayList<UserSuggestedContact>();
			if (otherUserIds != null && otherUserIds.size() > 0) {
				for (UUID sugUser : otherUserIds) {
					UserSuggestedContact userSuggestedContact = new UserSuggestedContact(
							new UserSuggestedContactKey(sugUser,
									user.getUserID()), user.getFullName(),
							user.getPhotoPath(), new Date(),
							"Matching : Same Domain");
					suggestContacts.add(userSuggestedContact);
				}
				userSuggestedContactDao.save(suggestContacts);
			}
		}
		return new Status("Success");
	}

	@Override
	public Status addMobile(User user, String mobileID) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, 30);
		user.setMobileNumber(mobileID);
		Random r = new Random(System.currentTimeMillis());
		user.setMobileVerificationCode(String.valueOf(10000 + r.nextInt(20000)));
		user.setMobileVerificationExpiry(cal.getTime());
		Status message = commonService.message(
				user.getMobileVerificationCode(), user.getMobileNumber());
		userDao.update(user);
		return message;
	}

	@Override
	public Status verifyMobile(User user, String mobileID, String code) {
		if (user.getMobileVerificationExpiry().before(new Date()))
			return new Status("Verification Code Expires");
		else if (user.getMobileVerificationCode().equals(code)) {
			if (user.getMobileNumber().equals(mobileID.replace(" ", ""))) {
				UserVerifiedMobileNumber mobile = new UserVerifiedMobileNumber(
						new userVerifiedMobileNumberkey(user.getUserID(),
								mobileID));
				userVerifiedMobileNumberDao.save(mobile);
				user.setVerifiedMobileNumber(mobileID);
				userDao.update(user);
				contactChangeAfterNewId(user, mobileID);

				return new Status("Success");
			} else
				throw new AppException(new AppErrorMessage("300",
						"Mobile Number Mismatch"));
		} else
			throw new AppException(new AppErrorMessage("400",
					"Verification Code Mismatch"));
	}

	@Override
	public Status removeEmail(User user, String emailID) {
		user.setDomainEmail("");
		user.setDomainVerificationCode("");
		user.setVerifiedDomainEmail("");
		userDao.update(user);
		userVerifiedDomainDao.delete(userVerifiedDomainDao.findByUser(user
				.getUserID()));
		return new Status("Success");
	}

	@Override
	public Status setFavourite(UUID userID, UUID contactId, Boolean isFavourite) {
		if (!isFavourite) {
			UserContact oneSideUser = userContactDao.findByContactId(contactId,
					userID);
			if (oneSideUser == null) {
				// one side friend
				userContactDao.delete(userContactDao.findByContactId(userID,
						contactId));
				return new Status("Success");
			}

		}

		UserContact userContact = userContactDao.findByContactId(userID,
				contactId);
		userContact.setIsStarred(isFavourite);
		userContactDao.update(userContact);
		return new Status("Success");
	}

	@Override
	public Suggestion importGmail(UUID userID, NetworkRequest request) {
		List<ContactDetail> cons = commonService.gmailContacts(request
				.getGmailAccessToken());
		System.out.println(cons);
		Set<ContactDetail> oldContacts = new HashSet<ContactDetail>();
		Set<ContactDetail> newContacts = new HashSet<ContactDetail>();
		List<String> gmailIds = new ArrayList<String>();
		List<UUID> userIDs = new ArrayList<UUID>();
		for (ContactDetail con : cons)
			gmailIds.add(con.getId());
		List<UserVerifiedGmailAddress> oldUsersWithSameGmail = userVerifiedGmailAddressDao
				.findUsersByGmails(gmailIds);
		if (oldUsersWithSameGmail != null) {
			for (ContactDetail con : cons) {
				for (UserVerifiedGmailAddress old : oldUsersWithSameGmail) {
					if (con.getId().equals(old.getPk().getGmailAddress())
							&& !userIDs.contains(old.getPk().getUserID())) {
						userIDs.add(old.getPk().getUserID());
						con.setUserId(old.getPk().getUserID());
						oldContacts.add(con);
						oldUsersWithSameGmail.remove(old);
						break;
					}
				}
				newContacts.add(con);
			}
		}

		List<UserSuggestedInvite> inviteContacts = new ArrayList<UserSuggestedInvite>();
		for (ContactDetail con : newContacts) {
			InputStream photo = gmailPhoto(con.getFile() + "?access_token="
					+ request.getGmailAccessToken());
			UserSuggestedInvite inviteContact = new UserSuggestedInvite(
					new UserSuggestedInviteKey(userID, con.getId()),
					con.getUserName(),
					photo == null ? "https://whosup-server.s3.amazonaws.com/default.png"
							: commonService.uploadImage(photo, null),
					new Date(), "Matching : Gmail contact import");
			inviteContacts.add(inviteContact);
		}
		userSuggestedInviteDao.save(inviteContacts);

		List<UserContact> userContacts = userContactDao.findByContactIds(
				userID, userIDs);
		if (userContacts != null && userContacts.size() > 0) {
			for (UserContact u : userContacts) {
				if (userIDs.contains(u.getContactUserID()))
					userIDs.remove(u.getContactUserID());
			}
		}
		List<UserSuggestedContact> userSuggestedContacts = userSuggestedContactDao
				.findByContactIds(userID, userIDs);
		if (userSuggestedContacts != null && userSuggestedContacts.size() > 0) {
			for (UserSuggestedContact u : userSuggestedContacts) {
				if (userIDs.contains(u.getContactID()))
					userIDs.remove(u.getContactID());
			}
		}
		userSuggestedContacts = new ArrayList<UserSuggestedContact>();

		List<User> users = userDao.findUserByIds(userIDs);
		if (users != null && !users.isEmpty()) {
			for (ContactDetail con : oldContacts) {
				for (User user : users) {
					if (con.getUserId().equals(user.getUserID())
							&& !userID.equals(user.getUserID())) {
						UserSuggestedContact userSuggestedContact = new UserSuggestedContact(
								new UserSuggestedContactKey(userID,
										user.getUserID()), user.getFullName(),
								user.getPhotoPath(), new Date(),
								"Matching : Gmail contact import");
						userSuggestedContact.setIsSuggestedViaImportGmail(true);
						userSuggestedContacts.add(userSuggestedContact);
						users.remove(user);
						break;
					}
				}
			}
		}
		List<String> pendingEmailContacts = new ArrayList<>();
		User user = userDao.findById(userID);
		for (UserSuggestedInvite con : inviteContacts) {
			pendingEmailContacts.add(con.getMobileOrEmailId());
		}
		user.setGmailContactsPending(pendingEmailContacts);
		userDao.update(user);
		userSuggestedContactDao.save(userSuggestedContacts);

		return new Suggestion("Success");
	}

	protected InputStream gmailPhoto(String url) {
		try {
			URL requestMethod = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) requestMethod
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream in = urlConnection.getInputStream();
				return in;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Suggestion importMobile(UUID userID, NetworkRequest contactRequest) {

		Set<ContactDetail> oldContacts = new HashSet<ContactDetail>();
		Set<ContactDetail> newContacts = new HashSet<ContactDetail>();
		List<String> mobileNumbers = new ArrayList<String>();
		List<UUID> userIDs = new ArrayList<UUID>();
		NetworkRequest contact = new NetworkRequest();
		List<ContactDetail> cnct = new ArrayList<ContactDetail>();
		for (ContactDetail temp : contactRequest.getContacts()) {
			if (temp.getId() == null)
				continue;
			if (temp.getId().matches("^+[0-9]+")
					|| temp.getId().matches("^[0-9]+")) {
				cnct.add(temp);
			}
		}
		contact.setContacts(cnct);
		for (ContactDetail con : contact.getContacts())
			mobileNumbers.add(con.getId());
		List<UserVerifiedMobileNumber> oldUsersWithSameMobile = userVerifiedMobileNumberDao
				.findUsersByMobiles(mobileNumbers);
		if (oldUsersWithSameMobile != null) {
			for (ContactDetail con : contact.getContacts()) {
				for (UserVerifiedMobileNumber old : oldUsersWithSameMobile) {
					if (con.getId().equals(old.getMobileNumber())) {
						userIDs.add(old.getPk().getUserID());
						con.setUserId(old.getPk().getUserID());
						oldContacts.add(con);
						oldUsersWithSameMobile.remove(old);
						break;
					}
				}
				newContacts.add(con);
			}
		}

		List<UserSuggestedInvite> inviteContacts = new ArrayList<UserSuggestedInvite>();
		for (ContactDetail con : newContacts) {
			UserSuggestedInvite inviteContact = new UserSuggestedInvite(
					new UserSuggestedInviteKey(userID, con.getId()),
					con.getUserName(), commonService.uploadImage(
							new ByteArrayInputStream(Base64.getDecoder()
									.decode(con.getFile())), null), new Date(),
					"Matching : Mobile contact import");
			inviteContacts.add(inviteContact);
		}
		userSuggestedInviteDao.save(inviteContacts);

		List<UserContact> userContacts = userContactDao.findByContactIds(
				userID, userIDs);
		if (userContacts != null && userContacts.size() > 0) {
			for (UserContact u : userContacts) {
				if (userIDs.contains(u.getContactUserID()))
					userIDs.remove(u.getContactUserID());
			}
		}
		List<UserSuggestedContact> userSuggestedContacts = userSuggestedContactDao
				.findByContactIds(userID, userIDs);
		if (userSuggestedContacts != null && userSuggestedContacts.size() > 0) {
			for (UserSuggestedContact u : userSuggestedContacts) {
				if (userIDs.contains(u.getContactID()))
					userIDs.remove(u.getContactID());
			}
		}
		userSuggestedContacts = new ArrayList<UserSuggestedContact>();

		List<User> users = userDao.findUserByIds(userIDs);
		for (ContactDetail con : oldContacts) {
			for (User user : users) {
				if (con.getUserId().equals(user.getUserID())
						&& !userID.equals(user.getUserID())) {
					UserSuggestedContact userSuggestedContact = new UserSuggestedContact(
							new UserSuggestedContactKey(userID,
									user.getUserID()), user.getFullName(),
							user.getPhotoPath(), new Date(),
							"Matching : Mobile contact import");
					userSuggestedContact.setIsSuggestedViaImportMobile(true);
					userSuggestedContacts.add(userSuggestedContact);
					users.remove(user);
					break;
				}
			}
		}
		List<String> pendingMobContacts = new ArrayList<>();
		User user = userDao.findById(userID);
		for (UserSuggestedInvite con : inviteContacts) {
			pendingMobContacts.add(con.getMobileOrEmailId());
		}
		user.setMobileContactsPending(pendingMobContacts);
		userDao.update(user);
		userSuggestedContactDao.save(userSuggestedContacts);

		return new Suggestion("Success");

	}

	@Override
	public Profile friendProfile(UUID userID, UUID contactID) {
		Profile profile = new Profile();
		UserContact userContact = userContactDao.findByContactId(userID,
				contactID);
		if (userContact != null) {
			profile.setFriendStatus(userContact.getConnectionStatusID());
			if (userContact.getIsConnectedViaEmailDomain() != null)
				profile.setConnectedViaEmailDomain(userContact
						.getIsConnectedViaEmailDomain());
			if (userContact.getIsConnectedViaWhosup() != null)
				profile.setConnectedViaWhosup(userContact
						.getIsConnectedViaWhosup());
		} else {
			profile.setFriendStatus(3);
		}
		if (profile.getFriendStatus() != 3)
			profile.setUserContact(userContact);
		else {
			User user = userDao.findById(contactID);
			profile.setUserContact(new UserContact(new UserContactKey(userID,
					contactID), user.getFullName(), user.getPhotoPath()));

		}
		List<UUID> userEvents = userInterestedEventDao
				.findEventsByUserId(userID);
		List<UUID> contactEvents = userInterestedEventDao
				.findEventsByUserId(contactID);
		List<UUID> commonEvents = new ArrayList<UUID>();
		if (userEvents != null && contactEvents != null) {
			for (UUID u : userEvents) {
				if (contactEvents.contains(u)) {
					commonEvents.add(u);
				}
			}
		}
		List<Event> events = eventDao.findEventsById(commonEvents);
		List<UUID> userIds = userContactDao
				.findCommonFriends(userID, contactID);
		List<User> users = userDao.findUserByIds(userIds);
		Map<UUID, String> userName = new HashMap<UUID, String>();
		if (users != null) {
			for (User u : users) {
				userName.put(u.getUserID(), u.getFullName());
			}
		}
		List<CommonInterest> commons = new ArrayList<CommonInterest>();
		if (events != null) {
			for (Event event : events) {
				CommonInterest common = new CommonInterest(event.getTitle(),
						event.getSmallPhotoPath(), event.getEventDate());
				List<UUID> eventIds = event.getInterestedUserIDs();
				List<String> commonUsers = new ArrayList<String>();
				if (userIds != null && eventIds != null) {
					for (UUID e : eventIds) {
						if (userIds.contains(e)) {
							commonUsers.add(userName.get(e));
						}
					}
				}
				common.setCommonFriends(commonUsers);
				commons.add(common);
			}
		}
		profile.setCommomInterest(commons);
		return profile;
	}

	@Override
	public Status signOut(User user) {
		user.setDeviceID("");
		user.setDeviceType("");
		userDao.update(user);
		return new Status("Success");
	}

	protected String getRandomCode() {
		Long code = (long) ((Math.random() * 9000 + 1000));
		return code.toString();
	}

	@Override
	public MyCrowd selectCrowd(UUID userId, NetworkRequest request) {
		MyCrowd crowd = new MyCrowd();
		ArrayList<UserContact> sendUserContact = new ArrayList<UserContact>();
		if (request.getPageNo() > 0) {
			List<UserContact> userContacts = new ArrayList<UserContact>();
			List<UserContact> domainUserContacts = new ArrayList<UserContact>();
			userContacts = userContactDao.findContacts(userId);
			UserVerifiedDomain userdomain = userVerifiedDomainDao
					.findByUser(userId);
			List<UUID> sameDomainUserIDs = new ArrayList<UUID>();
			if (userdomain != null) {
				sameDomainUserIDs = userVerifiedDomainDao
						.findUsersByDomain(userdomain.getDomainName());
				if (sameDomainUserIDs.contains(userId)) {
					sameDomainUserIDs.remove(userId);
				}
			}
			for (UUID contactUserID : sameDomainUserIDs) {
				boolean isNotStarredUser = true;
				for (UserContact u : userContacts) {
					if (u.getPk().getContactUserID().equals(contactUserID)) {
						isNotStarredUser = false;
						break;
					}
				}
				if (isNotStarredUser) {
					User contactUser = userDao.findById(contactUserID);
					if (contactUser != null) {
						UserContact userContact = new UserContact();
						UserContactKey userContactkey = new UserContactKey();
						userContactkey.setUserID(contactUserID);
						userContactkey
								.setContactUserID(contactUser.getUserID());
						userContact.setPk(userContactkey);
						userContact.setContactFullName(contactUser
								.getFullName());
						userContact.setContactPhotoPath(contactUser
								.getPhotoPath());
						domainUserContacts.add(userContact);
					}
				}
			}
			System.out.println("\nuserContacts : " + userContacts.size()
					+ " domainUserContacts : " + domainUserContacts.size());
			System.out.println("\n" + userContacts.toString());
			System.out.println("\n" + domainUserContacts.toString());
			Integer totalUsers = userContacts.size()
					+ domainUserContacts.size();
			crowd.setTotalPage((int) Math.ceil((totalUsers * 1.0) / 9));
			if (totalUsers > ((request.getPageNo() - 1) * 9)) {
				if (userContacts.size() > ((request.getPageNo() - 1) * 9)) {
					sendUserContact
							.addAll(userContacts.subList(
									request.getPageNo() * 9 - 9,
									(userContacts.size() >= request.getPageNo() * 9) ? request
											.getPageNo() * 9 : userContacts
											.size()));
				}
				if (sendUserContact.size() < 9 && domainUserContacts.size() > 0) {
					if (sendUserContact.size() == 0
							&& userContacts.size() < ((request.getPageNo() - 1) * 9)) {
						Integer previouslySendNetworkContactCount = ((request
								.getPageNo() - 1) * 9) - userContacts.size();
						Integer remainingNetworkContactCount = (domainUserContacts
								.size() - previouslySendNetworkContactCount);
						sendUserContact
								.addAll(domainUserContacts
										.subList(
												previouslySendNetworkContactCount,
												previouslySendNetworkContactCount
														+ (remainingNetworkContactCount >= 9 ? 9
																: remainingNetworkContactCount)));
					} else {
						sendUserContact
								.addAll(domainUserContacts.subList(
										0,
										(domainUserContacts.size() >= (9 - sendUserContact
												.size())) ? (9 - sendUserContact
												.size()) : domainUserContacts
												.size()));
					}
				}
				crowd.setUserContacts(sendUserContact);
			}
		}

		return crowd;
	}

	@Override
	public Suggestion inviteContact(User user, String id) {
		if (id.contains("@"))
			commonService.sendMail(id, "Who's Up Invitation",
					"Hi,\n\t" + user.getFullName()
							+ " invited you to Who's Up ");

		UserSuggestedInvite invite = userSuggestedInviteDao
				.findByUserIdAndOtherId(user.getUserID(), id);
		invite.setCodeLastGenerated(UUID.randomUUID().toString());
		userSuggestedInviteDao.save(invite);
		return suggestion(user.getUserID());
	}

	@Override
	public void contactChangeAfterNewId(User user, String id) {
		List<UserSuggestedInvite> invites = userSuggestedInviteDao
				.findByOtherId(id);
		if (invites != null && invites.size() > 0) {
			List<UUID> contactIds = new ArrayList<UUID>();
			List<UUID> suggestIds = new ArrayList<UUID>();
			for (UserSuggestedInvite in : invites) {
				if (in.getCodeLastGenerated() != null
						&& !in.getCodeLastGenerated().isEmpty())
					contactIds.add(in.getPk().getUserID());
				else
					suggestIds.add(in.getPk().getUserID());
			}
			List<UserContact> alreadyUserContacts = userContactDao
					.findByContactIds(user.getUserID(), contactIds);
			List<UUID> otherContactIds = new ArrayList<UUID>();
			otherContactIds.addAll(contactIds);
			if (alreadyUserContacts != null) {
				for (UserContact con : alreadyUserContacts) {
					if (contactIds.contains(con.getContactUserID()))
						contactIds.remove(con.getContactUserID());
				}
			}
			List<UserContact> userContacts = new ArrayList<UserContact>();
			List<User> otherUsers = userDao.findUserByIds(contactIds);
			if (otherUsers != null) {
				for (User other : otherUsers) {
					UserContact userContact = new UserContact(
							new UserContactKey(user.getUserID(),
									other.getUserID()), other.getFullName(),
							other.getPhotoPath());
					userContact.setConnectionStatusID(0);
					userContact.setIsConnectedViaWhosup(true);
					userContact.setDateConnected(new Date());
					userContacts.add(userContact);
				}
				userContactDao.save(userContacts);
			}
			List<UserSuggestedContact> suggestedContacts = userSuggestedContactDao
					.findByContactIds(user.getUserID(), contactIds);
			if (suggestedContacts != null && suggestedContacts.size() > 0)
				userSuggestedContactDao.delete(suggestedContacts);

			alreadyUserContacts = userContactDao.findOtherContactsByUser(
					otherContactIds, user.getUserID());
			if (alreadyUserContacts != null) {
				for (UserContact con : alreadyUserContacts) {
					if (otherContactIds.contains(con.getPk().getUserID()))
						otherContactIds.remove(con.getPk().getUserID());
				}
			}
			userContacts = new ArrayList<UserContact>();
			if (otherContactIds != null && otherContactIds.size() > 0) {
				for (UUID other : otherContactIds) {
					UserContact userContact = new UserContact(
							new UserContactKey(other, user.getUserID()),
							user.getFullName(), user.getPhotoPath());
					userContact.setConnectionStatusID(0);
					userContact.setIsConnectedViaWhosup(true);
					userContact.setDateConnected(new Date());
					userContacts.add(userContact);
				}
				userContactDao.save(userContacts);
			}
			suggestedContacts = userSuggestedContactDao.findByOtherContactIds(
					otherContactIds, user.getUserID());
			if (suggestedContacts != null && suggestedContacts.size() > 0)
				userSuggestedContactDao.delete(suggestedContacts);

			userContacts = userContactDao.findOtherContactsByUser(suggestIds,
					user.getUserID());
			if (userContacts != null && userContacts.size() > 0) {
				for (UserContact u : userContacts) {
					if (suggestIds.contains(u.getContactUserID()))
						suggestIds.remove(u.getContactUserID());
				}
			}
			suggestedContacts = userSuggestedContactDao.findByOtherContactIds(
					suggestIds, user.getUserID());
			if (suggestedContacts != null) {
				for (UserSuggestedContact sug : suggestedContacts) {
					if (suggestIds.contains(sug.getPk().getUserID()))
						suggestIds.remove(sug.getPk().getUserID());
				}
			}

			suggestedContacts = new ArrayList<UserSuggestedContact>();
			if (suggestIds != null && suggestIds.size() > 0) {
				for (UUID sugUser : suggestIds) {
					UserSuggestedContact userSuggestedContact = new UserSuggestedContact(
							new UserSuggestedContactKey(sugUser,
									user.getUserID()), user.getFullName(),
							user.getPhotoPath(), new Date(),
							"Matching : Mobile contact import");
					userSuggestedContact.setIsSuggestedViaImportGmail(true);
					suggestedContacts.add(userSuggestedContact);
				}
				userSuggestedContactDao.save(suggestedContacts);
			}
			userSuggestedInviteDao.delete(invites);
		}
	}

	@Override
	public Status autoFriend(User user, UUID contactId) {
		if (user.getUserID().equals(contactId)) {
			throw new AppException(new AppErrorMessage("400",
					"You can not give friend request to you"));
		}
		if (userContactRequestDao.findByContactId(user.getUserID(), contactId) != null) {
			NetworkRequest request = new NetworkRequest();
			request.setAccessToken(user.getAccessToken());
			request.setContactId(contactId);
			request.setAccept(true);
			request.setShareInterest(false);
			acceptRequest(request);
			setFavourite(user.getUserID(), contactId, true);
			return new Status("Success");
		}
		userContactRequestDao.delete(userContactRequestDao.findByContactId(
				contactId, user.getUserID()));
		UserContact userContact = userContactDao.findByContactId(
				user.getUserID(), contactId);
		UserContact otherContact = userContactDao.findByContactId(contactId,
				user.getUserID());

		if (userContact != null
				&& userContact.getConnectionStatusID().intValue() == 0)
			throw new AppException(new AppErrorMessage("400",
					"You are already connected in whosup with this user"));
		else {
			if (userContact != null)
				userContactDao.delete(userContact);
			if (otherContact != null)
				userContactDao.delete(otherContact);
			User contactUser = userDao.findById(contactId);
			UserContactKey userContactKey = new UserContactKey(
					user.getUserID(), contactId);
			UserContact newContact = new UserContact(userContactKey,
					contactUser.getFullName(), contactUser.getPhotoPath());
			newContact.setDateRequested(new Date());
			newContact.setConnectionStatusID(0);
			newContact.setIsConnectedViaEmailDomain(true);
			newContact.setIsStarred(true);
			userContactDao.save(newContact);
		}
		UserSuggestedContact suggestContact = userSuggestedContactDao
				.findByContactId(user.getUserID(), contactId);
		if (suggestContact != null)
			userSuggestedContactDao.delete(suggestContact);
		return new Status("Success");
	}

	private List<UserSuggestedInvite> deleteUsersAlreadyFriend(UUID userId,
			List<UserSuggestedInvite> invite) {
		List<UUID> userIds = new ArrayList<UUID>();
		for (UserSuggestedInvite userSuggested : invite) {
			userIds.add(userSuggested.getPk().getUserID());
		}
		if (userIds.size() > 0) {
			List<UserContact> findByContactIds = userContactDao
					.findByContactIds(userId, userIds);
			List<UserSuggestedInvite> removeFriendsContact = new ArrayList<>();
			if (findByContactIds.size() > 0) {
				for (UserSuggestedInvite userContact : invite) {
					for (UserContact userCntct : findByContactIds) {
						if (userContact.getPk().getUserID()
								.equals(userCntct.getPk().getContactUserID())) {
							removeFriendsContact.add(userContact);
						}
					}
				}
				invite.removeAll(removeFriendsContact);
			}
		}
		return invite;
	}

	private List<UserSuggestedContact> deleteFriends(UUID userId,
			List<UserSuggestedContact> add) {
		List<UUID> userIds = new ArrayList<UUID>();
		for (UserSuggestedContact userSuggested : add) {
			userIds.add(userSuggested.getPk().getContactID());
		}
		if (userIds.size() > 0) {
			List<UserContact> findByContactIds = userContactDao
					.findByContactIds(userId, userIds);
			List<UserSuggestedContact> removeFriendsContact = new ArrayList<>();
			if (findByContactIds.size() > 0) {
				for (UserSuggestedContact userContact : add) {
					for (UserContact userCntct : findByContactIds) {
						if (userContact.getPk().getContactID().equals(userCntct.getPk().getContactUserID())) {
							removeFriendsContact.add(userContact);
						} else if(userId.equals(userContact.getPk().getContactID())) {
							removeFriendsContact.add(userContact);
						}
					}
				}
					add.removeAll(removeFriendsContact);
			}
		}
		return add;
	}

}
