package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the UpdateRequest.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UpdateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1260184617121843406L;

	private Integer quoteId;
	
	private Integer orderId;
	
	private String familyName;
	
	private Integer quoteToLe;
	
	private Integer orderToLeId;

	private Integer siteId;

	private Integer localITContactId;

	private Timestamp requestorDate;

	private String attributeName;

	private String attributeValue;
	
	private String checkList;
	
	private String serviceId;
	
	private String bandwidthEditted;

	private List<ComponentDetail> componentDetails;
	
	private List<AttributeDetail> attributeDetails;
	
	private String termInMonths;
	
	private String primaryOrSecondaryOrBoth;
	
	private Byte isTaxExempted;
	
	private List<Integer> siteIdList;
	
	private Integer linkId;
	
	private String isBandwidthChanged;
	
	private String isSiteShift;
	
	private Byte isMulticircuit;
	
	private String linkCode;
	
	private Boolean isNoOfMultiVrfChanged = false;
	
	private String userName;	

	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}

	public String getIsBandwidthChanged() {
		return isBandwidthChanged;
	}

	public void setIsBandwidthChanged(String isBandwidthChanged) {
		this.isBandwidthChanged = isBandwidthChanged;
	}

	public String getIsSiteShift() {
		return isSiteShift;
	}

	public void setIsSiteShift(String isSiteShift) {
		this.isSiteShift = isSiteShift;
	}

	public Byte getIsMulticircuit() {
		return isMulticircuit;
	}

	public void setIsMulticircuit(Byte isMulticircuit) {
		this.isMulticircuit = isMulticircuit;
	}

	public String getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(String termInMonths) {
		this.termInMonths = termInMonths;
	}

	/**
	 * @return the componentDetails
	 */
	public List<ComponentDetail> getComponentDetails() {
		if (componentDetails == null) {
			componentDetails = new ArrayList<>();
		}
		return componentDetails;
	}

	/**
	 * @param componentDetails
	 *            the componentDetails to set
	 */
	public void setComponentDetails(List<ComponentDetail> componentDetails) {
		this.componentDetails = componentDetails;
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
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 *            the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the requestorDate
	 */
	public Timestamp getRequestorDate() {
		return requestorDate;
	}

	/**
	 * @param requestorDate
	 *            the requestorDate to set
	 */
	public void setRequestorDate(Timestamp requestorDate) {
		this.requestorDate = requestorDate;
	}

	/**
	 * @return the localITContactId
	 */
	public Integer getLocalITContactId() {
		return localITContactId;
	}

	/**
	 * @param localITContactId
	 *            the localITContactId to set
	 */
	public void setLocalITContactId(Integer localITContactId) {
		this.localITContactId = localITContactId;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName
	 *            the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return the attributeValue
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * @param attributeValue
	 *            the attributeValue to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	/**
	 * @return the quoteToLe
	 */
	public Integer getQuoteToLe() {
		return quoteToLe;
	}

	/**
	 * @param quoteToLe the quoteToLe to set
	 */
	public void setQuoteToLe(Integer quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

	/**
	 * @return the orderToLeId
	 */
	public Integer getOrderToLeId() {
		return orderToLeId;
	}

	/**
	 * @param orderToLeId the orderToLeId to set
	 */
	public void setOrderToLeId(Integer orderToLeId) {
		this.orderToLeId = orderToLeId;
	}

	/**
	 * 
	 * @return checkList
	 */
	public String getCheckList() {
		return checkList;
	}

	/**
	 * 
	 * @param checkList Checklist to set
	 */
	public void setCheckList(String checkList) {
		this.checkList = checkList;
	}

	/**
	 * 
	 * getAttributeDetails
	 * @return the attributeDetailsList
	 */
	public List<AttributeDetail> getAttributeDetails() {
		return attributeDetails;
	}
	
	/**
	 * 
	 * setAttributeDetails the attributeDetails to set
	 * @param attributeDetails
	 */

	public void setAttributeDetails(List<AttributeDetail> attributeDetails) {
		this.attributeDetails = attributeDetails;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the orderId
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the primaryOrSecondaryOrBoth
	 */
	public String getPrimaryOrSecondaryOrBoth() {
		return primaryOrSecondaryOrBoth;
	}

	/**
	 * @param primaryOrSecondaryOrBoth the primaryOrSecondaryOrBoth to set
	 */
	public void setPrimaryOrSecondaryOrBoth(String primaryOrSecondaryOrBoth) {
		this.primaryOrSecondaryOrBoth = primaryOrSecondaryOrBoth;
	}

	/**
	 * @return the isTaxExempted
	 */
	public Byte getIsTaxExempted() {
		return isTaxExempted;
	}

	/**
	 * @param isTaxExempted the isTaxExempted to set
	 */
	public void setIsTaxExempted(Byte isTaxExempted) {
		this.isTaxExempted = isTaxExempted;
	}
	

	public String getBandwidthEditted() {
		return bandwidthEditted;
	}

	public void setBandwidthEditted(String bandwidthEditted) {
		this.bandwidthEditted = bandwidthEditted;
	}
	
	

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public Boolean getIsNoOfMultiVrfChanged() {
		return isNoOfMultiVrfChanged;
	}

	public void setIsNoOfMultiVrfChanged(Boolean isNoOfMultiVrfChanged) {
		this.isNoOfMultiVrfChanged = isNoOfMultiVrfChanged;
	}

	@Override
	public String toString() {
		return "UpdateRequest [quoteId=" + quoteId + ", orderId=" + orderId + ", familyName=" + familyName
				+ ", quoteToLe=" + quoteToLe + ", orderToLeId=" + orderToLeId + ", siteId=" + siteId
				+ ", localITContactId=" + localITContactId + ", requestorDate=" + requestorDate + ", attributeName="
				+ attributeName + ", attributeValue=" + attributeValue + ", checkList=" + checkList + ", serviceId="
				+ serviceId + ", componentDetails=" + componentDetails + ", attributeDetails=" + attributeDetails
				+ ", termInMonths=" + termInMonths + ", primaryOrSecondaryOrBoth=" + primaryOrSecondaryOrBoth
				+ ", isTaxExempted=" + isTaxExempted
				+ ", bandwidthEditted=" + bandwidthEditted 
				+ ", linkId=" + linkId +"]";
	}

	public List<Integer> getSiteIdList() {
		return siteIdList;
	}

	public void setSiteIdList(List<Integer> siteIdList) {
		this.siteIdList = siteIdList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
