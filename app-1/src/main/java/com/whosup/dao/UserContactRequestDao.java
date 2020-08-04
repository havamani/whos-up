package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserContactRequest;

public interface UserContactRequestDao {

	List<UserContactRequest> findByUserId(UUID userId);

	void save(UserContactRequest contact);

	UserContactRequest findByContactId(UUID userId, UUID contactId);

	void delete(UserContactRequest contact);

}
