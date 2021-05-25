package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the BillingRequest.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class BillingRequest {

	private String accountId;

	private String accounCuId;

	private Integer billingContactId;

	private String legalEntityName;

	private Integer quoteLeId;

	private Integer customerLeId;

	private List<BillingAttributesBean> attributes;

	/**
	 * @return the attributes
	 */
	public List<BillingAttributesBean> getAttributes() {
		if (attributes == null) {
			attributes = new ArrayList<>();
		}

		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<BillingAttributesBean> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accounCuId
	 */
	public String getAccounCuId() {
		return accounCuId;
	}

	/**
	 * @param accounCuId
	 *            the accounCuId to set
	 */
	public void setAccounCuId(String accounCuId) {
		this.accounCuId = accounCuId;
	}

	/**
	 * @return the customerLeId
	 */
	public Integer getCustomerLeId() {
		return customerLeId;
	}

	/**
	 * @param customerLeId
	 *            the customerLeId to set
	 */
	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	/**
	 * @return the quoteLeId
	 */
	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	/**
	 * @param quoteLeId
	 *            the quoteLeId to set
	 */
	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public Integer getBillingContactId() {
		return billingContactId;
	}

	public void setBillingContactId(Integer billingContactId) {
		this.billingContactId = billingContactId;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

}
