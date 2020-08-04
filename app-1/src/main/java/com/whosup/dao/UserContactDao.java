package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserContact;

public interface UserContactDao {

	UserContact findByContactId(UUID userId, UUID contactId);

	List<UserContact> findContacts(UUID userId);

	List<UserContact> findContactsByFb(UUID userId);

	void save(List<UserContact> contacts);

	void save(UserContact contact);

	Long findContactCount(UUID userId);

	Long findRequestCount(UUID userId);

	List<UserContact> findContactsByLn(UUID userId);

	List<UserContact> findContactsByWhosUp(UUID userId);

	void update(UserContact contact);

	List<UserContact> findContactsByNetwork(UUID userId, List<UUID> userIDs);

	List<UUID> findCommonFriends(UUID userId, UUID contactID);

	List<UserContact> findMyCrowd(UUID userId);

	List<UserContact> findContactsByGatherFrom(UUID userId,
			List<String> gatherFrom);

	List<UserContact> findOtherContactsByUser(List<UUID> userIds,
			UUID contactUserId);

	List<UserContact> findByContactIds(UUID userId, List<UUID> contactIds);

	List<UUID> findEventSharedUserIds(UUID userId, List<UUID> contactIds);

	Long findFacebookCount(UUID userId);

	List<UserContact> findOtherContacts(UUID userId);

	void update(List<UserContact> contact);

	Long findFacebookStarredCount(UUID userId);

	void delete(UserContact contact);

	List<UserContact> findContactsByEmailDomain(UUID userId);

}
