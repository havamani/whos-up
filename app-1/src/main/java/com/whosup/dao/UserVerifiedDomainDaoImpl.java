package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.UserVerifiedDomain;

public class UserVerifiedDomainDaoImpl implements UserVerifiedDomainDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public void save(UserVerifiedDomain domain) {
		if (domain != null)
			cassandraOperations.insert(domain);
	}

	@Override
	public void delete(UserVerifiedDomain domain) {
		if (domain != null)
			cassandraOperations.delete(domain);
	}

	@Override
	public UserVerifiedDomain findByUser(UUID userId) {
		if (userId == null)
			return null;
		Select select = QueryBuilder.select().from("user_verified_domain")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId));
		List<UserVerifiedDomain> domain = cassandraOperations.select(select,
				UserVerifiedDomain.class);
		if (domain != null && domain.size() > 0)
			return domain.get(0);
		else
			return null;
	}

	@Override
	public List<UUID> findUsersByDomain(String domain) {
		if (domain == null)
			return null;
		Select select = QueryBuilder.select().from("user_verified_domain");
		select.where(QueryBuilder.eq("domain_name", domain));
		List<UserVerifiedDomain> domainUsers = cassandraOperations.select(
				select, UserVerifiedDomain.class);
		if (domainUsers != null) {
			List<UUID> userIds = new ArrayList<UUID>();
			for (UserVerifiedDomain u : domainUsers)
				userIds.add(u.getPk().getuserId());
			return userIds;
		}
		return new ArrayList<UUID>();
	}

	@Override
	public long findDomainCount(String domainName) {
		if (domainName == null)
			return 0L;
		String select = "SELECT count(*) FROM user_verified_domain WHERE domain_name='"
				+ domainName + "'";
		Long count = cassandraOperations.queryForObject(select, Long.class);
		return count.longValue();
	}
}
