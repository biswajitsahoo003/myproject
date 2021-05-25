package com.tcl.dias.oms.teamsdr.beans;

import com.tcl.dias.oms.beans.QuoteProductComponentBean;

import java.util.List;

/**
 * Bean for Teams DR Services
 * 
 * @author Srinivas Raghavan
 *
 */
public class TeamsDRServicesBean {
	private Integer solutionId;
	private String solutionCode;
	private Integer quoteTeamsDRId;
	private String offeringName;
	private String offeringType;
	private String plan;
	private Integer noOfUsers;
	private Integer nrcQuantity = 1;
	private List<MediaGatewayConfigurationBean> mgConfigurations;
	private List<TeamsDRConfigurationBean> configurations;
	private TeamsDRLicenseComponentsBean licenseComponents;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;
	private List<QuoteProductComponentBean> components;

	public TeamsDRServicesBean() {
		this.nrcQuantity = 1;
	}

	public Integer getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}

	public String getSolutionCode() {
		return solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

	public Integer getQuoteTeamsDRId() {
		return quoteTeamsDRId;
	}

	public void setQuoteTeamsDRId(Integer quoteTeamsDRId) {
		this.quoteTeamsDRId = quoteTeamsDRId;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Integer getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(Integer noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public Integer getNrcQuantity() {
		return nrcQuantity;
	}

	public void setNrcQuantity(Integer nrcQuantity) {
		this.nrcQuantity = nrcQuantity;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getOfferingType() {
		return offeringType;
	}

	public void setOfferingType(String offeringType) {
		this.offeringType = offeringType;
	}

	public List<TeamsDRConfigurationBean> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<TeamsDRConfigurationBean> configurations) {
		this.configurations = configurations;
	}

	public TeamsDRLicenseComponentsBean getLicenseComponents() {
		return licenseComponents;
	}

	public void setLicenseComponents(TeamsDRLicenseComponentsBean licenseComponents) {
		this.licenseComponents = licenseComponents;
	}

	public List<MediaGatewayConfigurationBean> getMgConfigurations() {
		return mgConfigurations;
	}

	public void setMgConfigurations(List<MediaGatewayConfigurationBean> mgConfigurations) {
		this.mgConfigurations = mgConfigurations;
	}

	public List<QuoteProductComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<QuoteProductComponentBean> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "TeamsDRServicesBean{" +
				"solutionId=" + solutionId +
				", solutionCode='" + solutionCode + '\'' +
				", offeringName='" + offeringName + '\'' +
				", offeringType='" + offeringType + '\'' +
				", plan='" + plan + '\'' +
				", noOfUsers=" + noOfUsers +
				", mgConfigurations=" + mgConfigurations +
				", configurations=" + configurations +
				", licenseComponents=" + licenseComponents +
				", mrc=" + mrc +
				", nrc=" + nrc +
				", arc=" + arc +
				", tcv=" + tcv +
				'}';
	}
}
