package com.whosup.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.UserAccessTokenDao;
import com.whosup.dao.UserDao;
import com.whosup.model.User;
import com.whosup.model.request.SplitCost;
import com.whosup.model.response.IOweResponse;
import com.whosup.model.response.OwesMeResponse;
import com.whosup.model.response.Status;
import com.whosup.service.SplitCostService;

@Path("/splitCost")
@Component
public class SplitCostResource {

	@Autowired
	private SplitCostService splitCostService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAccessTokenDao userAccessTokenDao;

	/**
	 * return list of iowe Details
	 *
	 */
	@GET
	@Produces("application/json")
	@Path("/iOwe")
	public List<IOweResponse> iOwe(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return splitCostService.getIOwe(user.getUserID());
	}

	@POST
	@Produces("application/json")
	@Path("/stripe/pay")
	public Status stripePay(SplitCost request) {
		User user = validateAccessToken(request.getAccessToken());
		return splitCostService.stripePay(user.getUserID(),
				request.getEventId(), request.getPayAccessToken());
	}

	@GET
	@Produces("application/json")
	@Path("/stripe/payExist")
	public Status stripePayUsingExistingCards(
			@QueryParam(value = "access_token") UUID accessToken,
			@QueryParam(value = "event_id") UUID eventId,
			@QueryParam(value = "card_id") String CardId) {
		User user = validateAccessToken(accessToken);
		return splitCostService.stripePayUsingExistingCards(user.getUserID(),
				eventId, CardId);
	}

	@GET
	@Produces("application/json")
	@Path("/stripe/getCards")
	public List<Card> getCards(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return splitCostService.getcards(user.getUserID());
	}

	@GET
	@Produces("application/json")
	@Path("/owesMe")
	public List<OwesMeResponse> owesMe(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return splitCostService.getOwesMe(user.getUserID());
	}

	@POST
	@Produces("application/json")
	@Path("/owesMe/paidCash")
	public List<OwesMeResponse> paidCash(SplitCost request) {
		validateAccessToken(request.getAccessToken());
		return splitCostService.paidCash(
				validateAccessToken(request.getAccessToken()).getUserID(),
				request);
	}

	@POST
	@Produces("application/json")
	@Path("/owesMe/refund")
	public List<OwesMeResponse> paymentRefund(SplitCost request) {
		// StripePayAndRefundValidation(request.getPayAccessToken());
		validateAccessToken(request.getAccessToken());
		return splitCostService.paymentRefund(
				validateAccessToken(request.getAccessToken()).getUserID(),
				request.getRefundUserId(), request.getEventId());
	}

	@GET
	@Produces("application/json")
	@Path("/stripe/account")
	public Status stripeAccount(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return splitCostService.stripeAccount(user.getUserID());
	}

	@GET
	@Produces("application/json")
	@Path("/stripe/verify")
	public Status stripeVerify(
			@QueryParam(value = "access_token") UUID accessToken) {
		User user = validateAccessToken(accessToken);
		return splitCostService.stripeVerify(user.getUserID());
	}

	@GET
	@Produces("application/json")
	@Path("/stripe/confirm")
	public Status stripeConfirm(@QueryParam(value = "scope") String scope,
			@QueryParam(value = "code") String code,
			@QueryParam(value = "state") String accessToken,
			@QueryParam(value = "error") String error,
			@QueryParam(value = "error_description") String error_description) {
		User user = null;
		try {
			user = validateAccessToken(UUID.fromString(accessToken));
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new AppException(new AppErrorMessage("400",
					"You must login first in whosup"));
		}
		return splitCostService.stripeConfirm(user.getUserID(), code, error,
				error_description);
	}

	private User validateAccessToken(UUID accessToken) {
		User user = userDao.findById(userAccessTokenDao
				.findUserIdByAccessToken(accessToken));
		if (user != null) {
			if (user.getAccessTokenExpireDate().before(new Date()))
				throw new AppException(new AppErrorMessage("400",
						"Your Access Token has been Expired"));
			return user;
		} else
			throw new AppException(new AppErrorMessage("101",
					"Given Token is invalid"));
	}

	private void StripePayAndRefundValidation(String payAccessToken) {

		RequestOptions requestOptions = new RequestOptions.RequestOptionsBuilder()
				.setApiKey("sk_test_PHIVUV8doTYbSQYahzo5bN18").build();

		RequestOptions requestOptions1 = new RequestOptions.RequestOptionsBuilder()
				.setApiKey("sk_test_PHIVUV8doTYbSQYahzo5bN18")
				.setStripeAccount("acct_178W8sFEB9oug2Zn").build();

		RequestOptions requestOptions2 = new RequestOptions.RequestOptionsBuilder()
				.setApiKey("sk_test_PHIVUV8doTYbSQYahzo5bN18")
				.setStripeAccount("acct_16jo9aD1PwQ5Ye86").build();

		// Customer existingCustomer = null;
		// try {
		// existingCustomer = Customer.retrieve("cus_7ey3uVQSk0y12n",
		// requestOptions2);
		// } catch (AuthenticationException | InvalidRequestException
		// | APIConnectionException | CardException | APIException e1) {
		// e1.printStackTrace();
		// }
		// System.out.println(existingCustomer.getId());

		Map<String, Object> tokenParams = new HashMap<String, Object>();
		Map<String, Object> cardParams = new HashMap<String, Object>();
		cardParams.put("number", "4242424242424242");
		cardParams.put("exp_month", 12);
		cardParams.put("exp_year", 2016);
		cardParams.put("cvc", "314");
		tokenParams.put("card", cardParams);
		Token testToken = null;
		try {
			testToken = Token.create(tokenParams, requestOptions);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e2) {
			e2.printStackTrace();
		}
		System.out.println(testToken.getId());

		Map<String, Object> customerParamsMap = new HashMap<String, Object>();
		customerParamsMap.put("source", testToken.getId());
		// customerParamsMap.put("source", payAccessToken);
		Customer customer = null;
		try {
			customer = Customer.create(customerParamsMap, requestOptions);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
		}
		System.out.println(customer.getId());

		Map<String, Object> tokenParamsToPay = new HashMap<String, Object>();
		tokenParamsToPay.put("card", customer.getDefaultSource());
		tokenParamsToPay.put("customer", customer.getId());

		Token testTokenTopay = null;
		try {
			testTokenTopay = Token.create(tokenParamsToPay, requestOptions2);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e2) {
			e2.printStackTrace();
		}
		System.out.println(testTokenTopay.getId());

		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", 2000);
		chargeMap.put("currency", "EUR");
		chargeMap.put("source", testTokenTopay.getId());
		// chargeMap.put("customer", customer.getId());
		// chargeMap.put("card", customer.getDefaultSource());
		chargeMap.put("description", "Who's Up Charge for Event ");
		System.out.println(chargeMap);
		Charge charge = null;
		try {
			charge = Charge.create(chargeMap, requestOptions2);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
		}
		System.out.println(charge.getId());
		Map<String, Object> refundParams = new HashMap<String, Object>();
		refundParams.put("charge", charge.getId());
		Refund refund = null;
		try {
			refund = Refund.create(refundParams, requestOptions2);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			e.printStackTrace();
		}
		System.out.println(refund.getId());
	}

}
