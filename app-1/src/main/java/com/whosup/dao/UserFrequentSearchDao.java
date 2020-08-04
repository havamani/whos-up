package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserFrequentSearch;

public interface UserFrequentSearchDao {
	
	void save(UserFrequentSearch frequentSearch);
	
	void update(UserFrequentSearch frequentSearch);
	
	void saveOrUpdateSearchTerm(UUID userId, String searchTerm);
	
	List<UserFrequentSearch> findSearchTermByUserId(UUID userID);
}
