package com.tcl.dias.oms.beans;

import javax.validation.constraints.NotNull;

import com.tcl.dias.common.utils.Constants;

/**
 * 
 * @author Manojkumar R
 *
 */
public class TriggerEmailResponse {

	public TriggerEmailResponse(String message) {
		this.message = message;
	}

	@NotNull(message = Constants.ENTITY_NAME_NOT_FOUND)
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
