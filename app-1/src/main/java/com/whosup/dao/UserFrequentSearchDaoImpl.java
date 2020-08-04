package com.whosup.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserFrequentSearch;
import com.whosup.model.UserFrequentSearchKey;

public class UserFrequentSearchDaoImpl implements UserFrequentSearchDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserFrequentSearch frequentSearch) {
		if (frequentSearch != null)
			cassandraOperations.insert(frequentSearch);
	}

	@Override
	public void update(UserFrequentSearch frequentSearch) {
		if (frequentSearch != null)
			cassandraOperations.update(frequentSearch);
	}

	@Override
	public void saveOrUpdateSearchTerm(UUID userId, String searchTerm) {
		Select select = QueryBuilder.select().from("user_frequent_search");
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("search_term", searchTerm));
		UserFrequentSearch userFrequentSearch = cassandraOperations.selectOne(
				select, UserFrequentSearch.class);
		if (userFrequentSearch != null) {
			userFrequentSearch.setCnt(userFrequentSearch.getCnt() + 1);
			userFrequentSearch.setDateLastSearched(new Date());
			update(userFrequentSearch);
		} else {
			UserFrequentSearch search = new UserFrequentSearch(
					new UserFrequentSearchKey(userId, searchTerm));
			search.setCnt(1);
			search.setDateLastSearched(new Date());
			save(search);
		}
	}

	@Override
	public List<UserFrequentSearch> findSearchTermByUserId(UUID userId) {
		Select select = QueryBuilder.select().from("user_frequent_search");
		select.where(QueryBuilder.eq("user_id", userId));
		List<UserFrequentSearch> searchTerm = cassandraOperations.select(
				select, UserFrequentSearch.class);
		return searchTerm;
	}

}
