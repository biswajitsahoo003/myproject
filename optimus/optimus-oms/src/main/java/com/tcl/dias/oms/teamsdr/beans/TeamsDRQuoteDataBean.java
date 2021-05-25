package com.tcl.dias.oms.teamsdr.beans;

import com.tcl.dias.oms.gsc.beans.GscMultipleLESolutionBean;

import java.util.List;

/**
 * Bean for Teams DR Quote Data
 *
 * @author Srinivas Raghavan
 *
 */
public class TeamsDRQuoteDataBean {
	// quote related
	private Integer quoteId;
	private String quoteCode;
	private Integer customerId;
	private String productFamilyName;
	private String accessType;
	private String profileName;
	private Integer quoteVersion;
	private String engagementOptyId;
	private String stage;
	private String subStage;
	private Boolean isAdminChangedPrice;
	private String effectiveMSADate;
	private List<TeamsDRServicesBean> newTeamsDRSolutions;
	private List<GscMultipleLESolutionBean> newGscSolutions;
	private List<TeamsDRMultiQuoteLeBean> quoteToLes;

	public TeamsDRQuoteDataBean() {

	}

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

	public Integer getQuoteVersion() {
		return quoteVersion;
	}

	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public List<TeamsDRMultiQuoteLeBean> getQuoteToLes() {
		return quoteToLes;
	}

	public void setQuoteToLes(List<TeamsDRMultiQuoteLeBean> quoteToLes) {
		this.quoteToLes = quoteToLes;
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

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getSubStage() {
		return subStage;
	}

	public void setSubStage(String subStage) {
		this.subStage = subStage;
	}

	public Boolean getIsAdminChangedPrice() {
		return isAdminChangedPrice;
	}

	public void setIsAdminChangedPrice(Boolean isAdminChangedPrice) {
		this.isAdminChangedPrice = isAdminChangedPrice;
	}

	public String getEffectiveMSADate() {
		return effectiveMSADate;
	}

	public void setEffectiveMSADate(String effectiveMSADate) {
		this.effectiveMSADate = effectiveMSADate;
	}

	@Override
	public String toString() {
		return "TeamsDRQuoteDataBean{" +
				"quoteId=" + quoteId +
				", quoteCode='" + quoteCode + '\'' +
				", customerId=" + customerId +
				", productFamilyName='" + productFamilyName + '\'' +
				", accessType='" + accessType + '\'' +
				", profileName='" + profileName + '\'' +
				", quoteVersion=" + quoteVersion +
				", engagementOptyId='" + engagementOptyId + '\'' +
				", stage='" + stage + '\'' +
				", subStage='" + subStage + '\'' +
				", isAdminChangedPrice=" + isAdminChangedPrice +
				", effectiveMSADate='" + effectiveMSADate + '\'' +
				", newTeamsDRSolutions=" + newTeamsDRSolutions +
				", newGscSolutions=" + newGscSolutions +
				", quoteToLes=" + quoteToLes +
				'}';
	}
}
