package com.tcl.dias.l2oworkflowutils.beans;

import java.io.Serializable;
import java.util.Date;

public class MfTaskDataBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer taskId;
	private String assignedTo;
	private Date assignedOn; 
	private String feasibilityCoordinator;
	private String productType; 
	private String subject;
	private Date taskCloseDate;
	private String taskRelatedTo;
	private String copfId;
	private Date lastModifiedBy;
	private Date creationDate;
	private String orderType;
	private String productSubType;
	private String subGroup;
	private String taskToAspIndia;
	private String taskAcknowledgedBy;
	private String feasibilityStatus;
	private String requestorComments;
	private String assignedFrom;
	private Integer siteId;
	private String siteCode;
	private String siteType;
	private String lmType;
	private String responderComments;
	private String reason;
	private String createdBy;
	private String prvComments;
	private String prvStatus;
	private String opportunityStage;
	private String opportunityAccountName;
	private String customerSegment;
	private String opportunityOwnerEmail;
	private String addressLineOne;
	private String addressLineTwo;
	private String city;
	private String state;
	private String latLong;
	private String pincode;
	private String localLoopInterface;
	private String feasibilityId;
	private String prvTaskId;
	private Double portCapacity;
	private Double localLoopCapacity;
	private String quoteStage;
	private String taskStatus;
	private String customerLat;
	private String customerLong;
	private Integer quoteId;
	private Integer quoteLeId;
	private String quoteType;
	private String quoteCategory;
	private String locality;
    private String parallelBuild;
    private String parallelRunDays;
    private Boolean is3DTask=false;
    private String mf3DSiteType;
    private String bandwidth;
    private String quoteCreatedUserType;
    private String quoteCode;
    
    // For NPL
    private String mfLinkEndType;
    private String mfLinkType;
    private Integer linkId;
    
    private String addressLineOneSiteA;
  	private String addressLineTwoSiteA;
  	private String citySiteA;
  	private String stateSiteA;
  	private String latLongSiteA;
  	private String pincodeSiteA;
  	private String countrySiteA;
  	private String localitySiteA;
  	
     private String addressLineOneSiteB;
  	private String addressLineTwoSiteB;
  	private String citySiteB;
  	private String stateSiteB;
  	private String latLongSiteB;
  	private String pincodeSiteB;
  	private String countrySiteB;
  	private String localitySiteB;
  	
  	private String customerLatA;
    private String customerLongA;
    private String customerLatB;
    private String customerLongB;
 
 	private String aEndlocalLoopInterface;
 	private String bEndlocalLoopInterface;
 	
 	private String aEndLocalLoopBandwidth;
 	private String bEndLocalLoopBandwidth;
 	
    private String siteGettingShifted;
    
    private String aEndLMProvider;
	private String bEndLMProvider;
	
	
	public String getaEndLMProvider() {
		return aEndLMProvider;
	}
	public void setaEndLMProvider(String aEndLMProvider) {
		this.aEndLMProvider = aEndLMProvider;
	}
	public String getbEndLMProvider() {
		return bEndLMProvider;
	}
	public void setbEndLMProvider(String bEndLMProvider) {
		this.bEndLMProvider = bEndLMProvider;
	}
	
	public String getSiteGettingShifted() {
		return siteGettingShifted;
	}
	public void setSiteGettingShifted(String siteGettingShifted) {
		this.siteGettingShifted = siteGettingShifted;
	}
 	
 	public String getaEndLocalLoopBandwidth() {
 		return aEndLocalLoopBandwidth;
 	}
 	public void setaEndLocalLoopBandwidth(String aEndLocalLoopBandwidth) {
 		this.aEndLocalLoopBandwidth = aEndLocalLoopBandwidth;
 	}
 	public String getbEndLocalLoopBandwidth() {
 		return bEndLocalLoopBandwidth;
 	}
 	public void setbEndLocalLoopBandwidth(String bEndLocalLoopBandwidth) {
 		this.bEndLocalLoopBandwidth = bEndLocalLoopBandwidth;
 	}
 	public String getaEndlocalLoopInterface() {
 		return aEndlocalLoopInterface;
 	}
 	public void setaEndlocalLoopInterface(String aEndlocalLoopInterface) {
 		this.aEndlocalLoopInterface = aEndlocalLoopInterface;
 	}
 	public String getbEndlocalLoopInterface() {
 		return bEndlocalLoopInterface;
 	}
 	public void setbEndlocalLoopInterface(String bEndlocalLoopInterface) {
 		this.bEndlocalLoopInterface = bEndlocalLoopInterface;
 	}
    
	public String getCustomerLatA() {
		return customerLatA;
	}
	public void setCustomerLatA(String customerLatA) {
		this.customerLatA = customerLatA;
	}
	public String getCustomerLongA() {
		return customerLongA;
	}
	public void setCustomerLongA(String customerLongA) {
		this.customerLongA = customerLongA;
	}
	public String getCustomerLatB() {
		return customerLatB;
	}
	public void setCustomerLatB(String customerLatB) {
		this.customerLatB = customerLatB;
	}
	public String getCustomerLongB() {
		return customerLongB;
	}
	public void setCustomerLongB(String customerLongB) {
		this.customerLongB = customerLongB;
	}
	
	public String getAddressLineOneSiteA() {
		return addressLineOneSiteA;
	}
	public void setAddressLineOneSiteA(String addressLineOneSiteA) {
		this.addressLineOneSiteA = addressLineOneSiteA;
	}
	public String getAddressLineTwoSiteA() {
		return addressLineTwoSiteA;
	}
	public void setAddressLineTwoSiteA(String addressLineTwoSiteA) {
		this.addressLineTwoSiteA = addressLineTwoSiteA;
	}
	public String getCitySiteA() {
		return citySiteA;
	}
	public void setCitySiteA(String citySiteA) {
		this.citySiteA = citySiteA;
	}
	public String getStateSiteA() {
		return stateSiteA;
	}
	public void setStateSiteA(String stateSiteA) {
		this.stateSiteA = stateSiteA;
	}
	public String getLatLongSiteA() {
		return latLongSiteA;
	}
	public void setLatLongSiteA(String latLongSiteA) {
		this.latLongSiteA = latLongSiteA;
	}
	public String getPincodeSiteA() {
		return pincodeSiteA;
	}
	public void setPincodeSiteA(String pincodeSiteA) {
		this.pincodeSiteA = pincodeSiteA;
	}
	public String getCountrySiteA() {
		return countrySiteA;
	}
	public void setCountrySiteA(String countrySiteA) {
		this.countrySiteA = countrySiteA;
	}
	public String getLocalitySiteA() {
		return localitySiteA;
	}
	public void setLocalitySiteA(String localitySiteA) {
		this.localitySiteA = localitySiteA;
	}
	public String getAddressLineOneSiteB() {
		return addressLineOneSiteB;
	}
	public void setAddressLineOneSiteB(String addressLineOneSiteB) {
		this.addressLineOneSiteB = addressLineOneSiteB;
	}
	public String getAddressLineTwoSiteB() {
		return addressLineTwoSiteB;
	}
	public void setAddressLineTwoSiteB(String addressLineTwoSiteB) {
		this.addressLineTwoSiteB = addressLineTwoSiteB;
	}
	public String getCitySiteB() {
		return citySiteB;
	}
	public void setCitySiteB(String citySiteB) {
		this.citySiteB = citySiteB;
	}
	public String getStateSiteB() {
		return stateSiteB;
	}
	public void setStateSiteB(String stateSiteB) {
		this.stateSiteB = stateSiteB;
	}
	public String getLatLongSiteB() {
		return latLongSiteB;
	}
	public void setLatLongSiteB(String latLongSiteB) {
		this.latLongSiteB = latLongSiteB;
	}
	public String getPincodeSiteB() {
		return pincodeSiteB;
	}
	public void setPincodeSiteB(String pincodeSiteB) {
		this.pincodeSiteB = pincodeSiteB;
	}
	public String getCountrySiteB() {
		return countrySiteB;
	}
	public void setCountrySiteB(String countrySiteB) {
		this.countrySiteB = countrySiteB;
	}
	public String getLocalitySiteB() {
		return localitySiteB;
	}
	public void setLocalitySiteB(String localitySiteB) {
		this.localitySiteB = localitySiteB;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public String getMfLinkEndType() {
		return mfLinkEndType;
	}
	public void setMfLinkEndType(String mfLinkEndType) {
		this.mfLinkEndType = mfLinkEndType;
	}
	public String getMfLinkType() {
		return mfLinkType;
	}
	public void setMfLinkType(String mfLinkType) {
		this.mfLinkType = mfLinkType;
	}
	
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getMf3DSiteType() {
		return mf3DSiteType;
	}
	public void setMf3DSiteType(String mf3dSiteType) {
		mf3DSiteType = mf3dSiteType;
	}
	public Boolean getIs3DTask() {
		return is3DTask;
	}
	public void setIs3DTask(Boolean is3dTask) {
		is3DTask = is3dTask;
	}
	public String getParallelBuild() {
		return parallelBuild;
	}
	public void setParallelBuild(String parallelBuild) {
		this.parallelBuild = parallelBuild;
	}
	public String getParallelRunDays() {
		return parallelRunDays;
	}
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public Date getAssignedOn() {
		return assignedOn;
	}
	public void setAssignedOn(Date assignedOn) {
		this.assignedOn = assignedOn;
	}
	public String getFeasibilityCoordinator() {
		return feasibilityCoordinator;
	}
	public void setFeasibilityCoordinator(String feasibilityCoordinator) {
		this.feasibilityCoordinator = feasibilityCoordinator;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getTaskCloseDate() {
		return taskCloseDate;
	}
	public void setTaskCloseDate(Date taskCloseDate) {
		this.taskCloseDate = taskCloseDate;
	}
	public String getTaskRelatedTo() {
		return taskRelatedTo;
	}
	public void setTaskRelatedTo(String taskRelatedTo) {
		this.taskRelatedTo = taskRelatedTo;
	}
	public String getCopfId() {
		return copfId;
	}
	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}
	public Date getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(Date lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getProductSubType() {
		return productSubType;
	}
	public void setProductSubType(String productSubType) {
		this.productSubType = productSubType;
	}
	public String getSubGroup() {
		return subGroup;
	}
	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}
	public String getTaskToAspIndia() {
		return taskToAspIndia;
	}
	public void setTaskToAspIndia(String taskToAspIndia) {
		this.taskToAspIndia = taskToAspIndia;
	}
	public String getTaskAcknowledgedBy() {
		return taskAcknowledgedBy;
	}
	public void setTaskAcknowledgedBy(String taskAcknowledgedBy) {
		this.taskAcknowledgedBy = taskAcknowledgedBy;
	}
	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}
	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}
	public String getRequestorComments() {
		return requestorComments;
	}
	public void setRequestorComments(String requestorComments) {
		this.requestorComments = requestorComments;
	}
	public String getAssignedFrom() {
		return assignedFrom;
	}
	public void setAssignedFrom(String assignedFrom) {
		this.assignedFrom = assignedFrom;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getResponderComments() {
		return responderComments;
	}
	public void setResponderComments(String responderComments) {
		this.responderComments = responderComments;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getPrvComments() {
		return prvComments;
	}
	public void setPrvComments(String prvComments) {
		this.prvComments = prvComments;
	}
	public String getPrvStatus() {
		return prvStatus;
	}
	public void setPrvStatus(String prvStatus) {
		this.prvStatus = prvStatus;
	}
	public String getOpportunityStage() {
		return opportunityStage;
	}
	public void setOpportunityStage(String opportunityStage) {
		this.opportunityStage = opportunityStage;
	}
	public String getOpportunityAccountName() {
		return opportunityAccountName;
	}
	public void setOpportunityAccountName(String opportunityAccountName) {
		this.opportunityAccountName = opportunityAccountName;
	}
	public String getCustomerSegment() {
		return customerSegment;
	}
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}
	public String getOpportunityOwnerEmail() {
		return opportunityOwnerEmail;
	}
	public void setOpportunityOwnerEmail(String opportunityOwnerEmail) {
		this.opportunityOwnerEmail = opportunityOwnerEmail;
	}
	public String getAddressLineOne() {
		return addressLineOne;
	}
	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}
	public String getAddressLineTwo() {
		return addressLineTwo;
	}
	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLatLong() {
		return latLong;
	}
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getLocalLoopInterface() {
		return localLoopInterface;
	}
	public void setLocalLoopInterface(String localLoopInterface) {
		this.localLoopInterface = localLoopInterface;
	}
	public String getFeasibilityId() {
		return feasibilityId;
	}
	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}
	public String getPrvTaskId() {
		return prvTaskId;
	}
	public void setPrvTaskId(String prvTaskId) {
		this.prvTaskId = prvTaskId;
	}
	public Double getPortCapacity() {
		return portCapacity;
	}
	public void setPortCapacity(Double portCapacity) {
		this.portCapacity = portCapacity;
	}
	public Double getLocalLoopCapacity() {
		return localLoopCapacity;
	}
	public void setLocalLoopCapacity(Double localLoopCapacity) {
		this.localLoopCapacity = localLoopCapacity;
	}
	public String getQuoteStage() {
		return quoteStage;
	}
	public void setQuoteStage(String quoteStage) {
		this.quoteStage = quoteStage;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getCustomerLat() {
		return customerLat;
	}
	public void setCustomerLat(String customerLat) {
		this.customerLat = customerLat;
	}
	public String getCustomerLong() {
		return customerLong;
	}
	public void setCustomerLong(String customerLong) {
		this.customerLong = customerLong;
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
	public String getLmType() {
		return lmType;
	}
	public void setLmType(String lmType) {
		this.lmType = lmType;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
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
	public String getQuoteCreatedUserType() {
		return quoteCreatedUserType;
	}
	public void setQuoteCreatedUserType(String quoteCreatedUserType) {
		this.quoteCreatedUserType = quoteCreatedUserType;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	} 
	
	
}


