package com.whosup.dao;

import java.util.UUID;

import com.whosup.model.UserTransactions;

public interface UserTransactionsDao {

	void save(UserTransactions userAccountTransactions);

	UserTransactions findById(UUID id);

	void update(UserTransactions userAccountTransactions);

}
