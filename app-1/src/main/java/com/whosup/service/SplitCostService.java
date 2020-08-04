package com.whosup.service;

import java.util.List;
import java.util.UUID;

import com.stripe.model.Card;
import com.whosup.model.request.SplitCost;
import com.whosup.model.response.IOweResponse;
import com.whosup.model.response.OwesMeResponse;
import com.whosup.model.response.Status;

public interface SplitCostService {

	List<IOweResponse> getIOwe(UUID userID);

	List<OwesMeResponse> getOwesMe(UUID userID);

	List<OwesMeResponse> paidCash(UUID userID, SplitCost request);

	List<OwesMeResponse> paymentRefund(UUID userID, UUID owingUserId,
			UUID eventId);

	Status stripeConfirm(UUID userID, String code, String error, String error_description);

	Status stripePay(UUID userId, UUID eventId, String payAccessToken);
	
	Status stripePayUsingExistingCards(UUID userId, UUID eventId, String cardId);

	Status stripeVerify(UUID userId);

	Status stripeAccount(UUID userID);

	List<Card> getcards(UUID userId);

}
