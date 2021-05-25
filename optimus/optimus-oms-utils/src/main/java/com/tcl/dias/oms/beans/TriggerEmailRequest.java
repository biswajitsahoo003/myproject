package com.tcl.dias.oms.beans;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.tcl.dias.common.utils.Constants;

/**
 * This java class is used to send in the request to Trigger Email
 * 
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TriggerEmailRequest {

	@NotNull(message = Constants.ENTITY_NAME_NOT_FOUND)
	@Email
	private String emailId;
	@NotNull(message = Constants.ENTITY_NAME_NOT_FOUND)
	private Integer quoteToLeId;

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param the
	 *            emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the quoteToLeId
	 */
	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	/**
	 * @param quoteToLeId
	 *            the quoteToLeId to set
	 */
	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	@Override
	public String toString() {
		return "TriggerEmailRequest [emailId=" + emailId + "quoteToLeId=" + quoteToLeId + "]";
	}

}
