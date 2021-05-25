package com.tcl.dias.oms.izopc.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.Quote;

/**
 * This file contains the QuoteBean.java class for quote specific data
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class QuoteBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer quoteId;

	private String quoteCode;

	private Integer createdBy;

	private Date createdTime;

	private Date effectiveDate;

	private Byte status;

	private Integer termInMonths;

	private Integer version;

	private Integer customerId;

	private String publicIp;

	private Boolean isDocusign;

	private Boolean isManualCofSigned = false;

	private Set<QuoteToLeBean> legalEntities;

	private String customerName;

	private String quoteType;

	private String serviceId;

	private String quoteCategory;

	private Integer serviceOrderId;

	private Boolean isMacdInitiated = false;

	private Boolean isMultiCircuit = false;

	private Boolean isMulticircuitBandwidthChangeFlag = false;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public QuoteBean() {

	}

	public QuoteBean(Quote quote) {
		if (quote != null) {
			this.quoteId = quote.getId();
			this.createdBy = quote.getCreatedBy();
			this.createdTime = quote.getCreatedTime();
			this.status = quote.getStatus();
			this.termInMonths = quote.getTermInMonths();
			this.quoteCode = quote.getQuoteCode();
			this.setLegalEntities(quote.getQuoteToLes() != null
					? quote.getQuoteToLes().stream().map(QuoteToLeBean::new).collect(Collectors.toSet())
					: null);

		}
	}

	/**
	 * @return the quoteId
	 */
	public Integer getQuoteId() {
		return quoteId;
	}

	/**
	 * @param quoteId the quoteId to set
	 */
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * @return the termInMonths
	 */
	public Integer getTermInMonths() {
		return termInMonths;
	}

	/**
	 * @param termInMonths the termInMonths to set
	 */
	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}

	/**
	 * @return the legalEntities
	 */
	public Set<QuoteToLeBean> getLegalEntities() {
		return legalEntities;
	}

	/**
	 * @param legalEntities the legalEntities to set
	 */
	public void setLegalEntities(Set<QuoteToLeBean> legalEntities) {
		this.legalEntities = legalEntities;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public Boolean getIsManualCofSigned() {
		return isManualCofSigned;
	}

	public void setIsManualCofSigned(Boolean isManualCofSigned) {
		this.isManualCofSigned = isManualCofSigned;
	}

	public Boolean getIsDocusign() {
		return isDocusign;
	}

	public void setIsDocusign(Boolean isDocusign) {
		this.isDocusign = isDocusign;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public String getQuoteCategory() {
		return quoteCategory;
	}

	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getServiceOrderId() {
		return serviceOrderId;
	}

	public void setServiceOrderId(Integer serviceOrderId) {
		this.serviceOrderId = serviceOrderId;
	}

	public Boolean getIsMacdInitiated() {
		return isMacdInitiated;
	}

	public void setIsMacdInitiated(Boolean isMacdInitiated) {
		this.isMacdInitiated = isMacdInitiated;
	}

	public Boolean getIsMultiCircuit() {
		return isMultiCircuit;
	}

	public void setIsMultiCircuit(Boolean isMultiCircuit) {
		this.isMultiCircuit = isMultiCircuit;
	}

	public Boolean getIsMulticircuitBandwidthChangeFlag() {
		return isMulticircuitBandwidthChangeFlag;
	}

	public void setIsMulticircuitBandwidthChangeFlag(Boolean isMulticircuitBandwidthChangeFlag) {
		this.isMulticircuitBandwidthChangeFlag = isMulticircuitBandwidthChangeFlag;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "QuoteBean [quoteId=" + quoteId + ", createdBy=" + createdBy + ", createdTime=" + createdTime
				+ ", effectiveDate=" + effectiveDate + ", status=" + status + ", termInMonths=" + termInMonths
				+ ", legalEntities=" + legalEntities + "]";
	}

}