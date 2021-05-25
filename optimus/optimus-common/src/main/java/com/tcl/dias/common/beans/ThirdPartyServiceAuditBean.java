package com.tcl.dias.common.beans;

import java.util.Date;

/**
 * This file contains the ThirdPartyServiceAuditBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ThirdPartyServiceAuditBean {

	private String httpMethod;
	private String requestPayload;
	private String responsePayload;
	private String requestHeader;
	private String statusCode;
	private String mdcToken;
	private Date createdTime;
	private String createdBy;
	private String sessionStateId;
	private String requestUrl;

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public String getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(String responsePayload) {
		this.responsePayload = responsePayload;
	}

	public String getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(String requestHeader) {
		this.requestHeader = requestHeader;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMdcToken() {
		return mdcToken;
	}

	public void setMdcToken(String mdcToken) {
		this.mdcToken = mdcToken;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getSessionStateId() {
		return sessionStateId;
	}

	public void setSessionStateId(String sessionStateId) {
		this.sessionStateId = sessionStateId;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
	public String toString() {
		return "ThirdPartyServiceAuditBean [httpMethod=" + httpMethod + ", requestPayload=" + requestPayload
				+ ", responsePayload=" + responsePayload + ", requestHeader=" + requestHeader + ", statusCode="
				+ statusCode + ", mdcToken=" + mdcToken + ", createdTime=" + createdTime + ", createdBy=" + createdBy
				+ ", sessionStateId=" + sessionStateId + ", requestUrl=" + requestUrl + "]";
	}

}
