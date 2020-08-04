package com.whosup.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "user_verified_domain")
@JsonInclude(Include.NON_NULL)
public class UserVerifiedDomain {

	@PrimaryKey
	private UserVerifiedDomainKey pk;

	public UserVerifiedDomain() {

	}

	public UserVerifiedDomain(UserVerifiedDomainKey pk) {
		this.pk = pk;
	}

	public UserVerifiedDomainKey getPk() {
		return pk;
	}

	public void setPk(UserVerifiedDomainKey pk) {
		this.pk = pk;
	}

	public String getDomainName() {
		return pk.getDomainName();
	}
}
