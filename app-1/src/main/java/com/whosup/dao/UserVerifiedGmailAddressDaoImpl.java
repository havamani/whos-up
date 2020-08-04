package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserVerifiedGmailAddress;

public class UserVerifiedGmailAddressDaoImpl implements
		UserVerifiedGmailAddressDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserVerifiedGmailAddress gmail) {
		if (gmail != null)
			cassandraOperations.insert(gmail);
	}

	@Override
	public List<UserVerifiedGmailAddress> findUsersByGmails(List<String> gmails) {
		if (gmails == null || gmails.isEmpty())
			return new ArrayList<UserVerifiedGmailAddress>();
		Select select = QueryBuilder.select().from(
				"user_verified_gmail_address");
		select.where(QueryBuilder.in("gmail_address", gmails.toArray()));
		List<UserVerifiedGmailAddress> gmailUsers = cassandraOperations.select(
				select, UserVerifiedGmailAddress.class);
		return gmailUsers;
	}

	@Override
	public UserVerifiedGmailAddress findUsersByGmail(UUID userId, String gmail) {
		if (gmail == null || gmail.isEmpty() || userId == null)
			return null;
		Select select = QueryBuilder.select().from(
				"user_verified_gmail_address");
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("gmail_address", gmail));
		UserVerifiedGmailAddress gmailUser = cassandraOperations.selectOne(
				select, UserVerifiedGmailAddress.class);
		return gmailUser;
	}
}
