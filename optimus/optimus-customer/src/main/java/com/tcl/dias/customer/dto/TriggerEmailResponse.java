package com.tcl.dias.customer.dto;

/**
 * 
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
 
/**
 * This file contains the Trigger Email Response
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TriggerEmailResponse {

	public TriggerEmailResponse(String message) {
		this.message = message;
	}

	public TriggerEmailResponse() {
	}

	private String message;
	
	/**
	 * the message to get
	 * 
	 * @return the message
	 */ 

	public String getMessage() {
		return message;
	}

	

	/**
	 * the message to set
	 * 
	 * @param message
	 */ 
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "TriggerEmailResponse [message=" + message + "]";
	}

}
