package com.tcl.dias.oms.webex.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AuthBean holds the authorization token for CCW Quote API
 * 
 * @author srraghav
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthBean {

	@JsonProperty
	private String access_token;

	@JsonProperty
	private String token_type;

	@JsonProperty
	private String expires_in;

	public String getAccessToken() {
		return access_token;
	}

	public void setAccessToken(String access_token) {
		this.access_token = access_token;
	}

	public String getTokenType() {
		return token_type;
	}

	public void setTokenType(String token_type) {
		this.token_type = token_type;
	}

	public String getExpiresIn() {
		return expires_in;
	}

	public void setExpiresIn(String expires_in) {
		this.expires_in = expires_in;
	}

}