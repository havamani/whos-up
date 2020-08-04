package com.whosup.service;

import com.whosup.model.User;
import com.whosup.model.response.LoginResponse;

public interface FacebookService {

	LoginResponse facebookLogin(String fb_accessToken, String deviceID,
			String deviceType);

	void saveFacebookFriends(User user);

}
