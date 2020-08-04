package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserVerifiedMobileNumber;

public interface UserVerifiedMobileNumberDao {

	List<UserVerifiedMobileNumber> findUsersByMobiles(List<String> mobiles);

	void save(UserVerifiedMobileNumber mobile);

	void delete(UserVerifiedMobileNumber mobile);

	UUID findUserByMobileNumber(String mobile);

}
