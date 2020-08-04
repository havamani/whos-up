package com.whosup.dao;

import java.util.UUID;

import com.whosup.model.UserAccessToken;

public interface UserAccessTokenDao {

	void save(UserAccessToken user);

	void update(UserAccessToken user);

	UserAccessToken findByUserId(UUID userId);

	UUID findUserIdByAccessToken(UUID accessToken);

}
