package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file Contains customerLegal Entity information
 * 
 *
 * @author MBEDI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CustomerLeVO {

	private String accountId;

	private String legalEntityName;

	private String agreementId;

	private Integer legalEntityId;

	private String sfdcCuId;

	private Double preapprovedMrc;
	
	private Double preapprovedNrc;
	
	private String preapprovedPaymentTerm;
	
	private String preapprovedBillingMethod;
	
	private String creditCheckAccountType;
	
	private String blacklistStatus;

	private Integer billingContactId;

	private List<Attributes> attributes;

	private Boolean preapprovedOpportunityFlag;

	private String tpsSfdcStatusCreditControl;
	
	private String creditPreapprovedFlag;
	

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
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

	public String getSfdcCuId() {
		return sfdcCuId;
	}

	public void setSfdcCuId(String sfdcCuId) {
		this.sfdcCuId = sfdcCuId;
	}


	public Boolean getPreapprovedOpportunityFlag() {
		return preapprovedOpportunityFlag;
	}

	public void setPreapprovedOpportunityFlag(Boolean preapprovedOpportunityFlag) {
		this.preapprovedOpportunityFlag = preapprovedOpportunityFlag;
	}


	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	public String getTpsSfdcStatusCreditControl() {
		return tpsSfdcStatusCreditControl;
	}

	public void setTpsSfdcStatusCreditControl(String tpsSfdcStatusCreditControl) {
		this.tpsSfdcStatusCreditControl = tpsSfdcStatusCreditControl;
	}

	public String getCreditPreapprovedFlag() {
		return creditPreapprovedFlag;
	}

	public void setCreditPreapprovedFlag(String creditPreapprovedFlag) {
		this.creditPreapprovedFlag = creditPreapprovedFlag;
	}

	
	
	


}
