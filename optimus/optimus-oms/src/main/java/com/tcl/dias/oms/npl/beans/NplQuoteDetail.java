package com.tcl.dias.oms.npl.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.SolutionDetail;

/**
 * This file contains the QuoteRequest.java class. Bean class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class NplQuoteDetail {

	private Integer quoteId;
	private Integer orderId;
	private Integer quoteleId;
	private Integer customerId;
	private List<Integer> orderLeIds;
	private String productName;
	List<SolutionDetail> solutions;
	List<NplSite> site;
	private String isBandwidthChanged;
	private String isSiteShift;
	private Byte isMulticircuit;
	private Integer linkid;
	
	
	
	
	
	
	
	
	public Integer getLinkid() {
		return linkid;
	}

	public void setLinkid(Integer linkid) {
		this.linkid = linkid;
	}

	public Byte getIsMulticircuit() {
		return isMulticircuit;
	}

	public void setIsMulticircuit(Byte isMulticircuit) {
		this.isMulticircuit = isMulticircuit;
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

	public Integer getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(Integer linkCount) {
		this.linkCount = linkCount;
	}

	private Integer linkCount;
	@JsonIgnore
	boolean isManualFeasible;
	
	private String orderType;
	private String orderCategory;
	private String serviceId;
	private String engagementOptyId;
	private String quoteCode;
	private String classification;

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteleId() {
		return quoteleId;
	}

	public void setQuoteleId(Integer quoteleId) {
		this.quoteleId = quoteleId;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<SolutionDetail> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<SolutionDetail> solutions) {
		this.solutions = solutions;
	}

	public List<NplSite> getSite() {
		return site;
	}

	public void setSite(List<NplSite> site) {
		this.site = site;
	}

	/**
	 * @return the orderId
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the orderLeIds
	 */
	public List<Integer> getOrderLeIds() {
		if (orderLeIds == null) {
			orderLeIds = new ArrayList<>();
		}
		return orderLeIds;
	}

	/**
	 * @param orderLeIds
	 *            the orderLeIds to set
	 */
	public void setOrderLeIds(List<Integer> orderLeIds) {
		this.orderLeIds = orderLeIds;
	}

	public boolean isManualFeasible() {
		return isManualFeasible;
	}

	public void setManualFeasible(boolean isManualFeasible) {
		this.isManualFeasible = isManualFeasible;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	@Override
	public String toString() {
		return "NplQuoteDetail{" +
				"quoteId=" + quoteId +
				", orderId=" + orderId +
				", quoteleId=" + quoteleId +
				", customerId=" + customerId +
				", orderLeIds=" + orderLeIds +
				", productName='" + productName + '\'' +
				", solutions=" + solutions +
				", site=" + site +
				", linkCount=" + linkCount +
				", isManualFeasible=" + isManualFeasible +
				", orderType='" + orderType + '\'' +
				", orderCategory='" + orderCategory + '\'' +
				", serviceId='" + serviceId + '\'' +
				", engagementOptyId='" + engagementOptyId + '\'' +
				", quoteCode='" + quoteCode + '\'' +
				", classification='" + classification + '\'' +
				'}';
	}
}
