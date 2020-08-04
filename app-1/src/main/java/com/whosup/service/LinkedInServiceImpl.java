package com.whosup.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.whosup.dao.UserAccessTokenDao;
import com.whosup.dao.UserDao;
import com.whosup.dao.UserVerifiedGmailAddressDao;
import com.whosup.model.User;
import com.whosup.model.UserVerifiedGmailAddress;
import com.whosup.model.UserVerifiedGmailAddressKey;

public class LinkedInServiceImpl implements LinkedInService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAccessTokenDao userAccessTokenDao;
	@Autowired
	private UserVerifiedGmailAddressDao userVerifiedGmailAddressDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private NetworkService networkService;

	@Override
	public UUID likedInLogin(UUID accessToken, String linkedInAccessToken) {

		User user = userDao.findById(userAccessTokenDao
				.findUserIdByAccessToken(accessToken));
		JSONObject data = linkedInDetails(linkedInAccessToken);

		user.setLinkedInID(data.getString("id"));
		user.setLinkedInEmail(data.getString("emailAddress"));
		user.setLinkedInAccessToken(linkedInAccessToken);
		userDao.update(user);

		String email = data.optString("emailAddress");
		if (email != null
				&& !email.isEmpty()
				&& userVerifiedGmailAddressDao.findUsersByGmail(
						user.getUserID(), email) == null) {
			UserVerifiedGmailAddress gmail = new UserVerifiedGmailAddress(
					new UserVerifiedGmailAddressKey(user.getUserID(),
							data.optString("emailAddress")));
			userVerifiedGmailAddressDao.save(gmail);
		}
		if (email != null && !email.isEmpty())
			networkService.contactChangeAfterNewId(user, email);
		return user.getAccessToken();
	}

	@Override
	public JSONObject linkedInDetails(String accessToken) {

		JSONObject json = new JSONObject();
		String newUrl = "https://api.linkedin.com/v1/people/~:(id,firstName,lastName,email-address,picture-url)?format=json";
		try {
			URL requestMethod = new URL(newUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) requestMethod
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Authorization", "Bearer "
					+ accessToken);
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
}
