package com.whosup.model;

import java.util.Date;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(value = "user_frequent_search")
public class UserFrequentSearch {

	@PrimaryKey
	@JsonIgnore
	private UserFrequentSearchKey pk;

	@Column(value = "date_last_searched")
	private Date dateLastSearched;

	@Column(value = "cnt")
	private Integer cnt;

	public UserFrequentSearch() {
	}

	public UserFrequentSearch(UserFrequentSearchKey pk) {
		this.pk = pk;
	}

	public void setPk(UserFrequentSearchKey pk) {
		this.pk = pk;
	}

	public Date getDateLastSearched() {
		return dateLastSearched;
	}

	public void setDateLastSearched(Date dateLastSearched) {
		this.dateLastSearched = dateLastSearched;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public UserFrequentSearchKey getPk() {
		return pk;
	}



}
