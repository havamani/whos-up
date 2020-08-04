package com.whosup.model;

import java.io.Serializable;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class RegionPopularSearchKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PrimaryKeyColumn(name = "country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String country;

	@PrimaryKeyColumn(name = "popular_search_term", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private String popularSearchTerm;

	public RegionPopularSearchKey() {
	}

	public RegionPopularSearchKey(String country, String popularSearchTerm) {
		this.country = country;
		this.popularSearchTerm = popularSearchTerm;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPopularSearchTerm() {
		return popularSearchTerm;
	}

	public void setPopularSearchTerm(String popularSearchTerm) {
		this.popularSearchTerm = popularSearchTerm;
	}
	
	

}
