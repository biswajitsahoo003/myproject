package com.tcl.dias.audit.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Audit is the class of audit entity
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "audit")
@NamedQuery(name = "Audit.findAll", query = "SELECT a FROM Audit a")
public class Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "mdc_token")
	private String mdcToken;

	@Column(name = "quote_code")
	private String quoteCode;

	@Column(name = "stage")
	private String stage;

	@Column(name = "session_state_id")
	private String sessionStateId;

	@Lob
	@Column(name = "from_value")
	private String fromValue;

	@Lob
	@Column(name = "to_value")
	private String toValue;

	@Lob
	@Column(name = "request_payload")
	private String requestPayload;

	@Lob
	@Column(name = "response_payload")
	private String responsePayload;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "comments")
	private String comments;

	@Column(name = "response")
	private Integer response;

	@Column(name = "request_url")
	private String requestUrl;

	@Column(name = "http_method")
	private String httpMethod;

	@Column(name = "round_trip_time")
	private String roundTripTime;

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

	public String getSessionStateId() {
		return sessionStateId;
	}

	public void setSessionStateId(String sessionStateId) {
		this.sessionStateId = sessionStateId;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getResponse() {
		return response;
	}

	public void setResponse(Integer response) {
		this.response = response;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getRoundTripTime() {
		return roundTripTime;
	}

	public void setRoundTripTime(String roundTripTime) {
		this.roundTripTime = roundTripTime;
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

}
