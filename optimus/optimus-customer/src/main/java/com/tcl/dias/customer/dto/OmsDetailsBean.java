package com.tcl.dias.customer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the OmsDetailsBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OmsDetailsBean {

	private String accountId;

	private String accounCuId;

	private String legalEntityName;

	private Integer billingContactId;

	private List<AttributesDto> attributes;

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
	 * @return the attributes
	 */
	public List<AttributesDto> getAttributes() {
		if (attributes == null) {
			attributes = new ArrayList<>();
		}
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<AttributesDto> attributes) {
		this.attributes = attributes;
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
