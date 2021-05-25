package com.tcl.dias.common.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file Contains customerLegal Entity information
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CustomerLeBean {

	private String legalEntityName;

	private String agreementId;

	private Integer legalEntityId;

	private String sfdcId;

	private String type;

	private  String currency;

	private Integer customerId;

	private String fySegmentation;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	private List<String> country;

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

	public String getSfdcId() {
		return sfdcId;
	}

	public void setSfdcId(String sfdcId) {
		this.sfdcId = sfdcId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getCountry() {
		return country;
	}

	public void setCountry(List<String> country) {
		this.country = country;
	}


	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getFySegmentation() {
		return fySegmentation;
	}

	public void setFySegmentation(String fySegmentation) {
		this.fySegmentation = fySegmentation;
	}


	@Override
	public String toString() {
		return "CustomerLeBean [legalEntityName=" + legalEntityName + ", agreementId=" + agreementId
				+ ", legalEntityId=" + legalEntityId + ", sfdcId=" + sfdcId + ", type=" + type + ", country=" + country
				+ "]";
	}

}
