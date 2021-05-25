/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.nso.beans.QuoteToLeBean;
import com.tcl.dias.oms.entity.entities.Quote;

/**
 * @author KarMani
 *
 */

@JsonInclude(Include.NON_NULL)
public class QuoteBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer contractTerm;

	private Integer quoteId;

	private String quoteCode;
	
	private String quoteType;
	
	private String quoteCategory;

	private String siteAlias;

	private String secondaryAlias;

	private String serviceId;

	private Boolean isMacdInitiated =false;

	private Integer serviceOrderId;
	
	private Integer createdBy;

	private Date createdTime;

	private Date effectiveDate;

	private Byte status;

	private Integer termInMonths;

	private Integer customerId;

	private String publicIp;
	
	private String customerName;
	private String portBw;
	private String opportunityId;

	private String quoteStatus;

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getPortBw() {
		return portBw;
	}

	public void setPortBw(String portBw) {
		this.portBw = portBw;
	}

	private Double oldNrc;
	private Double oldArc;

	private Boolean isDocusign;

	private Boolean isManualCofSigned = false;

	private Set<QuoteToLeBean> legalEntities;
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getQuoteStatus() { return quoteStatus; }

	public void setQuoteStatus(String quoteStatus) { this.quoteStatus = quoteStatus; }

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
			//this.quoteStatus = quote.getQuoteStatus();

		}
	}

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
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	public Double getOldNrc() {
		return oldNrc;
	}

	public void setOldNrc(Double oldNrc) {
		this.oldNrc = oldNrc;
	}

	public Double getOldArc() {
		return oldArc;
	}

	public void setOldArc(Double oldArc) {
		this.oldArc = oldArc;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
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
	 * @param createdTime
	 *            the createdTime to set
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
	 * @param effectiveDate
	 *            the effectiveDate to set
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
	 * @param status
	 *            the status to set
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
	 * @param termInMonths
	 *            the termInMonths to set
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
	 * @param legalEntities
	 *            the legalEntities to set
	 */
	public void setLegalEntities(Set<QuoteToLeBean> legalEntities) {
		this.legalEntities = legalEntities;
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


	public String getSecondaryAlias() {
		return secondaryAlias;
	}

	public void setSecondaryAlias(String secondaryAlias) {
		this.secondaryAlias = secondaryAlias;
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

	/**
	 * @return the quoteType
	 */
	public String getQuoteType() {
		return quoteType;
	}

	/**
	 * @param quoteType the quoteType to set
	 */
	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	/**
	 * @return the quoteCategory
	 */
	public String getQuoteCategory() {
		return quoteCategory;
	}

	/**
	 * @param quoteCategory the quoteCategory to set
	 */
	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	

	/**
	 * @return the serviceOrderId
	 */
	public Integer getServiceOrderId() {
		return serviceOrderId;
	}

	/**
	 * @param serviceOrderId the serviceOrderId to set
	 */
	public void setServiceOrderId(Integer serviceOrderId) {
		this.serviceOrderId = serviceOrderId;
	}


	public String getSiteAlias() {
		return siteAlias;
	}

	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}

	public Integer getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(Integer contractTerm) {
		this.contractTerm = contractTerm;
	}

	public Boolean getIsMacdInitiated() {
		return this.isMacdInitiated;
	}

	public void setIsMacdInitiated(Boolean isMacdInitiated) {
		this.isMacdInitiated = isMacdInitiated;
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