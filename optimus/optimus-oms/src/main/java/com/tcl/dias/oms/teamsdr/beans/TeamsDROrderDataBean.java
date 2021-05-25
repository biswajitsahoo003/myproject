package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.tcl.dias.oms.gsc.beans.GscMultipleLESolutionBean;

/**
 * Teams DR Order data bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderDataBean {

	private Integer orderId;
	private String orderCode;
	private Integer quoteId;
	private Integer customerId;
	private String productFamilyName;
	private String accessType;
	private String profileName;
	private Integer orderVersion;
	private String engagementOptyId;
	private List<TeamsDRServicesBean> newTeamsDRSolutions;
	private List<GscMultipleLESolutionBean> newGscSolutions;
	private List<TeamsDRMultiOrderLeBean> orderToLes;

	public TeamsDROrderDataBean() {

	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
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

	public List<TeamsDRMultiOrderLeBean> getOrderToLes() {
		return orderToLes;
	}

	public void setOrderToLes(List<TeamsDRMultiOrderLeBean> orderToLes) {
		this.orderToLes = orderToLes;
	}

	public List<TeamsDRServicesBean> getNewTeamsDRSolutions() {
		return newTeamsDRSolutions;
	}

	public void setNewTeamsDRSolutions(List<TeamsDRServicesBean> newTeamsDRSolutions) {
		this.newTeamsDRSolutions = newTeamsDRSolutions;
	}

	public List<GscMultipleLESolutionBean> getNewGscSolutions() {
		return newGscSolutions;
	}

	public void setNewGscSolutions(List<GscMultipleLESolutionBean> newGscSolutions) {
		this.newGscSolutions = newGscSolutions;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	@Override
	public String toString() {
		return "TeamsDRQuoteDataBean{" + "quoteId=" + orderId + ", quoteCode='" + orderCode + '\'' + ", customerId="
				+ customerId + ", productFamilyName='" + productFamilyName + '\'' + ", accessType='" + accessType + '\''
				+ ", profileName='" + profileName + '\'' + ", orderVersion=" + orderVersion + ", engagementOptyId='"
				+ engagementOptyId + '\'' + ", newTeamsDRSolutions=" + newTeamsDRSolutions + ", newGscSolutions="
				+ newGscSolutions + ", quoteToLes=" + orderToLes + '}';
	}
}
