package com.whosup.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.whosup.dao.UserContactDao;
import com.whosup.dao.UserDao;
import com.whosup.dao.UserFacebookIdDao;
import com.whosup.dao.UserVerifiedGmailAddressDao;
import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.UserContactKey;
import com.whosup.model.UserFacebookId;
import com.whosup.model.UserVerifiedGmailAddress;
import com.whosup.model.UserVerifiedGmailAddressKey;
import com.whosup.model.response.LoginResponse;

public class FacebookServiceImpl implements FacebookService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserContactDao userContactDao;
	@Autowired
	private UserFacebookIdDao userFacebookIdDao;
	@Autowired
	private UserVerifiedGmailAddressDao userVerifiedGmailAddressDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private NetworkService networkService;

	@Override
	public LoginResponse facebookLogin(String fb_accessToken, String deviceID,
			String deviceType) {

		User user;

		JSONObject data = facebookDetails(fb_accessToken);
		User oldUser = userDao.findById(userFacebookIdDao.findUserByFbId(data
				.getString("id")));
		JSONObject photo = facebookPhoto(data.getString("id"));

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 60);

		if (oldUser != null
				&& oldUser.getFacebookID().equals(data.getString("id"))) {
			user = userDao.findById(oldUser.getUserID());
		} else {
			user = new User(data.getString("id"), data.optString("name"),
					data.optString("gender"));
			user.setFacebookEmail(data.optString("email"));
			user.setDateSignedUp(new Date());
		}
		user.setFacebookAccessToken(fb_accessToken);
		user.setAccessToken(User.getUUID());
		user.setAccessTokenExpireDate(cal.getTime());
		user.setDeviceID(deviceID);
		user.setDeviceType(deviceType);
		if (photo.optJSONObject("data").optString("url") == null)
			user.setFacebookPhotoPath(null);
		else
			user.setFacebookPhotoPath(commonService
					.uploadImage(facebookImage(photo.optJSONObject("data")
							.optString("url")), null));
		user.setFacebookFirstName(data.optString("first_name"));
		user.setFacebookLastName(data.optString("last_name"));
		user.setLayerAuthenticationToken(UUID.randomUUID());
		if (user.getFacebookPhotoPath() != null && !user.getFacebookPhotoPath().isEmpty())
			user.setPhotoPath(user.getFacebookPhotoPath());
		if (oldUser != null)
			userDao.update(user);
		else
			userDao.save(user);

		if (userFacebookIdDao.findUserByFbId(user.getFacebookID()) == null) {
			UserFacebookId userFbMap = new UserFacebookId(user.getFacebookID(),
					user.getUserID());
			userFacebookIdDao.save(userFbMap);
			networkService.verifyEmail(user, "", "WITHOUTDOMAIN");
		}
		String email = data.optString("email");
		if (email != null
				&& !email.isEmpty()
				&& userVerifiedGmailAddressDao.findUsersByGmail(
						user.getUserID(), email) == null) {
			UserVerifiedGmailAddress gmail = new UserVerifiedGmailAddress(
					new UserVerifiedGmailAddressKey(user.getUserID(),
							data.optString("email")));
			userVerifiedGmailAddressDao.save(gmail);
		}
		if (email != null && !email.isEmpty())
			networkService.contactChangeAfterNewId(user, email);
		saveFacebookFriends(user);
		return new LoginResponse(user.getAccessToken());
	}

	protected InputStream facebookImage(String url) {

		InputStream br = null;
		try {
			URL requestMethod = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) requestMethod
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				br = urlConnection.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return br;
	}

	protected JSONObject facebookDetails(String accessToken) {

		JSONObject json = new JSONObject();
		String url = "https://graph.facebook.com/me?access_token="
				+ accessToken
				+ "&fields=id,first_name,last_name,name,gender,email,age_range";
		try {
			URL requestMethod = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) requestMethod
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				json = new JSONObject(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	protected JSONObject facebookPhoto(String accessToken) {

		JSONObject json = new JSONObject();
		String url = "https://graph.facebook.com/v2.4/" + accessToken
				+ "/picture?format=json&redirect=false&type=large";
		try {
			URL requestMethod = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) requestMethod
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				json = new JSONObject(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public void saveFacebookFriends(User user) {
		List<String> fbIds = facebookFriends(user.getFacebookAccessToken());
		List<UUID> userIDs = userFacebookIdDao.findUsersByFbIds(fbIds);
		List<UserContact> userContacts = userContactDao.findByContactIds(
				user.getUserID(), userIDs);
		List<UUID> noContactUserIds = new ArrayList<UUID>();
		noContactUserIds.addAll(userIDs);
		for (UserContact con : userContacts)
			noContactUserIds.remove(con.getContactUserID());
		List<User> usersList = userDao.findUserByIds(noContactUserIds);
		if (usersList != null) {
			userContacts = new ArrayList<UserContact>();
			for (User u : usersList) {
				UserContact userContact = new UserContact(new UserContactKey(
						user.getUserID(), u.getUserID()), u.getFullName(),
						u.getPhotoPath());
				userContact.setConnectionStatusID(0);
				userContact.setIsConnectedViaFB(true);
				userContact.setDateConnected(new Date());
				userContacts.add(userContact);
			}
			if (userContacts.size() > 0)
				userContactDao.save(userContacts);
		}

		userContacts = userContactDao.findOtherContactsByUser(userIDs,
				user.getUserID());
		for (UserContact con : userContacts)
			userIDs.remove(con.getPk().getUserID());
		usersList = userDao.findUserByIds(userIDs);
		if (usersList != null) {
			userContacts = new ArrayList<UserContact>();
			for (User u : usersList) {
				UserContact userContactOther = new UserContact(
						new UserContactKey(u.getUserID(), user.getUserID()),
						user.getFullName(), user.getPhotoPath());
				userContactOther.setConnectionStatusID(0);
				userContactOther.setIsConnectedViaFB(true);
				userContactOther.setDateConnected(new Date());
				userContacts.add(userContactOther);
			}
			if (userContacts.size() > 0)
				userContactDao.save(userContacts);
		}
	}

	protected List<String> facebookFriends(String accessToken) {

		List<String> ids = new ArrayList<String>();
		String url = "https://graph.facebook.com/me/friends?access_token="
				+ accessToken;
		try {
			URL requestMethod = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) requestMethod
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				JSONObject json = new JSONObject(sb.toString());
				JSONArray data = json.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					JSONObject user = data.getJSONObject(i);
					ids.add(user.getString("id"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}
}