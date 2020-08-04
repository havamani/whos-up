package com.whosup.model;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "i_owe")
@JsonInclude(Include.NON_NULL)
public class IOwe {

	@PrimaryKey
	@JsonIgnore
	private IOweKey pk;

	@Column(value = "organizer_id")
	private UUID organizerId;

	private BigDecimal amount;

	@Column(value = "is_paid_electronically")
	private Boolean isPaidElectronically;

	@Column(value = "is_paid_by_cash")
	private Boolean isPaidByCash;

	@Column(value = "is_cancelled")
	private Boolean isCancelled;

	public IOwe() {

	}

	public IOwe(IOweKey pk, UUID organizerId, BigDecimal amount) {
		this.pk = pk;
		this.organizerId = organizerId;
		this.amount = amount;
		this.isCancelled = false;
		this.isPaidByCash = false;
		this.isPaidElectronically = false;
	}

	public IOweKey getPk() {
		return pk;
	}

	public void setPk(IOweKey pk) {
		this.pk = pk;
	}

	public UUID getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(UUID organizerId) {
		this.organizerId = organizerId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getIsPaidElectronically() {
		return isPaidElectronically;
	}

	public void setIsPaidElectronically(Boolean isPaidElectronically) {
		this.isPaidElectronically = isPaidElectronically;
	}

	public Boolean getIsPaidByCash() {
		return isPaidByCash;
	}

	public void setIsPaidByCash(Boolean isPaidByCash) {
		this.isPaidByCash = isPaidByCash;
	}

	public Boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

}
