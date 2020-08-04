package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserSuggestedContact;

public interface UserSuggestedContactDao {

	void save(UserSuggestedContact contact);

	void delete(UserSuggestedContact contact);

	void save(List<UserSuggestedContact> contact);

	List<UserSuggestedContact> findByUserId(UUID userId);

	UserSuggestedContact findByContactId(UUID userId, UUID contactId);

	List<UserSuggestedContact> findByOtherContactIds(List<UUID> userIds,
			UUID contactId);

	List<UserSuggestedContact> findByContactIds(UUID userId,
			List<UUID> contactIds);

	void delete(List<UserSuggestedContact> contact);

}
