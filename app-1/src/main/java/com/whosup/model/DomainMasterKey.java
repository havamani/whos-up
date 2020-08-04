package com.whosup.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "domain_masterkey")
@JsonInclude(Include.NON_NULL)
public class DomainMasterKey {

	@PrimaryKey(value = "company_domain")
	private String companyDomain;

	@Column(value = "domain_masterkey")
	private String masterKey;

	public DomainMasterKey() {

	}

	public DomainMasterKey(String companyDomain, String masterKey) {
		this.companyDomain = companyDomain;
		this.masterKey = masterKey;
	}

	public String getCompanyDomain() {
		return companyDomain;
	}

	public void setCompanyDomain(String companyDomain) {
		this.companyDomain = companyDomain;
	}

	public String getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(String masterKey) {
		this.masterKey = masterKey;
	}

}
