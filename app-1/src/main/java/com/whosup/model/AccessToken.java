package com.whosup.model;

import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.net.APIResource;
import com.stripe.net.RequestOptions;

public class AccessToken extends APIResource {

	String accessToken;

	Boolean livemode;

	String refreshToken;

	String tokenType;

	String stripePublishableKey;

	String stripeUserId;

	String scope;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Boolean getLivemode() {
		return livemode;
	}

	public void setLivemode(Boolean livemode) {
		this.livemode = livemode;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getStripePublishableKey() {
		return stripePublishableKey;
	}

	public void setStripePublishableKey(String stripePublishableKey) {
		this.stripePublishableKey = stripePublishableKey;
	}

	public String getStripeUserId() {
		return stripeUserId;
	}

	public void setStripeUserId(String stripeUserId) {
		this.stripeUserId = stripeUserId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public static AccessToken create(Map<String, Object> params)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {
		return create(params, null);
	}

	public static AccessToken create(Map<String, Object> params, String apiKey)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {
		RequestOptions requestOption = (new RequestOptions.RequestOptionsBuilder())
				.setApiKey(apiKey).build();
		return request(RequestMethod.POST, classURL(AccessToken.class), params,
				AccessToken.class, requestOption);
	}

	protected static String classURL(Class<?> clazz) {
		return "https://connect.stripe.com/oauth/token";
	}

}
