package com.tcl.dias.common.beans;

import java.util.Date;
import java.util.List;
public class MfDetailsBean {

	private Integer id;
	private Integer isActive;
	private MfDetailAttributes mfDetails;
	private String quoteCode;
	private Integer quoteId;
	private Integer quoteLeId;
	private String siteCode;
	private Integer siteId;
	private String status;
	private String updatedBy;
	private Date updatedTime;
	private String region;
	private String assignedTo;
	private String createdBy;
	private Date createdTime;
	private String productName;
	private String siteType;
    private String createdByEmail;
    private Boolean is3DMaps=false;
    private String mf3DMapSiteType;
    private String quoteCreatedUserType;

    private String aEndPredictedAcessFeasibility;
	private String bEndPredictedAcessFeasibility;
	private String systemLinkResponse;
	private boolean retriggerTaskForFeasibleSites;

	// PIPF -55
	public boolean isRetriggerTaskForFeasibleSites() {
		return retriggerTaskForFeasibleSites;
	}

	public void setRetriggerTaskForFeasibleSites(boolean retriggerTaskForFeasibleSites) {
		this.retriggerTaskForFeasibleSites = retriggerTaskForFeasibleSites;
	}
		
	
	public String getSystemLinkResponse() {
		return systemLinkResponse;
	}
	public void setSystemLinkResponse(String systemLinkResponse) {
		this.systemLinkResponse = systemLinkResponse;
	}
	private Integer linkId;
	private String linkedJsonString;
	
	private boolean otherEndTaskAvailable;
	
	public boolean isOtherEndTaskAvailable() {
		return otherEndTaskAvailable;
	}
	public void setOtherEndTaskAvailable(boolean otherEndTaskAvailable) {
		this.otherEndTaskAvailable = otherEndTaskAvailable;
	}
	public String getLinkedJsonString() {
		return linkedJsonString;
	}
	public void setLinkedJsonString(String linkedJsonString) {
		this.linkedJsonString = linkedJsonString;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	private boolean systemLinkAendNeeded;
	
	public boolean isSystemLinkAendNeeded() {
		return systemLinkAendNeeded;
	}
	public void setSystemLinkAendNeeded(boolean systemLinkAendNeeded) {
		this.systemLinkAendNeeded = systemLinkAendNeeded;
	}
	private boolean systemLinkBendNeeded;

	
	
	
	public boolean isSystemLinkBendNeeded() {
		return systemLinkBendNeeded;
	}
	public void setSystemLinkBendNeeded(boolean systemLinkBendNeeded) {
		this.systemLinkBendNeeded = systemLinkBendNeeded;
	}
	
	public String getaEndPredictedAcessFeasibility() {
		return aEndPredictedAcessFeasibility;
	}
	public void setaEndPredictedAcessFeasibility(String aEndPredictedAcessFeasibility) {
		this.aEndPredictedAcessFeasibility = aEndPredictedAcessFeasibility;
	}
	public String getbEndPredictedAcessFeasibility() {
		return bEndPredictedAcessFeasibility;
	}
	public void setbEndPredictedAcessFeasibility(String bEndPredictedAcessFeasibility) {
		this.bEndPredictedAcessFeasibility = bEndPredictedAcessFeasibility;
	}
   

    private List<String> lmTypes;
	
	public List<String> getLmTypes() {
		return lmTypes;
	}
	public void setLmTypes(List<String> lmTypes) {
		this.lmTypes = lmTypes;
	}
	public String getMf3DMapSiteType() {
		return mf3DMapSiteType;
	}
	public void setMf3DMapSiteType(String mf3dMapSiteType) {
		mf3DMapSiteType = mf3dMapSiteType;
	}
	public Boolean getIs3DMaps() {
		return is3DMaps;
	}
	public void setIs3DMaps(Boolean is3dMaps) {
		is3DMaps = is3dMaps;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}
	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	public MfDetailAttributes getMfDetails() {
		return mfDetails;
	}
	public void setMfDetails(MfDetailAttributes mfDetails) {
		this.mfDetails = mfDetails;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public Integer getQuoteLeId() {
		return quoteLeId;
	}
	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getQuoteCreatedUserType() {
		return quoteCreatedUserType;
	}
	public void setQuoteCreatedUserType(String quoteCreatedUserType) {
		this.quoteCreatedUserType = quoteCreatedUserType;
	}
	
	
	
	
}
