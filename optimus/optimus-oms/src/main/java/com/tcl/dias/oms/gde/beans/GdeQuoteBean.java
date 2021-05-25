package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GdeQuoteBean implements Serializable {

	/**
	 * 
	 */
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

	private Set<QuoteToLeBean> legalEntities;
	
	private Boolean isDocusign;
	
	private String customerName;
	
	private String quoteType;

	private String quoteCategory;

	private String serviceId;

	private Integer serviceOrderId;

	private Boolean isMacdInitiated =false;
	
	private String opportunityId;
	
	private String siteShifted;

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public Set<QuoteToLeBean> getLegalEntities() {
		return legalEntities;
	}

	public void setLegalEntities(Set<QuoteToLeBean> legalEntities) {
		this.legalEntities = legalEntities;
	}

	public Boolean getIsDocusign() {
		return isDocusign;
	}

	public void setIsDocusign(Boolean isDocusign) {
		this.isDocusign = isDocusign;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getSiteShifted() {
		return siteShifted;
	}

	public void setSiteShifted(String siteShifted) {
		this.siteShifted = siteShifted;
	}

	@Override
	public String toString() {
		return "GdeQuoteBean [quoteId=" + quoteId + ", quoteCode=" + quoteCode + ", createdBy=" + createdBy
				+ ", createdTime=" + createdTime + ", effectiveDate=" + effectiveDate + ", status=" + status
				+ ", termInMonths=" + termInMonths + ", version=" + version + ", customerId=" + customerId
				+ ", publicIp=" + publicIp + ", legalEntities=" + legalEntities + ", isDocusign=" + isDocusign
				+ ", customerName=" + customerName + ", quoteType=" + quoteType + ", quoteCategory=" + quoteCategory
				+ ", serviceId=" + serviceId + ", serviceOrderId=" + serviceOrderId + ", isMacdInitiated="
				+ isMacdInitiated + ", opportunityId=" + opportunityId + ", siteShifted=" + siteShifted + "]";
	}
	

}
