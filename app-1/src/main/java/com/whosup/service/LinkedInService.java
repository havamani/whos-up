package com.whosup.service;

import java.util.UUID;

import org.json.JSONObject;

public interface LinkedInService {

	JSONObject linkedInDetails(String accessToken);

	UUID likedInLogin(UUID accessToken, String linkedInAccessToken);

}
