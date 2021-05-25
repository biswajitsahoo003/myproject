package com.tcl.dias.oms.izopc.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.Site;

/**
 * This file contains the QuoteRequest.java class. Bean class
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class QuoteDetail {

	private Integer quoteId;
	private Integer orderId;
	private Integer quoteleId;
	private Integer customerId;
	private List<Integer> orderLeIds;
	private String productName;
	private List<SolutionDetail> solutions;
	private Byte isMulticircuit;
	private String engagementOptyId;
	private String orderCategory;
	private String orderType;
	private Integer orderIllSiteId;
	private String primaryOrSecondaryOrBoth;
	List<Site> site;

	@JsonIgnore
	boolean isManualFeasible;

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
	 * @return the orderLeIds
	 */
	public List<Integer> getOrderLeIds() {
		if (orderLeIds == null) {
			orderLeIds = new ArrayList<>();
		}
		return orderLeIds;
	}

	/**
	 * @param orderLeIds the orderLeIds to set
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

	public Byte getIsMulticircuit() {
		return isMulticircuit;
	}

	public void setIsMulticircuit(Byte isMulticircuit) {
		this.isMulticircuit = isMulticircuit;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderIllSiteId() {
		return orderIllSiteId;
	}

	public void setOrderIllSiteId(Integer orderIllSiteId) {
		this.orderIllSiteId = orderIllSiteId;
	}

	public String getPrimaryOrSecondaryOrBoth() {
		return primaryOrSecondaryOrBoth;
	}

	public void setPrimaryOrSecondaryOrBoth(String primaryOrSecondaryOrBoth) {
		this.primaryOrSecondaryOrBoth = primaryOrSecondaryOrBoth;
	}
	
	

	public List<Site> getSite() {
		return site;
	}

	public void setSite(List<Site> site) {
		this.site = site;
	}

	@Override
	public String toString() {
		return "QuoteDetail [quoteId=" + quoteId + ", quoteleId=" + quoteleId + ", customerId=" + customerId
				+ ", productName=" + productName + ", solutions=" + solutions + "]";
	}

}
