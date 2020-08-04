package com.whosup.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.RegionPopularSearch;
import com.whosup.model.RegionPopularSearchKey;

public class RegionPopularSearchDaoImpl implements RegionPopularSearchDao{

	@Autowired
	private CassandraOperations cassandraOperations;
	
	@Override
	public void save(RegionPopularSearch regionPopularSearch) {
		if (regionPopularSearch != null)
			cassandraOperations.insert(regionPopularSearch);

	}

	@Override
	public void update(RegionPopularSearch regionPopularSearch) {
		if (regionPopularSearch != null)
			cassandraOperations.update(regionPopularSearch);
		
	}
	
	@Override
	public void saveOrUpdatePopularSearch(String country, String searchTerm) {
		Select select = QueryBuilder.select().from("region_popular_search");
		select.where(QueryBuilder.eq("country", country)).and(
				QueryBuilder.eq("popular_search_term", searchTerm ));
		RegionPopularSearch regionPopularSearch = cassandraOperations.selectOne(
				select, RegionPopularSearch.class);
		if (regionPopularSearch != null) {
			regionPopularSearch.setRank(1);
			update(regionPopularSearch);
		} else {
			RegionPopularSearch search = new RegionPopularSearch(
					new RegionPopularSearchKey(country, searchTerm));
				search.setRank(null);
				save(search);
		}
		
	}

	@Override
	public List<RegionPopularSearch> findAllPopularSearch(String country) {
		Select select = QueryBuilder.select().from("region_popular_search");
		select.where(QueryBuilder.eq("country", country));
		System.out.println("Check Select Query:"+select.toString());
		List<RegionPopularSearch> regionPopularSearches = cassandraOperations.select(select, RegionPopularSearch.class);
		return regionPopularSearches;
	}

}
