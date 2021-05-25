package com.tcl.dias.common.beans;

/**
 * Class of AuditRequest Bean
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class AuditRequestBean {

	private Integer id;
	private String mdcToken;
	private String quoteCode;
	private String stage;
	private String fromValue;
	private String toValue;
	private String comments;
	private String createdBy;
	private Integer response;
	private String httpMethod;
	private String requestUrl;
	private String roundTripTime;
	private String sessionState;
	private String payloadRequest;
	private String payloadResponse;

	public AuditRequestBean() {

	}

	public AuditRequestBean(String mdcToken, String quoteCode, String stage, String toValue, String comments,
			String createdBy, Integer response, String fromValue, String httpMethod, String requestUrl,
			String roundTripTime, String sessionState, String payloadRequest, String payloadResponse) {
		this.mdcToken = mdcToken;
		this.quoteCode = quoteCode;
		this.stage = stage;
		this.toValue = toValue;
		this.comments = comments;
		this.createdBy = createdBy;
		this.response = response;
		this.fromValue = fromValue;
		this.httpMethod = httpMethod;
		this.requestUrl = requestUrl;
		this.roundTripTime = roundTripTime;
		this.sessionState = sessionState;
		this.payloadRequest = payloadRequest;
		this.payloadResponse = payloadResponse;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMdcToken() {
		return mdcToken;
	}

	public void setMdcToken(String mdcToken) {
		this.mdcToken = mdcToken;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getResponse() {
		return response;
	}

	public void setResponse(Integer response) {
		this.response = response;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRoundTripTime() {
		return roundTripTime;
	}

	public void setRoundTripTime(String roundTripTime) {
		this.roundTripTime = roundTripTime;
	}

	public String getSessionState() {
		return sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

	public String getPayloadRequest() {
		return payloadRequest;
	}

	public void setPayloadRequest(String payloadRequest) {
		this.payloadRequest = payloadRequest;
	}

	public String getPayloadResponse() {
		return payloadResponse;
	}

	public void setPayloadResponse(String payloadResponse) {
		this.payloadResponse = payloadResponse;
	}

	@Override
	public String toString() {
		return "AuditRequestBean [id=" + id + ", mdcToken=" + mdcToken + ", quoteCode=" + quoteCode + ", stage=" + stage
				+ ", fromValue=" + fromValue + ", toValue=" + toValue + ", comments=" + comments + ", createdBy="
				+ createdBy + ", response=" + response + ", httpMethod=" + httpMethod + ", requestUrl=" + requestUrl
				+ ", roundTripTime=" + roundTripTime + ", sessionState=" + sessionState + ", payloadRequest="
				+ payloadRequest + ", payloadResponse=" + payloadResponse + "]";
	}

}
