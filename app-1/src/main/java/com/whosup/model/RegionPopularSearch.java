package com.whosup.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(value = "region_popular_search")
public class RegionPopularSearch {
	
	@PrimaryKey
	@JsonIgnore
	private RegionPopularSearchKey pk;
	
	@Column(value = "rank")
	private Integer rank;

	public RegionPopularSearch() {
	}
	
	public RegionPopularSearch(RegionPopularSearchKey pk) {
		this.pk = pk;
	}


	public RegionPopularSearchKey getPk() {
		return pk;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public void setPk(RegionPopularSearchKey pk) {
		this.pk = pk;
	}
	
	
	
}
