package com.tcl.dias.common.redis.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Token authentication pojo to maintain token and expiry details
 * 
 * @author Manojkumar R
 *
 */
public class AuthTokenDetail implements Serializable {

	private static final long serialVersionUID = -1387073844205710156L;
	private String primaryToken;
	private Timestamp tokenTimestamp;
	private String secondaryToken;
	private Timestamp secondaryTokenTimestamp;
	private String xForwardedIp;
	private String userAgent;
	private String previousToken;

	public String getPrimaryToken() {
		return primaryToken;
	}

	public void setPrimaryToken(String primaryToken) {
		this.primaryToken = primaryToken;
	}

	public Timestamp getTokenTimestamp() {
		return tokenTimestamp;
	}

	public void setTokenTimestamp(Timestamp tokenTimestamp) {
		this.tokenTimestamp = tokenTimestamp;
	}

	public String getSecondaryToken() {
		return secondaryToken;
	}

	public void setSecondaryToken(String secondaryToken) {
		this.secondaryToken = secondaryToken;
	}

	public Timestamp getSecondaryTokenTimestamp() {
		return secondaryTokenTimestamp;
	}

	public void setSecondaryTokenTimestamp(Timestamp secondaryTokenTimestamp) {
		this.secondaryTokenTimestamp = secondaryTokenTimestamp;
	}

	public String getxForwardedIp() {
		return xForwardedIp;
	}

	public void setxForwardedIp(String xForwardedIp) {
		this.xForwardedIp = xForwardedIp;
	}

	public String getPreviousToken() {
		return previousToken;
	}

	public void setPreviousToken(String previousToken) {
		this.previousToken = previousToken;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "AuthTokenDetail [primaryToken=" + primaryToken + ", tokenTimestamp=" + tokenTimestamp
				+ ", secondaryToken=" + secondaryToken + ", secondaryTokenTimestamp=" + secondaryTokenTimestamp
				+ ", xForwardedIp=" + xForwardedIp + ", userAgent=" + userAgent + ", previousToken=" + previousToken
				+ "]";
	}

}
