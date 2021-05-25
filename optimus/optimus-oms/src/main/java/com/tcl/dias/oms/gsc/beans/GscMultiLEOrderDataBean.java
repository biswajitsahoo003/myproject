package com.tcl.dias.oms.gsc.beans;

import java.util.Date;
import java.util.List;

/**
 * Order data bean to accommodate GSC multi LE scenario
 * 
 * @author Srinivasa Raghavan
 * 
 */
public class GscMultiLEOrderDataBean {

	private Integer orderId;
	private Integer quoteId;
	private String orderCode;
	private Integer customerId;
	private String productFamilyName;
	private String accessType;
	private String profileName;
	private Integer orderVersion;
	private String engagementOptyId;
	private List<GscMultipleLEOrderSolutionBean> solutionsToBeAdded;
	private List<GscMultiOrderLeBean> orderToLes;
	private Date createdTime;
	private Integer createdBy;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getProductFamilyName() {
		return productFamilyName;
	}

	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Integer getOrderVersion() {
		return orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public List<GscMultipleLEOrderSolutionBean> getSolutionsToBeAdded() {
		return solutionsToBeAdded;
	}

	public void setSolutionsToBeAdded(List<GscMultipleLEOrderSolutionBean> solutionsToBeAdded) {
		this.solutionsToBeAdded = solutionsToBeAdded;
	}

	public List<GscMultiOrderLeBean> getOrderToLes() {
		return orderToLes;
	}

	public void setOrderToLes(List<GscMultiOrderLeBean> orderToLes) {
		this.orderToLes = orderToLes;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
}
