package com.tcl.dias.nso.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the CreateDocumentDto.java class. Dto Class for create
 * document
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CreateDocumentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer quoteId;
	
	private Integer quoteLeId;

	private Integer customerLegalEntityId;

	private Integer customerId;

	private Integer supplierLegalEntityId;

	private List<Integer> illSitesIds;
	
	private String familyName;
	
	private Integer illsiteId;
	
	private String isTaxExemption;
	
	private String creditCheckStatus;
	
	private Boolean preapprovedFlag;

	/**
	 * @return the quoteId
	 */
	public Integer getQuoteId() {
		return quoteId;
	}

	/**
	 * @param quoteId
	 *            the quoteId to set
	 */
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * @return the customerLegalEntityId
	 */
	public Integer getCustomerLegalEntityId() {
		return customerLegalEntityId;
	}

	/**
	 * @param customerLegalEntityId
	 *            the customerLegalEntityId to set
	 */
	public void setCustomerLegalEntityId(Integer customerLegalEntityId) {
		this.customerLegalEntityId = customerLegalEntityId;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the supplierLegalEntityId
	 */
	public Integer getSupplierLegalEntityId() {
		return supplierLegalEntityId;
	}

	/**
	 * @param supplierLegalEntityId
	 *            the supplierLegalEntityId to set
	 */
	public void setSupplierLegalEntityId(Integer supplierLegalEntityId) {
		this.supplierLegalEntityId = supplierLegalEntityId;
	}

	/**
	 * @return the illSitesIds
	 */
	public List<Integer> getIllSitesIds() {
		return illSitesIds;
	}

	/**
	 * @param illSitesIds
	 *            the illSitesIds to set
	 */
	public void setIllSitesIds(List<Integer> illSitesIds) {
		this.illSitesIds = illSitesIds;
	}

	/**
	 * @return the illsiteId
	 */
	public Integer getIllsiteId() {
		return illsiteId;
	}

	/**
	 * @param illsiteId the illsiteId to set
	 */
	public void setIllsiteId(Integer illsiteId) {
		this.illsiteId = illsiteId;
	}
	
	

	/**
	 * @return the quoteLeId
	 */
	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	/**
	 * @param quoteLeId the quoteLeId to set
	 */
	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	
	
	/**
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getIsTaxExemption() {
		return isTaxExemption;
	}

	public void setIsTaxExemption(String isTaxExemption) {
		this.isTaxExemption = isTaxExemption;
	}
	
	

	public String getCreditCheckStatus() {
		return creditCheckStatus;
	}

	public void setCreditCheckStatus(String creditCheckStatus) {
		this.creditCheckStatus = creditCheckStatus;
	}
	
	

	public Boolean getPreapprovedFlag() {
		return preapprovedFlag;
	}

	public void setPreapprovedFlag(Boolean preapprovedFlag) {
		this.preapprovedFlag = preapprovedFlag;
	}

	/**
	 * toString
	 * @return
	 */
	@Override
	public String toString() {
		return "CreateDocumentDto [quoteId=" + quoteId + ", customerLegalEntityId=" + customerLegalEntityId
				+ ", customerId=" + customerId + ", supplierLegalEntityId=" + supplierLegalEntityId + ", illSitesIds="
				+ illSitesIds + ", illsiteId=" + illsiteId + ", isTaxExemption=" + isTaxExemption +  ", creditCheckStatus=" + creditCheckStatus +  
				", preapprovedFlag=" + preapprovedFlag +"]";
	}
	
	

}
