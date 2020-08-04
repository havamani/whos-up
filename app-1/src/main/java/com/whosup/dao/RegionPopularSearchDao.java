package com.whosup.dao;

import java.util.List;

import com.whosup.model.RegionPopularSearch;

public interface RegionPopularSearchDao {

	void save(RegionPopularSearch regionPopularSearch);
	
	void update(RegionPopularSearch regionPopularSearch);
	
	void saveOrUpdatePopularSearch(String country, String search);
	
	List<RegionPopularSearch> findAllPopularSearch(String country);

}
