/**
 * 
 */
package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * this takes the customer request.
 * 
 * @author KusumaK
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */
@JsonInclude(Include.NON_NULL)
public class CustomerRequestBean {

	private String subject;
	private String message;
	private String requestedTime;
	
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
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
	/**
	 * @return the requestedTime
	 */
	public String getRequestedTime() {
		return requestedTime;
	}
	/**
	 * @param requestedTime the requestedTime to set
	 */
	public void setRequestedTime(String requestedTime) {
		this.requestedTime = requestedTime;
	}
	
	
}
