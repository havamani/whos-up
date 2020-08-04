package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserFacebookId;

public interface UserFacebookIdDao {

	List<UUID> findUsersByFbIds(List<String> fbIds);

	void save(UserFacebookId userFbMap);

	UUID findUserByFbId(String fbId);

}
