package com.tcl.dias.common.gsc.beans;

/**
 * Bean to send tiger notification on failure
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class EnterpriseTigerNotificationBean {

	private Integer id;
	private String requestUrl;
	private String requestBody;
	private String requestResponse;
	private String status;
	private Integer referenceId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getRequestResponse() {
		return requestResponse;
	}

	public void setRequestResponse(String requestResponse) {
		this.requestResponse = requestResponse;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

}
