package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.IOwe;

public class IOweDaoImpl implements IOweDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(IOwe iOwe) {
		if (iOwe != null)
			cassandraOperations.insert(iOwe);
	}

	@Override
	public void update(IOwe iOwe) {
		if (iOwe != null)
			cassandraOperations.update(iOwe);
	}

	@Override
	public void save(List<IOwe> iOwe) {
		if (iOwe != null && !iOwe.isEmpty())
			cassandraOperations.insert(iOwe);
	}

	@Override
	public IOwe findByUserIdAndEventId(UUID userId, UUID eventId) {
		if (userId == null || eventId == null)
			return null;
		Select select = QueryBuilder.select().from("i_owe");
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("event_id", eventId));
		IOwe owe = cassandraOperations.selectOne(select, IOwe.class);
		return owe;
	}

	@Override
	public List<IOwe> findByUserId(UUID userId) {
		if (userId == null)
			return new ArrayList<IOwe>();
		Select select = QueryBuilder.select().from("i_owe").allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId));
		List<IOwe> owes = cassandraOperations.select(select, IOwe.class);
		if (owes != null) {
			List<IOwe> filteredOwes = new ArrayList<IOwe>();
			for (IOwe i : owes) {
				if (i.getIsCancelled() || i.getIsPaidByCash()
						|| i.getIsPaidElectronically())
					continue;
				filteredOwes.add(i);
			}
			return filteredOwes;
		}
		return new ArrayList<IOwe>();
	}

	@Override
	public void delete(UUID eventId) {
		if (eventId != null) {
			Select select = QueryBuilder.select().from("i_owe")
					.allowFiltering();
			select.where(QueryBuilder.eq("event_id", eventId));
			List<IOwe> owes = cassandraOperations.select(select, IOwe.class);
			if (owes != null && owes.size() > 0)
				cassandraOperations.delete(owes);
		}
	}

	@Override
	public void delete(UUID userId, UUID eventId) {
		if (userId != null || eventId != null) {
			Select select = QueryBuilder.select().from("i_owe")
					.allowFiltering();
			select.where(QueryBuilder.eq("user_id", userId)).and(
					QueryBuilder.eq("event_id", eventId));
			IOwe owe = cassandraOperations.selectOne(select, IOwe.class);
			owe.setIsCancelled(true);
			cassandraOperations.update(owe);
		}
	}
}
