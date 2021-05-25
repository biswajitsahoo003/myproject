package com.tcl.dias.ticketing.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * used to get the ServiceRequestResponse when retrieving the details of tickets
 * from service request management
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestResponse {

	@JsonProperty("result")
	private ServiceRequestResponseData result;

	private String status;

	private String message;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("result")
	public ServiceRequestResponseData getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(ServiceRequestResponseData result) {
		this.result = result;
	}

}
