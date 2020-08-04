package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserSuggestedInvite;

public class UserSuggestedInviteDaoImpl implements UserSuggestedInviteDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserSuggestedInvite contact) {
		if (contact != null)
			cassandraOperations.insert(contact);
	}

	@Override
	public void save(List<UserSuggestedInvite> contacts) {
		if (contacts != null && !contacts.isEmpty())
			cassandraOperations.insert(contacts);
	}

	@Override
	public void delete(List<UserSuggestedInvite> contacts) {
		if (contacts != null && !contacts.isEmpty())
			cassandraOperations.delete(contacts);
	}
	
	@Override
	public void delete(UserSuggestedInvite contact) {
		if (contact != null)
			cassandraOperations.delete(contact);
	}

	@Override
	public List<UserSuggestedInvite> findByUserId(UUID userId) {
		if (userId == null)
			return new ArrayList<UserSuggestedInvite>();
		Select select = QueryBuilder.select().from("user_suggested_invite");
		select.where(QueryBuilder.eq("user_id", userId));
		List<UserSuggestedInvite> suggestedContacts = cassandraOperations
				.select(select, UserSuggestedInvite.class);
		return suggestedContacts;
	}

	@Override
	public List<UserSuggestedInvite> findByOtherId(String id) {
		if (id == null)
			return new ArrayList<UserSuggestedInvite>();
		Select select = QueryBuilder.select().from("user_suggested_invite")
				.allowFiltering();
		select.where(QueryBuilder.eq("mobile_number_or_email_address", id));
		List<UserSuggestedInvite> suggestedContacts = cassandraOperations
				.select(select, UserSuggestedInvite.class);
		return suggestedContacts;
	}

	@Override
	public UserSuggestedInvite findByUserIdAndOtherId(UUID userId, String id) {
		if (userId == null || id == null)
			return null;
		Select select = QueryBuilder.select().from("user_suggested_invite");
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("mobile_number_or_email_address", id));
		UserSuggestedInvite suggestedContacts = cassandraOperations.selectOne(
				select, UserSuggestedInvite.class);
		return suggestedContacts;
	}
}
