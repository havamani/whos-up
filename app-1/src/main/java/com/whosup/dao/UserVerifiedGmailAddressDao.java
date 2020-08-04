package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserVerifiedGmailAddress;

public interface UserVerifiedGmailAddressDao {

	List<UserVerifiedGmailAddress> findUsersByGmails(List<String> gmails);

	void save(UserVerifiedGmailAddress gmail);

	UserVerifiedGmailAddress findUsersByGmail(UUID userId, String gmail);

}
