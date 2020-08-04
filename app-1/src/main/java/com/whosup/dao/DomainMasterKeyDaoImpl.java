package com.whosup.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.whosup.model.DomainMasterKey;

public class DomainMasterKeyDaoImpl implements DomainMasterKeyDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public DomainMasterKey selectDomain(String domain) {
		if (domain == null || domain.isEmpty())
			return null;
		DomainMasterKey domainKey = cassandraOperations.selectOneById(
				DomainMasterKey.class, domain);
		return domainKey;
	}
}
