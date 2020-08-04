package com.whosup.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserVerifiedMobileNumber;

public class UserVerifiedMobileNumberDaoImpl implements
		UserVerifiedMobileNumberDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserVerifiedMobileNumber mobile) {
		if (mobile != null)
			cassandraOperations.insert(mobile);
	}

	@Override
	public void delete(UserVerifiedMobileNumber mobile) {
		if (mobile != null)
			cassandraOperations.delete(mobile);
	}

	@Override
	public UUID findUserByMobileNumber(String mobile) {
		if (mobile == null || mobile.isEmpty())
			return null;
		Select select = QueryBuilder.select().from(
				"user_verified_mobile_number");
		select.where(QueryBuilder.eq("mobile_number", mobile));
		UserVerifiedMobileNumber mobileUser = cassandraOperations.selectOne(
				select, UserVerifiedMobileNumber.class);
		if (mobileUser == null)
			return null;
		else
			return mobileUser.getPk().getUserID();
	}

	@Override
	public List<UserVerifiedMobileNumber> findUsersByMobiles(
			List<String> mobiles) {
		if (mobiles == null || mobiles.isEmpty())
			return new ArrayList<UserVerifiedMobileNumber>();
		Select select = QueryBuilder.select().from(
				"user_verified_mobile_number");
		select.where(QueryBuilder.in("mobile_number", mobiles.toArray()));
		System.out.println(select);
		List<UserVerifiedMobileNumber> mobileUsers = cassandraOperations
				.select(select, UserVerifiedMobileNumber.class);
		return mobileUsers;
	}
}
