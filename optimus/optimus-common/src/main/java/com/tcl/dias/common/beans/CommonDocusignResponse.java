package com.tcl.dias.common.beans;

/**
 * This file contains the CommonDocusignRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CommonDocusignResponse {

	private Integer quoteId;
	private Integer quoteLeId;
	private String envelopeId;
	private String errorMessage;
	private String type;
	private String envelopeResponse;
	private String ip;
	private String docusignStatus;
	private String name;
	private boolean status;
	private String path;
	private String requestId;
	private String objUrl;

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public String getEnvelopeId() {
		return envelopeId;
	}

	public void setEnvelopeId(String envelopeId) {
		this.envelopeId = envelopeId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEnvelopeResponse() {
		return envelopeResponse;
	}

	public void setEnvelopeResponse(String envelopeResponse) {
		this.envelopeResponse = envelopeResponse;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDocusignStatus() {
		return docusignStatus;
	}

	public void setDocusignStatus(String docusignStatus) {
		this.docusignStatus = docusignStatus;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getObjUrl() {
		return objUrl;
	}

	public void setObjUrl(String objUrl) {
		this.objUrl = objUrl;
	}

	@Override
	public String toString() {
		return "CommonDocusignResponse [quoteId=" + quoteId + ", quoteLeId=" + quoteLeId + ", envelopeId=" + envelopeId
				+ ", errorMessage=" + errorMessage + ", type=" + type + ", envelopeResponse=" + envelopeResponse
				+ ", ip=" + ip + ", docusignStatus=" + docusignStatus + ", name=" + name + ", status=" + status
				+ ", path=" + path + ", requestId=" + requestId + ", objUrl=" + objUrl + "]";
	}

}
