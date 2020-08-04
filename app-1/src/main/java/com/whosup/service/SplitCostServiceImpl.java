package com.whosup.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.Refund;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.EventDao;
import com.whosup.dao.IOweDao;
import com.whosup.dao.OwesMeDao;
import com.whosup.dao.UserDao;
import com.whosup.dao.UserEventInboxDao;
import com.whosup.dao.UserTransactionsDao;
import com.whosup.model.AccessToken;
import com.whosup.model.Event;
import com.whosup.model.IOwe;
import com.whosup.model.OwesMe;
import com.whosup.model.User;
import com.whosup.model.UserTransactions;
import com.whosup.model.request.SplitCost;
import com.whosup.model.response.IOweResponse;
import com.whosup.model.response.OwesMeResponse;
import com.whosup.model.response.OwesMeUserDetails;
import com.whosup.model.response.Status;

public class SplitCostServiceImpl implements SplitCostService {

	static Logger log = Logger.getLogger(SplitCostServiceImpl.class.getName());

	@Autowired
	private UserDao userDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private IOweDao iOweDao;
	@Autowired
	private OwesMeDao owesMeDao;
	@Autowired
	private UserEventInboxDao userEventInboxDao;
	@Autowired
	private UserTransactionsDao userTransactionsDao;
	@Autowired
	private CommonService commonService;

	private static final String STRIPE_API_KEY = "sk_test_PHIVUV8doTYbSQYahzo5bN18";

	@Override
	public List<IOweResponse> getIOwe(UUID userID) {
		List<IOweResponse> iOwes = new ArrayList<IOweResponse>();
		List<IOwe> owes = iOweDao.findByUserId(userID);
		List<UUID> eventIds = new ArrayList<UUID>();
		List<UUID> organizerIds = new ArrayList<UUID>();

		if (owes != null) {
			for (IOwe i : owes) {
				organizerIds.add(i.getOrganizerId());
				eventIds.add(i.getPk().getEventID());
			}
			List<User> users = userDao.findUserByIds(organizerIds);
			List<Event> events = eventDao.findEventsById(eventIds);
			Map<UUID, User> hashMapUsers = new HashMap<>();
			Map<UUID, Event> hashMapEvents = new HashMap<>();
			if (users != null) {
				for (User user : users) {
					hashMapUsers.put(user.getUserID(), user);
				}
				for (Event event : events) {
					hashMapEvents.put(event.getEventID(), event);
				}
				for (IOwe i : owes) {
					IOweResponse iOwe = new IOweResponse();
					iOwe.setAmount(i.getAmount());
					iOwe.setEventId(i.getPk().getEventID());
					iOwe.setOrganiserId(i.getOrganizerId());
					User user = hashMapUsers.get(i.getOrganizerId());
					iOwe.setOrganiserName(user.getFullName());
					iOwe.setOrganiserPhotoPath(user.getPhotoPath());
					Event event = hashMapEvents.get(i.getPk().getEventID());
					iOwe.setCurrency(event.getCurrency());
					iOwe.setEventName(event.getTitle());
					iOwe.setEventDate(event.getEventDate());
					if (iOwe.getPeoplePaid() == null)
						iOwe.setPeoplePaid(0);
					else
						iOwe.setPeoplePaid(event.getTotalPaid());
					iOwe.setTotalPeople(event.getTotalAttending());
					iOwes.add(iOwe);
				}
			}
		}
		Collections.sort(iOwes, new Comparator<IOweResponse>() {
			public int compare(IOweResponse o1, IOweResponse o2) {
				return o1.getEventDate().compareTo(o2.getEventDate());
			}
		});
		return iOwes;
	}

	@Override
	public List<OwesMeResponse> getOwesMe(UUID userID) {
		List<OwesMeResponse> owesMes = new ArrayList<OwesMeResponse>();
		List<OwesMe> payments = owesMeDao.selectOwesMe(userID);
		Set<UUID> eventIds = new HashSet<UUID>();
		for (OwesMe o : payments)
			eventIds.add(o.getPk().getEventId());
		List<Event> events = eventDao.findEventsById(new ArrayList<UUID>(
				eventIds));
		HashMap<UUID, Event> hashMapEventDetails = new HashMap<>();
		for (Event event : events) {
			eventIds.add(event.getEventID());
			hashMapEventDetails.put(event.getEventID(), event);
		}

		List<OwesMeUserDetails> owesMeUserDetails = new ArrayList<OwesMeUserDetails>();
		OwesMeResponse owesMe = new OwesMeResponse();
		if (payments != null) {
			int firstAttempt = 1;
			for (OwesMe payment : payments) {
				Boolean isNewEvent = false;
				if (firstAttempt == 1
						|| !(payment.getPk().getEventId().equals(owesMe
								.getEventID()))) {
					if (firstAttempt != 1) {
						owesMe.setOwesMeUserDetails(owesMeUserDetails);
						owesMes.add(owesMe);
					}
					for (OwesMeResponse o : owesMes) {
						if (o.getEventID().equals(payment.getPk().getEventId())) {
							owesMe = o;
							owesMeUserDetails = new ArrayList<OwesMeUserDetails>();
							owesMeUserDetails.addAll(o.getOwesMeUserDetails());
							owesMes.remove(o);
							isNewEvent = true;
							break;
						}
					}
					if (!isNewEvent) {
						owesMe = new OwesMeResponse();
						owesMeUserDetails = new ArrayList<OwesMeUserDetails>();
						Event event = hashMapEventDetails.get(payment.getPk()
								.getEventId());
						owesMe.setEventID(payment.getPk().getEventId());
						owesMe.setTitle(payment.getEventTitle());
						owesMe.setEventDate(payment.getEventDate());
						owesMe.setCurrency(payment.getCurrency());
						if (event.getIsFixedAmount()) {
							owesMe.setCostToSplit(event.getFixedCost());
							owesMe.setAmountPaid(new BigDecimal(((event
									.getTotalPaid() == null ? 0 : event
									.getTotalPaid()) + 1)
									* event.getFixedCost().doubleValue()));
							owesMe.setTotalAmount(new BigDecimal(event
									.getTotalAttending()
									* event.getFixedCost().doubleValue()));
						} else {
							owesMe.setCostToSplit(event.getCostToSplit());
							owesMe.setAmountPaid(new BigDecimal(
									((event.getTotalPaid() == null ? 0 : event
											.getTotalPaid()) + 1)
											* (event.getCostToSplit()
													.doubleValue() / event
													.getTotalAttending())));
							owesMe.setTotalAmount(event.getCostToSplit());
						}
					}
					isNewEvent = false;
					++firstAttempt;
				}

				OwesMeUserDetails owesMeUserDetail = new OwesMeUserDetails();
				owesMeUserDetail.setFullName(payment.getOwingUserName());
				owesMeUserDetail.setPhotoPath(userDao.findById(
						payment.getPk().getOwingUserId()).getPhotoPath());
				owesMeUserDetail.setUserID(payment.getPk().getOwingUserId());
				if (payment.getDateMarkedAsPaid() != null
						|| payment.getDatePaidElectronically() != null)
					owesMeUserDetail.setPaid(true);
				else
					owesMeUserDetail.setPaid(false);
				if (payment.getDateRefunded() != null)
					owesMeUserDetail.setIsRefund(true);
				else
					owesMeUserDetail.setIsRefund(false);
				owesMeUserDetails.add(owesMeUserDetail);
			}
			owesMes.add(owesMe);
			owesMe.setOwesMeUserDetails(owesMeUserDetails);
		}
		Collections.sort(owesMes, new Comparator<OwesMeResponse>() {
			public int compare(OwesMeResponse o1, OwesMeResponse o2) {
				return o1.getEventDate().compareTo(o2.getEventDate());
			}
		});
		return owesMes;
	}

	@Override
	public List<OwesMeResponse> paidCash(UUID userID, SplitCost request) {
		OwesMe payment = owesMeDao.selectByEventAndUserAndOrganizer(userID,
				request.getEventId(), request.getPaidCashUserId());
		if (!payment.getPk().getUserId().equals(userID))
			throw new AppException(new AppErrorMessage("400",
					"You are not allowed to perform this action"));
		if (payment.getDatePaidElectronically() == null
				&& payment.getDateMarkedAsPaid() == null) {
			UserTransactions transaction = new UserTransactions();
			transaction.setEventId(request.getEventId());
			transaction.setAmountPaid(payment.getAmountOwed());
			transaction.setDestinationUserId(payment.getPk().getUserId());
			transaction.setId(UserTransactions.getUUID());
			transaction.setChargeDate(new Date());
			transaction.setStripeChargeId("cash");
			transaction.setStripeSenderUserId(payment.getPk().getOwingUserId());
			transaction.setIsTransactionConfirmed(true);
			transaction.setTransDecription("Who's Up Charge for Event "
					+ eventDao.selectEvent(request.getEventId()).getTitle());
			userTransactionsDao.save(transaction);
			payment.setTransactionId(transaction.getId());
			payment.setDateMarkedAsPaid(new Date());
			owesMeDao.update(payment);

			Event event = eventDao.selectEvent(request.getEventId());
			event.setTotalPaid((event.getTotalPaid() == null ? 0 : event
					.getTotalPaid()) + 1);
			eventDao.update(event);
			IOwe owe = iOweDao.findByUserIdAndEventId(
					request.getPaidCashUserId(), request.getEventId());
			owe.setIsPaidByCash(true);
			iOweDao.update(owe);
		}
		try {
			JSONObject notification = new JSONObject();
			notification.put("type", "MARKED_AS_PAID");
			notification.put("message", "You have been marked as paid "+payment.getAmountOwed() +" for the Event "+payment.getEventTitle());
			commonService.notify(userDao.findById(request.getPaidCashUserId()), notification.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getOwesMe(userID);
	}

	@Override
	public List<OwesMeResponse> paymentRefund(UUID userID, UUID owingUserId,
			UUID eventId) {
		OwesMe payment = owesMeDao.selectByEventAndUserAndOrganizer(userID,
				eventId, owingUserId);
		if (!payment.getPk().getUserId().equals(userID))
			throw new AppException(new AppErrorMessage("400",
					"You are not allowed to perform this action"));
		if (payment.getDateRefunded() == null) {
			if (payment.getDatePaidElectronically() != null
					&& refundAmount(payment) == 200 ) {
				payment.setDateRefunded(new Date());
			} else if (payment.getDateMarkedAsPaid() != null) {
				UserTransactions transaction = userTransactionsDao
						.findById(payment.getTransactionId());
				transaction.setStripeRefundId("cash");
				transaction.setRefundDate(new Date());
				userTransactionsDao.update(transaction);
				payment.setDateRefunded(new Date());
			}
			owesMeDao.update(payment);
			Event event = eventDao.selectEvent(eventId);
			event.setTotalPaid(event.getTotalPaid() - 1);
			eventDao.update(event);
		}
		try {
			JSONObject notification = new JSONObject();
			notification.put("type", "AMOUNT_REFUNDED");
			notification.put("message", "You have been Refunded "+ payment.getAmountOwed() +" for the event " + payment.getEventTitle());
			commonService.notify(userDao.findById(userID), notification.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getOwesMe(userID);
	}

	@Override
	public Status stripePay(UUID userId, UUID eventId, String payAccessToken) {
		OwesMe payment = owesMeDao.selectByEventAndUserAndOrganizer(iOweDao
				.findByUserIdAndEventId(userId, eventId));
		if (payment == null)
			throw new AppException(new AppErrorMessage("400",
					"You can't pay to this Event"));
		if (payment.getDatePaidElectronically() != null
				|| payment.getDateMarkedAsPaid() != null)
			throw new AppException(new AppErrorMessage("400",
					"You already paid to this Event"));

		RequestOptions requestOptions = new RequestOptions.RequestOptionsBuilder()
				.setApiKey(STRIPE_API_KEY).build();

		// RequestOptions requestOptions = new
		// RequestOptions.RequestOptionsBuilder()
		// .setApiKey(STRIPE_API_KEY)
		// .setStripeAccount(
		// userDao.findById(payment.getPk().getUserId())
		// .getStripeId()).build();

		// Map<String, Object> tokenParams = new HashMap<String, Object>();
		// Map<String, Object> cardParams = new HashMap<String, Object>();
		// cardParams.put("number", "4242424242424242");
		// cardParams.put("exp_month", 12);
		// cardParams.put("exp_year", 2016);
		// cardParams.put("cvc", "314");
		// tokenParams.put("card", cardParams);
		// Token testToken = null;
		// try {
		// testToken = Token.create(tokenParams, requestOptions);
		// } catch (AuthenticationException | InvalidRequestException
		// | APIConnectionException | CardException | APIException e2) {
		// e2.printStackTrace();
		// }
		
		User user = userDao.findById(userId);
		if (user.getStripeCustomerId() == null
				|| user.getStripeCustomerId().isEmpty()) {
			Map<String, Object> customerParamsMap = new HashMap<String, Object>();
			customerParamsMap.put("source", payAccessToken);
			// customerParamsMap.put("source", testToken.getId());
			Customer customer = null;
			try {
				customer = Customer.create(customerParamsMap, requestOptions);
			} catch (AuthenticationException | InvalidRequestException
					| APIConnectionException | CardException | APIException e) {
				e.printStackTrace();
				throw new AppException(new AppErrorMessage("500",
						"Stripe Internal Server error"));
			}
			System.out
					.println("Customer id created for using given accessToken: "
							+ customer.getId());
			Status status = makePayment(payment, userId, eventId,
					customer.getId(), customer.getDefaultSource());
			if (status.getStatus().equals("Success")) {
				user.setStripeCustomerId(customer.getId());
				List<String> cardsforcustomer = new ArrayList<String>();
				cardsforcustomer.add(customer.getDefaultSource());
				user.setCardsForCustomer_id(cardsforcustomer);
				userDao.update(user);
			}
			return status;
		} else {
			Customer customer1 = null;
			try {
				customer1 = Customer.retrieve(user.getStripeCustomerId(),
						requestOptions);
			} catch (AuthenticationException | InvalidRequestException
					| APIConnectionException | CardException | APIException e) {
				e.printStackTrace();
				throw new AppException(new AppErrorMessage("500",
						"Stripe Internal Server error"));
			}
			System.out
					.println("Customer id retrieved from existing customer id: "
							+ customer1.getId());
			Map<String, Object> customerCardParam = new HashMap<String, Object>();
			customerCardParam.put("source", payAccessToken);
			// customerCardParam.put("source", testTokentest.getId());
			Card updatedCard = null;
			try {
				updatedCard = customer1.createCard(customerCardParam,
						requestOptions);
			} catch (AuthenticationException | InvalidRequestException
					| APIConnectionException | CardException | APIException e1) {
				e1.printStackTrace();
				throw new AppException(new AppErrorMessage("500",
						"Stripe Internal Server error"));
			}
			System.out.println("Card Id Added to the existing Customer Id: "
					+ updatedCard.getId());
			boolean isAlreadyCardAvailable = false;
			for (String cardid : user.getCardsForCustomer_id()) {
				try {
					ExternalAccount source = customer1.getSources().retrieve(
							cardid, requestOptions);
					Card cardfromserver = (Card) source;
					if (cardfromserver.getFingerprint().equals(
							updatedCard.getFingerprint())) {
						isAlreadyCardAvailable = true;
						break;
					}
				} catch (AuthenticationException | InvalidRequestException
						| APIConnectionException | CardException | APIException e) {
					e.printStackTrace();
					throw new AppException(new AppErrorMessage("500",
							"Stripe Internal Server error"));
				}
			}

			Status status;
			if (isAlreadyCardAvailable == true) {
				status = makePayment(payment, userId, eventId,
						user.getStripeCustomerId(), updatedCard.getId());
			} else {
				status = makePayment(payment, userId, eventId,
						user.getStripeCustomerId(), updatedCard.getId());
				if (status.getStatus().equals("Success")) {
					List<String> customerCardIds = new ArrayList<String>();
					customerCardIds.addAll(user.getCardsForCustomer_id());
					customerCardIds.add(updatedCard.getId());
					user.setCardsForCustomer_id(customerCardIds);
					userDao.update(user);
					Event event = eventDao.selectEvent(eventId);
					try {
						JSONObject notification = new JSONObject();
						notification.put("type", "PAY_SUCCESS");
						notification.put("message", "You have been paid "+ payment.getAmountOwed() +" by " + user.getFullName() + " for the event " + payment.getEventTitle());
						commonService.notify(userDao.findById(event.getOrganizerUserID()), notification.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return status;
		}
	}

	@Override
	public Status stripePayUsingExistingCards(UUID userId, UUID eventId,
			String cardId) {
		OwesMe payment = owesMeDao.selectByEventAndUserAndOrganizer(iOweDao
				.findByUserIdAndEventId(userId, eventId));
		if (payment == null)
			throw new AppException(new AppErrorMessage("400",
					"You can't pay to this Event"));
		if (payment.getDatePaidElectronically() != null
				|| payment.getDateMarkedAsPaid() != null)
			throw new AppException(new AppErrorMessage("400",
					"You already paid to this Event"));
		User user = userDao.findById(userId);
		if (!(user.getStripeCustomerId() == null || user.getStripeCustomerId()
				.isEmpty())) {
			Status status = makePayment(payment, userId, eventId,
					user.getStripeCustomerId(), cardId);
			return status;
		} else {
			return new Status("500");
		}
	}

	@Override
	public Status stripeConfirm(UUID userID, String code, String error,
			String error_description) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("code", code);
		vars.put("grant_type", "authorization_code");
		boolean isErrorCodeAvailable = (error != null) ? true : false;
		if (isErrorCodeAvailable) {
			throw new AppException(
					new AppErrorMessage(error, error_description));
		} else {
			try {
				AccessToken accessToken = AccessToken.create(vars,
						STRIPE_API_KEY);
				User user = userDao.findById(userID);
				user.setStripeId(accessToken.getStripeUserId());
				user.setStripeSignInToken(accessToken.getAccessToken());
				userDao.update(user);
				return new Status("200");
			} catch (AuthenticationException | InvalidRequestException
					| APIConnectionException | CardException | APIException e) {
				return new Status("400");
			}
		}
	}

	protected int refundAmount(OwesMe payment) {
		UserTransactions transaction = userTransactionsDao.findById(payment
				.getTransactionId());
		try {
			System.out.println("Refund Starts");
			// RequestOptions requestOptions = new
			// RequestOptions.RequestOptionsBuilder()
			// .setApiKey(STRIPE_API_KEY).build();
			RequestOptions requestOptions = new RequestOptions.RequestOptionsBuilder()
					.setApiKey(STRIPE_API_KEY)
					.setStripeAccount(
							userDao.findById(payment.getPk().getUserId())
									.getStripeId()).build();

			System.out.println("Refund details : " + requestOptions.toString());
			Map<String, Object> refundParams = new HashMap<String, Object>();
			System.out.println("Refund details : " + refundParams.toString());
			refundParams.put("charge", transaction.getStripeChargeId());
			Refund refund = Refund.create(refundParams, requestOptions);
			System.out.println("Refund details : " + refund.toString());
			if (refund != null) {
				transaction.setStripeRefundId(refund.getId());
				transaction.setRefundDate(new Date());
				userTransactionsDao.update(transaction);
			}
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			System.out.println(e.toString());
			return 500;
		}
		return 200;
	}

	@Override
	public Status stripeAccount(UUID userID) {
		Status status = new Status();
		User user = userDao.findById(userID);
		if (user.getStripeSignInToken() == null
				|| user.getStripeSignInToken().isEmpty()) {
			status.setStatus("400");
			return status;
		}
		try {
			RequestOptions requestOptions = new RequestOptions.RequestOptionsBuilder()
					.setApiKey(STRIPE_API_KEY)
					.setStripeAccount(user.getStripeId()).build();
			Account account = Account.retrieve(requestOptions);
			status.setStripeAccount(account.getEmail());
			if (status.getStripeAccount() == null
					|| status.getStripeAccount().isEmpty())
				status.setStatus("400");
			else
				status.setStatus("200");
		} catch (AuthenticationException e) {
			status.setStatus("400");
		} catch (InvalidRequestException | APIConnectionException
				| CardException | APIException e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Status stripeVerify(UUID userID) {
		User user = userDao.findById(userID);
		if (user.getStripeId() != null && user.getStripeId().length() > 0)
			return new Status("200");
		else
			return new Status("400");
	}

	@Override
	public List<Card> getcards(UUID userId) {

		RequestOptions requestOptions = new RequestOptions.RequestOptionsBuilder()
				.setApiKey(STRIPE_API_KEY).build();

		List<Card> availableCardsForUser = new ArrayList<Card>();
		User user = userDao.findById(userId);
		try {
			if (!(user.getStripeCustomerId() == null || user
					.getStripeCustomerId().equals(""))) {
				Customer customer = Customer.retrieve(
						user.getStripeCustomerId(), requestOptions);
				for (String cardid : user.getCardsForCustomer_id()) {
					ExternalAccount source = customer.getSources().retrieve(
							cardid, requestOptions);
					if (source.getObject().equals("card")) {
						Card cardfromserver = (Card) source;
						availableCardsForUser.add(cardfromserver);
					}
				}
			} else {
				return new ArrayList<Card>();
			}

		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new AppException(new AppErrorMessage("500",
					"Stripe Internal Server error"));
		}
		return availableCardsForUser;
	}

	public Status makePayment(OwesMe payment, UUID userId, UUID eventId,
			String customerId, String cardId) {

		RequestOptions requestOptionsWithAccountID = new RequestOptions.RequestOptionsBuilder()
				.setApiKey(STRIPE_API_KEY)
				.setStripeAccount(
						userDao.findById(payment.getPk().getUserId())
								.getStripeId()).build();

		Map<String, Object> tokenParamsToPay = new HashMap<String, Object>();
		tokenParamsToPay.put("card", cardId);
		tokenParamsToPay.put("customer", customerId);
		Token TokenTopay = null;
		try {
			TokenTopay = Token.create(tokenParamsToPay,
					requestOptionsWithAccountID);
			System.out.println("Token Id Used for Payment: "
					+ TokenTopay.getId());
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e2) {
			e2.printStackTrace();
			throw new AppException(new AppErrorMessage("500",
					"Stripe Internal Server error"));
		}
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("source", TokenTopay.getId());
		chargeMap.put("amount",
				(int) Math.ceil(payment.getAmountOwed().doubleValue() * 100));
		chargeMap.put("currency", payment.getCurrency());
		// chargeMap.put("customer", customerId);
		// chargeMap.put("card", cardId);
		chargeMap.put("description", "Who's Up Charge for Event "
				+ eventDao.selectEvent(eventId).getTitle());
		try {
			Charge charge = Charge.create(chargeMap,
					requestOptionsWithAccountID);
			System.out.println("Charge Id created for the last card: "
					+ charge.getId());
			if (charge != null) {
				if (charge.getStatus().equals("succeeded") && charge.getPaid()) {
					UserTransactions transaction = new UserTransactions();
					transaction.setEventId(eventId);
					transaction.setAmountPaid(payment.getAmountOwed());
					transaction.setDestinationUserId(payment.getPk()
							.getUserId());
					transaction.setId(UserTransactions.getUUID());
					transaction.setChargeDate(new Date());
					transaction.setStripeChargeId(charge.getId());
					transaction.setStripeSenderUserId(payment.getPk()
							.getOwingUserId());
					transaction.setIsTransactionConfirmed(true);
					transaction.setTransDecription(charge.getDescription());
					transaction.setReceiptDetail(charge.toString());
					userTransactionsDao.save(transaction);
					payment.setTransactionId(transaction.getId());
					payment.setDatePaidElectronically(new Date());
					owesMeDao.update(payment);
					Event event = eventDao.selectEvent(eventId);
					event.setTotalPaid((event.getTotalPaid() == null ? 0
							: event.getTotalPaid()) + 1);
					eventDao.update(event);
					IOwe owe = iOweDao.findByUserIdAndEventId(userId, eventId);
					owe.setIsPaidElectronically(true);
					iOweDao.update(owe);
				} else {
					return new Status("failed");
				}
			}
		} catch (StripeException e) {
			e.printStackTrace();
			throw new AppException(new AppErrorMessage("500",
					"Stripe Internal Server error"));
		}
		return new Status("Success");
	}

}
