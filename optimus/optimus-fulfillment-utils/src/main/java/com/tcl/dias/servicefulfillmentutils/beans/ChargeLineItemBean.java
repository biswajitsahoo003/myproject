package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class ChargeLineItemBean extends TaskDetailsBaseBean {

	private List<LineItemDetailsBean> lineItemDetails;
	
	private String accountNumber;

	private String delayReason;

	private String delayReasonCategory;

	private String delayReasonSubCategory;


	public List<LineItemDetailsBean> getLineItemDetails() {
		return lineItemDetails;
	}

	public void setLineItemDetails(List<LineItemDetailsBean> lineItemDetails) {
		this.lineItemDetails = lineItemDetails;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

	public String getDelayReasonCategory() {
		return delayReasonCategory;
	}

	public void setDelayReasonCategory(String delayReasonCategory) {
		this.delayReasonCategory = delayReasonCategory;
	}

	public String getDelayReasonSubCategory() {
		return delayReasonSubCategory;
	}

	public void setDelayReasonSubCategory(String delayReasonSubCategory) {
		this.delayReasonSubCategory = delayReasonSubCategory;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
}
