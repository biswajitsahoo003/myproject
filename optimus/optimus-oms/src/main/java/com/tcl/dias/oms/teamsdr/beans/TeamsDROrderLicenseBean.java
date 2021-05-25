package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.tcl.dias.oms.beans.OrderProductComponentBean;

/**
 * Teams DR Order license bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderLicenseBean {
	private Integer id;
	private String licenseName;
	private String licenseContractPeriod;
	private Integer noOfLicenses;
	private String sfdcProductName;
	private List<OrderProductComponentBean> licenseSKUComponents;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;

	public TeamsDROrderLicenseBean() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
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

	public Integer getNoOfLicenses() {
		return noOfLicenses;
	}

	public void setNoOfLicenses(Integer noOfLicenses) {
		this.noOfLicenses = noOfLicenses;
	}

	public String getLicenseContractPeriod() {
		return licenseContractPeriod;
	}

	public void setLicenseContractPeriod(String licenseContractPeriod) {
		this.licenseContractPeriod = licenseContractPeriod;
	}

	public List<OrderProductComponentBean> getLicenseSKUComponents() {
		return licenseSKUComponents;
	}

	public void setLicenseSKUComponents(List<OrderProductComponentBean> licenseSKUComponents) {
		this.licenseSKUComponents = licenseSKUComponents;
	}

	public String getSfdcProductName() {
		return sfdcProductName;
	}

	public void setSfdcProductName(String sfdcProductName) {
		this.sfdcProductName = sfdcProductName;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseBean{" + "id=" + id + ", licenseName='" + licenseName + '\'' + ", licenseContractPeriod='"
				+ licenseContractPeriod + '\'' + ", noOfLicenses=" + noOfLicenses + ", licenseComponents="
				+ licenseSKUComponents + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv
				+ ", sfdcProductName=" + sfdcProductName + '}';
	}
}
