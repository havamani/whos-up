package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.UserVerifiedDomain;

public interface UserVerifiedDomainDao {

	UserVerifiedDomain findByUser(UUID userId);

	List<UUID> findUsersByDomain(String domain);

	long findDomainCount(String domainName);

	void save(UserVerifiedDomain domain);

	void delete(UserVerifiedDomain domain);

}
