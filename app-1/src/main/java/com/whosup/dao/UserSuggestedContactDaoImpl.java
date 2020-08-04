package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserSuggestedContact;

public class UserSuggestedContactDaoImpl implements UserSuggestedContactDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserSuggestedContact contact) {
		if (contact != null)
			cassandraOperations.insert(contact);
	}

	@Override
	public void save(List<UserSuggestedContact> contact) {
		if (contact != null && !contact.isEmpty())
			cassandraOperations.insert(contact);
	}

	@Override
	public void delete(UserSuggestedContact contact) {
		if (contact != null)
			cassandraOperations.delete(contact);
	}

	@Override
	public void delete(List<UserSuggestedContact> contact) {
		if (contact != null && !contact.isEmpty())
			cassandraOperations.delete(contact);
	}

	@Override
	public List<UserSuggestedContact> findByUserId(UUID userId) {
		if (userId == null)
			return new ArrayList<UserSuggestedContact>();
		Select select = QueryBuilder.select().from("user_suggested_contact");
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("is_request_sent", false));
		List<UserSuggestedContact> suggestedContacts = cassandraOperations
				.select(select, UserSuggestedContact.class);
		return suggestedContacts;
	}

	@Override
	public UserSuggestedContact findByContactId(UUID userId, UUID contactId) {
		if (userId == null || contactId == null)
			return null;
		Select select = QueryBuilder.select().from("user_suggested_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("contact_id", contactId));
		UserSuggestedContact suggestContact = cassandraOperations.selectOne(
				select, UserSuggestedContact.class);
		return suggestContact;
	}

	@Override
	public List<UserSuggestedContact> findByContactIds(UUID userId,
			List<UUID> contactIds) {
		if (userId == null || contactIds == null || contactIds.size() == 0)
			return new ArrayList<UserSuggestedContact>();
		Select select = QueryBuilder.select().from("user_suggested_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.in("contact_id", contactIds.toArray()));
		List<UserSuggestedContact> suggestContacts = cassandraOperations
				.select(select, UserSuggestedContact.class);
		return suggestContacts;
	}

	@Override
	public List<UserSuggestedContact> findByOtherContactIds(List<UUID> userIds,
			UUID contactId) {
		if (contactId == null || userIds == null || userIds.size() == 0)
			return new ArrayList<UserSuggestedContact>();
		Select select = QueryBuilder.select().from("user_suggested_contact")
				.allowFiltering();
		select.where(QueryBuilder.in("user_id", userIds.toArray())).and(
				QueryBuilder.eq("contact_id", contactId));
		List<UserSuggestedContact> suggestContacts = cassandraOperations
				.select(select, UserSuggestedContact.class);
		return suggestContacts;
	}
}
