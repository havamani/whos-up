package com.whosup.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "owes_me")
@JsonInclude(Include.NON_NULL)
public class OwesMe {

	@PrimaryKey
	@JsonIgnore
	private OwesMeKey pk;

	@Column(value = "currency")
	private String currency;

	@Column(value = "amount_owed")
	private BigDecimal amountOwed;

	@Column(value = "event_date")
	private Date eventDate;

	@Column(value = "event_title")
	private String eventTitle;

	@Column(value = "owing_user_name")
	private String owingUserName;

	@Column(value = "owing_user_photo_path")
	private String owingUserPhotoPath;

	@Column(value = "user_transaction_id")
	private UUID transactionId;

	@Column(value = "date_marked_as_paid")
	private Date dateMarkedAsPaid;

	@Column(value = "date_paid_electronically")
	private Date datePaidElectronically;

	@Column(value = "date_refunded")
	private Date dateRefunded;

	public OwesMe() {

	}

	public OwesMe(OwesMeKey pk) {
		this.pk = pk;
	}

	public OwesMeKey getPk() {
		return pk;
	}

	public void setPk(OwesMeKey pk) {
		this.pk = pk;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAmountOwed() {
		return amountOwed;
	}

	public void setAmountOwed(BigDecimal amountOwed) {
		this.amountOwed = amountOwed;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getOwingUserName() {
		return owingUserName;
	}

	public void setOwingUserName(String owingUserName) {
		this.owingUserName = owingUserName;
	}

	public String getOwingUserPhotoPath() {
		return owingUserPhotoPath;
	}

	public void setOwingUserPhotoPath(String owingUserPhotoPath) {
		this.owingUserPhotoPath = owingUserPhotoPath;
	}

	public UUID getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}

	public Date getDateMarkedAsPaid() {
		return dateMarkedAsPaid;
	}

	public void setDateMarkedAsPaid(Date dateMarkedAsPaid) {
		this.dateMarkedAsPaid = dateMarkedAsPaid;
	}

	public Date getDatePaidElectronically() {
		return datePaidElectronically;
	}

	public void setDatePaidElectronically(Date datePaidElectronically) {
		this.datePaidElectronically = datePaidElectronically;
	}

	public Date getDateRefunded() {
		return dateRefunded;
	}

	public void setDateRefunded(Date dateRefunded) {
		this.dateRefunded = dateRefunded;
	}

}
