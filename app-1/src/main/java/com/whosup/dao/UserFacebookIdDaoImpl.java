package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserFacebookId;

public class UserFacebookIdDaoImpl implements UserFacebookIdDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public UUID findUserByFbId(String fbId) {
		if (fbId == null)
			return null;
		Select select = QueryBuilder.select().from("user_facebook_id");
		select.where(QueryBuilder.eq("facebook_id", fbId));
		UserFacebookId userFbMap = cassandraOperations.selectOne(select,
				UserFacebookId.class);
		if (userFbMap == null)
			return null;
		else
			return userFbMap.getUserID();
	}

	@Override
	public List<UUID> findUsersByFbIds(List<String> fbIds) {
		if (fbIds == null || fbIds.isEmpty())
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_facebook_id");
		select.where(QueryBuilder.in("facebook_id", fbIds.toArray()));
		List<UserFacebookId> userFacebookMap = cassandraOperations.select(
				select, UserFacebookId.class);
		List<UUID> userIDs = new ArrayList<UUID>();
		for (UserFacebookId u : userFacebookMap) {
			userIDs.add(u.getUserID());
		}
		return userIDs;
	}

	@Override
	public void save(UserFacebookId userFbMap) {
		if (userFbMap != null)
			cassandraOperations.insert(userFbMap);
	}
}
