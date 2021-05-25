package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This file contains the CustomerLeDetailsBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerLeDetailsBean {

	private String accountId;

	private String accounCuId;

	private String legalEntityName;

	private Integer billingContactId;

	private List<Attributes> attributes;

	private Double preapprovedMrc;
	
	private Double preapprovedNrc;
	
	private String preapprovedPaymentTerm;
	
	private String preapprovedBillingMethod;
	
	private String creditCheckAccountType;
	
	private String blacklistStatus;
	
	private String creditPreapprovedFlag;

	private String billingFrequency;

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
	public List<Attributes> getAttributes() {
		if (attributes == null) {
			attributes = new ArrayList<>();
		}
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<Attributes> attributes) {
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

	public Double getPreapprovedMrc() {
		return preapprovedMrc;
	}

	public void setPreapprovedMrc(Double preapprovedMrc) {
		this.preapprovedMrc = preapprovedMrc;
	}

	public Double getPreapprovedNrc() {
		return preapprovedNrc;
	}

	public void setPreapprovedNrc(Double preapprovedNrc) {
		this.preapprovedNrc = preapprovedNrc;
	}

	public String getPreapprovedPaymentTerm() {
		return preapprovedPaymentTerm;
	}

	public void setPreapprovedPaymentTerm(String preapprovedPaymentTerm) {
		this.preapprovedPaymentTerm = preapprovedPaymentTerm;
	}

	public String getPreapprovedBillingMethod() {
		return preapprovedBillingMethod;
	}

	public void setPreapprovedBillingMethod(String preapprovedBillingMethod) {
		this.preapprovedBillingMethod = preapprovedBillingMethod;
	}

	public String getCreditCheckAccountType() {
		return creditCheckAccountType;
	}

	public void setCreditCheckAccountType(String creditCheckAccountType) {
		this.creditCheckAccountType = creditCheckAccountType;
	}

	public String getBlacklistStatus() {
		return blacklistStatus;
	}

	public void setBlacklistStatus(String blacklistStatus) {
		this.blacklistStatus = blacklistStatus;
	}

	public String getCreditPreapprovedFlag() {
		return creditPreapprovedFlag;
	}

	public void setCreditPreapprovedFlag(String creditPreapprovedFlag) {
		this.creditPreapprovedFlag = creditPreapprovedFlag;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}
}
