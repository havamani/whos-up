package com.whosup.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserAccessToken;

public class UserAccessTokenDaoImpl implements UserAccessTokenDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserAccessToken user) {
		cassandraOperations.insert(user);
	}

	@Override
	public void update(UserAccessToken user) {
		cassandraOperations.update(user);
	}

	@Override
	public UserAccessToken findByUserId(UUID userId) {
		return cassandraOperations.selectOneById(UserAccessToken.class, userId);
	}

	@Override
	public UUID findUserIdByAccessToken(UUID accessToken) {
		if (accessToken != null) {
			Select select = QueryBuilder.select().from("user_access_token");
			select.where(QueryBuilder.eq("access_token", accessToken));
			UserAccessToken token = cassandraOperations.selectOne(select,
					UserAccessToken.class);
			if (token == null)
				return null;
			return token.getUserId();
		} else
			return null;
	}
}
