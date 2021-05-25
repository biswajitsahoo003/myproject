package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.tcl.dias.oms.beans.OrderProductComponentBean;

/**
 * Teams DR Order services bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderServicesBean {
	private Integer solutionId;
	private String solutionCode;
	private String offeringName;
	private String offeringType;
	private String plan;
	private Integer noOfUsers;
	private List<MediaGatewayOrderConfigurationBean> mgConfigurations;
	private List<TeamsDROrderConfigurationBean> configurations;
	private TeamsDRLicenseComponentsBean licenseComponents;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;
	private List<OrderProductComponentBean> components;

	public TeamsDROrderServicesBean() {
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

	public List<TeamsDROrderConfigurationBean> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<TeamsDROrderConfigurationBean> configurations) {
		this.configurations = configurations;
	}

	public TeamsDRLicenseComponentsBean getLicenseComponents() {
		return licenseComponents;
	}

	public void setLicenseComponents(TeamsDRLicenseComponentsBean licenseComponents) {
		this.licenseComponents = licenseComponents;
	}

	public List<MediaGatewayOrderConfigurationBean> getMgConfigurations() {
		return mgConfigurations;
	}

	public void setMgConfigurations(List<MediaGatewayOrderConfigurationBean> mgConfigurations) {
		this.mgConfigurations = mgConfigurations;
	}

	public List<OrderProductComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<OrderProductComponentBean> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "TeamsDROrderServicesBean{" + "solutionId=" + solutionId + ", solutionCode='" + solutionCode + '\''
				+ ", offeringName='" + offeringName + '\'' + ", offeringType='" + offeringType + '\'' + ", plan='"
				+ plan + '\'' + ", noOfUsers=" + noOfUsers + ", mgConfigurations=" + mgConfigurations
				+ ", configurations=" + configurations + ", licenseComponents=" + licenseComponents + ", mrc=" + mrc
				+ ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + ", components=" + components + '}';
	}
}
