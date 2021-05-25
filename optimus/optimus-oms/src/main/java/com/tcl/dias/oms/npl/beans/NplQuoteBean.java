package com.tcl.dias.oms.npl.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.beans.LeOwnerDetailsSfdc;
import com.tcl.dias.oms.entity.entities.Quote;

/**
 * This file contains the QuoteBean.java class for quote specific data
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class NplQuoteBean implements Serializable {
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
	
	private String quoteAccess;
	
	private String quoteCreatedUserType;
	
	private Boolean isMultiCircuit =false;

	private Boolean isMulticircuitBandwidthChangeFlag=false;
	
	private Boolean isMulticircuitShiftSiteFlag=false;
	
	private String quoteStatus;
	
    private Boolean quoteRejectionStatus =false;
	
	private String quoteRejectionComment;
	
	private Boolean isCommercialTriggered=false;
	
	private Boolean isInitialCommercialTrigger=false;
	
    private Boolean isBulkUpload=false;
    
    private Integer bulkUploadId;
    
    private String bulkUploadStatus;
	
    private Integer totalLinkCount;
    
    private String quoteMaxCount;
	
    private Boolean isSiteBilling=false;
	
	
	
	
	
	
	public Boolean getIsSiteBilling() {
		return isSiteBilling;
	}

	public void setIsSiteBilling(Boolean isSiteBilling) {
		this.isSiteBilling = isSiteBilling;
	}
	
	
	
	
	
	public String getQuoteMaxCount() {
		return quoteMaxCount;
	}

	public void setQuoteMaxCount(String quoteMaxCount) {
		this.quoteMaxCount = quoteMaxCount;
	}

	
	
	
	
	
	public String getBulkUploadStatus() {
		return bulkUploadStatus;
	}

	public void setBulkUploadStatus(String bulkUploadStatus) {
		this.bulkUploadStatus = bulkUploadStatus;
	}
	
	
	
	
	public Integer getBulkUploadId() {
		return bulkUploadId;
	}

	public void setBulkUploadId(Integer bulkUploadId) {
		this.bulkUploadId = bulkUploadId;
	}

	public Boolean getIsBulkUpload() {
		return isBulkUpload;
	}

	public void setIsBulkUpload(Boolean isBulkUpload) {
		this.isBulkUpload = isBulkUpload;
	}


	private LeOwnerDetailsSfdc leOwnerDetailsSfdc;

	public LeOwnerDetailsSfdc getLeOwnerDetailsSfdc() {
		return leOwnerDetailsSfdc;
	}

	public void setLeOwnerDetailsSfdc(LeOwnerDetailsSfdc leOwnerDetailsSfdc) {
		this.leOwnerDetailsSfdc = leOwnerDetailsSfdc;
	}
	
	private Character isCommercialChanges;
	
	
	

	public Boolean getQuoteRejectionStatus() {
		return quoteRejectionStatus;
	}

	public void setQuoteRejectionStatus(Boolean quoteRejectionStatus) {
		this.quoteRejectionStatus = quoteRejectionStatus;
	}

	public String getQuoteRejectionComment() {
		return quoteRejectionComment;
	}

	public void setQuoteRejectionComment(String quoteRejectionComment) {
		this.quoteRejectionComment = quoteRejectionComment;
	}

	public Boolean getIsCommercialTriggered() {
		return isCommercialTriggered;
	}

	public void setIsCommercialTriggered(Boolean isCommercialTriggered) {
		this.isCommercialTriggered = isCommercialTriggered;
	}

	public Boolean getIsInitialCommercialTrigger() {
		return isInitialCommercialTrigger;
	}

	public void setIsInitialCommercialTrigger(Boolean isInitialCommercialTrigger) {
		this.isInitialCommercialTrigger = isInitialCommercialTrigger;
	}

	public String getQuoteStatus() {
		return quoteStatus;
	}

	public void setQuoteStatus(String quoteStatus) {
		this.quoteStatus = quoteStatus;
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

	public Boolean getIsMulticircuitShiftSiteFlag() {
		return isMulticircuitShiftSiteFlag;
	}

	public void setIsMulticircuitShiftSiteFlag(Boolean isMulticircuitShiftSiteFlag) {
		this.isMulticircuitShiftSiteFlag = isMulticircuitShiftSiteFlag;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Boolean getIsDocusign() {
		return isDocusign;
	}

	public void setIsDocusign(Boolean isDocusign) {
		this.isDocusign = isDocusign;
	}

	public NplQuoteBean() {

	}

	public NplQuoteBean(Quote quote) {
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
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
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
	
	

	public String getQuoteAccess() {
		return quoteAccess;
	}

	public void setQuoteAccess(String quoteAccess) {
		this.quoteAccess = quoteAccess;
	}

	public String getQuoteCreatedUserType() {
		return quoteCreatedUserType;
	}

	public void setQuoteCreatedUserType(String quoteCreatedUserType) {
		this.quoteCreatedUserType = quoteCreatedUserType;
	}
	
	public Integer getTotalLinkCount() {
		return totalLinkCount;
	}

	public void setTotalLinkCount(Integer totalLinkCount) {
		this.totalLinkCount = totalLinkCount;
	}

	public Character getIsCommercialChanges() {
		return isCommercialChanges;
	}

	public void setIsCommercialChanges(Character isCommercialChanges) {
		this.isCommercialChanges = isCommercialChanges;
	}

	@Override
	public String toString() {
		return "NplQuoteBean{" +
				"quoteId=" + quoteId +
				", quoteCode='" + quoteCode + '\'' +
				", createdBy=" + createdBy +
				", createdTime=" + createdTime +
				", effectiveDate=" + effectiveDate +
				", status=" + status +
				", termInMonths=" + termInMonths +
				", version=" + version +
				", customerId=" + customerId +
				", publicIp='" + publicIp + '\'' +
				", legalEntities=" + legalEntities +
				", isDocusign=" + isDocusign +
				", customerName='" + customerName + '\'' +
				", quoteType='" + quoteType + '\'' +
				", quoteCategory='" + quoteCategory + '\'' +
				", serviceId='" + serviceId + '\'' +
				", serviceOrderId=" + serviceOrderId +
				", isMacdInitiated=" + isMacdInitiated +
				", opportunityId='" + opportunityId + '\'' +
				", siteShifted='" + siteShifted + '\'' +
				", quoteAccess='" + quoteAccess + '\'' +
				", quoteCreatedUserType='" + quoteCreatedUserType + '\'' +
				", isMultiCircuit=" + isMultiCircuit +
				", isMulticircuitBandwidthChangeFlag=" + isMulticircuitBandwidthChangeFlag +
				", isMulticircuitShiftSiteFlag=" + isMulticircuitShiftSiteFlag +
				", quoteStatus='" + quoteStatus + '\'' +
				", quoteRejectionStatus=" + quoteRejectionStatus +
				", quoteRejectionComment='" + quoteRejectionComment + '\'' +
				", isCommercialTriggered=" + isCommercialTriggered +
				", isInitialCommercialTrigger=" + isInitialCommercialTrigger +
				", leOwnerDetailsSfdc=" + leOwnerDetailsSfdc +
				'}';
	}


}