
package com.tcl.dias.common.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * OpportunityResponseBean.class is used for sfdc
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpportunityResponseBean {

	private String status;
	private OpportunityResponse opportunity;
	private String message;
	private String customOptyId;
	private boolean isError;
	private String errorMessage;
	private String refId;
	private Integer quoteToLeId;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the opportunity
	 */
	public OpportunityResponse getOpportunity() {
		return opportunity;
	}

	/**
	 * @param opportunity
	 *            the opportunity to set
	 */
	public void setOpportunity(OpportunityResponse opportunity) {
		this.opportunity = opportunity;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the customOptyId
	 */
	public String getCustomOptyId() {
		return customOptyId;
	}

	/**
	 * @param customOptyId
	 *            the customOptyId to set
	 */
	public void setCustomOptyId(String customOptyId) {
		this.customOptyId = customOptyId;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	@Override
	public String toString() {
		return "OpportunityResponseBean{" +
				"status='" + status + '\'' +
				", opportunity=" + opportunity +
				", message='" + message + '\'' +
				", customOptyId='" + customOptyId + '\'' +
				", isError=" + isError +
				", errorMessage='" + errorMessage + '\'' +
				", refId='" + refId + '\'' +
				", quoteToLeId=" + quoteToLeId +
				'}';
	}
}
