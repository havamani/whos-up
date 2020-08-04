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

@Table(value = "user_transaction")
@JsonInclude(Include.NON_NULL)
public class UserTransactions extends Model {

	@PrimaryKey
	@JsonIgnore
	private UUID id;

	@Column(value = "event_id")
	private UUID eventId;

	@Column(value = "stripe_sender_user_id")
	private UUID stripeSenderUserId;

	@Column(value = "stripe_charge_id")
	private String stripeChargeId;

	@Column(value = "stripe_refund_id")
	private String stripeRefundId;

	@Column(value = "trans_decription")
	private String transDecription;

	@Column(value = "receipt_detail")
	private String receiptDetail;

	@Column(value = "charge_date")
	private Date chargeDate;

	@Column(value = "refund_date")
	private Date refundDate;

	@Column(value = "amount_paid")
	private BigDecimal amountPaid;

	@Column(value = "destination_user_id")
	private UUID destinationUserId;

	@Column(value = "is_transaction_confirmed")
	private Boolean isTransactionConfirmed;

	public UserTransactions() {

	}

	public UserTransactions(UUID id, UUID stripeSenderUserId,
			UUID destinationUserId) {
		this.id = id;
		this.stripeSenderUserId = stripeSenderUserId;
		this.destinationUserId = destinationUserId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

	public UUID getStripeSenderUserId() {
		return stripeSenderUserId;
	}

	public void setStripeSenderUserId(UUID stripeSenderUserId) {
		this.stripeSenderUserId = stripeSenderUserId;
	}

	public String getStripeChargeId() {
		return stripeChargeId;
	}

	public void setStripeChargeId(String stripeChargeId) {
		this.stripeChargeId = stripeChargeId;
	}

	public String getStripeRefundId() {
		return stripeRefundId;
	}

	public void setStripeRefundId(String stripeRefundId) {
		this.stripeRefundId = stripeRefundId;
	}

	public String getTransDecription() {
		return transDecription;
	}

	public void setTransDecription(String transDecription) {
		this.transDecription = transDecription;
	}

	public String getReceiptDetail() {
		return receiptDetail;
	}

	public void setReceiptDetail(String receiptDetail) {
		this.receiptDetail = receiptDetail;
	}

	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public UUID getDestinationUserId() {
		return destinationUserId;
	}

	public void setDestinationUserId(UUID destinationUserId) {
		this.destinationUserId = destinationUserId;
	}

	public Boolean getIsTransactionConfirmed() {
		return isTransactionConfirmed;
	}

	public void setIsTransactionConfirmed(Boolean isTransactionConfirmed) {
		this.isTransactionConfirmed = isTransactionConfirmed;
	}

}
