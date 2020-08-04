package com.whosup.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.whosup.model.UserLocationLog;

public class UserLocationLogDaoImpl implements UserLocationLogDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserLocationLog locationLog) {
		if (locationLog != null)
			cassandraOperations.insert(locationLog);
	}
}
