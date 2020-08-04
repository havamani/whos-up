package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.IOwe;
import com.whosup.model.OwesMe;

public class OwesMeDaoImpl implements OwesMeDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(OwesMe owesMe) {
		if (owesMe != null)
			cassandraOperations.insert(owesMe);
	}

	@Override
	public void save(List<OwesMe> owesMe) {
		if (owesMe != null && !owesMe.isEmpty())
			cassandraOperations.insert(owesMe);
	}

	@Override
	public void update(OwesMe owesMe) {
		if (owesMe != null)
			cassandraOperations.update(owesMe);
	}

	@Override
	public void delete(List<OwesMe> owesMe) {
		if (owesMe != null && !owesMe.isEmpty())
			cassandraOperations.delete(owesMe);
	}

	@Override
	public void delete(OwesMe owesMe) {
		if (owesMe != null)
			cassandraOperations.delete(owesMe);
	}

	@Override
	public OwesMe selectByEventAndUserAndOrganizer(UUID organizerId,
			UUID eventId, UUID owingUserId) {
		if (organizerId == null || eventId == null || owingUserId == null)
			return null;

		Select select = QueryBuilder.select().from("owes_me");
		select.where(QueryBuilder.eq("user_id", organizerId))
				.and(QueryBuilder.eq("event_id", eventId))
				.and(QueryBuilder.eq("owing_user_id", owingUserId));
		OwesMe payment = cassandraOperations.selectOne(select, OwesMe.class);
		return payment;
	}

	@Override
	public OwesMe selectByEventAndUserAndOrganizer(IOwe owe) {
		if (owe != null) {
			return selectByEventAndUserAndOrganizer(owe.getOrganizerId(), owe
					.getPk().getEventID(), owe.getPk().getUserID());
		} else
			return null;
	}

	@Override
	public OwesMe selectByEventIdAndUserID(UUID eventId, UUID owingUserId) {
		if (eventId == null || owingUserId == null)
			return null;

		Select select = QueryBuilder.select().from("owes_me").allowFiltering();
		select.where(QueryBuilder.eq("event_id", eventId)).and(
				QueryBuilder.eq("owing_user_id", owingUserId));
		OwesMe payment = cassandraOperations.selectOne(select, OwesMe.class);
		return payment;
	}

	@Override
	public List<OwesMe> selectPaymentOfEvent(UUID eventId) {
		if (eventId == null)
			return new ArrayList<OwesMe>();
		Select select = QueryBuilder.select().from("owes_me").allowFiltering();
		select.where(QueryBuilder.eq("event_id", eventId));
		List<OwesMe> payments = cassandraOperations
				.select(select, OwesMe.class);
		if (payments != null)
			return payments;
		else
			return new ArrayList<OwesMe>();
	}

	@Override
	public List<OwesMe> selectOwesMe(UUID userId) {
		if (userId == null)
			return new ArrayList<OwesMe>();
		Select select = QueryBuilder.select().from("owes_me");
		select.where(QueryBuilder.eq("user_id", userId));
		List<OwesMe> payments = cassandraOperations
				.select(select, OwesMe.class);
		if (payments != null)
			return payments;
		else
			return new ArrayList<OwesMe>();
	}

}
