package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserSuggestedInvite;

public interface UserSuggestedInviteDao {

	void save(UserSuggestedInvite contact);

	void save(List<UserSuggestedInvite> contacts);

	void delete(List<UserSuggestedInvite> contacts);

	List<UserSuggestedInvite> findByUserId(UUID userId);

	UserSuggestedInvite findByUserIdAndOtherId(UUID userId, String id);

	List<UserSuggestedInvite> findByOtherId(String id);

	void delete(UserSuggestedInvite findByUserIdAndOtherId);

}
