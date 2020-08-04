package com.whosup.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.whosup.model.UserTransactions;

public class UserTransactionsDaoImpl implements UserTransactionsDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserTransactions userAccountTransactions) {
		if (userAccountTransactions != null)
			cassandraOperations.insert(userAccountTransactions);
	}

	@Override
	public void update(UserTransactions userAccountTransactions) {
		if (userAccountTransactions != null)
			cassandraOperations.update(userAccountTransactions);
	}

	@Override
	public UserTransactions findById(UUID id) {
		if (id == null)
			return null;
		else
			return cassandraOperations
					.selectOneById(UserTransactions.class, id);
	}

}
