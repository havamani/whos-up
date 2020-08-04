package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserContactRequest;

public class UserContactRequestDaoImpl implements UserContactRequestDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserContactRequest contact) {
		if (contact != null)
			cassandraOperations.insert(contact);
	}

	@Override
	public void delete(UserContactRequest contact) {
		if (contact != null)
			cassandraOperations.delete(contact);
	}

	@Override
	public List<UserContactRequest> findByUserId(UUID userId) {
		if (userId == null)
			return new ArrayList<UserContactRequest>();
		Select select = QueryBuilder.select().from("user_contact_request");
		select.where(QueryBuilder.eq("user_id", userId));
		List<UserContactRequest> contactRequests = cassandraOperations.select(
				select, UserContactRequest.class);
		return contactRequests;
	}

	@Override
	public UserContactRequest findByContactId(UUID userId, UUID contactId) {
		if (userId == null || contactId == null)
			return null;
		Select select = QueryBuilder.select().from("user_contact_request")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("contact_id", contactId));
		UserContactRequest contactRequest = cassandraOperations.selectOne(
				select, UserContactRequest.class);
		return contactRequest;
	}
}
