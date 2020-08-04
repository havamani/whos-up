package com.whosup.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

public class FacilityDaoImpl implements FacilityDao {

	@Autowired
	private CassandraOperations cassandraOperations;

}
