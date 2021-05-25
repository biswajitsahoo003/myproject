package com.tcl.dias.common.beans;

import java.util.List;

public class MasterVRFBean {
	private String masterVrfName;
	private String masterVrfBandwidth;
	private String masterFlexiqos;
	private String billingType;
	private String noOfVrfs;
	private String totalVrfBandwidth;
	private String masterServiceId;
	private List<SlaveVRFBean> slaveVRFDetails;
	public String getMasterVrfName() {
		return masterVrfName;
	}
	public void setMasterVrfName(String masterVrfName) {
		this.masterVrfName = masterVrfName;
	}
	public String getMasterVrfBandwidth() {
		return masterVrfBandwidth;
	}
	public void setMasterVrfBandwidth(String masterVrfBandwidth) {
		this.masterVrfBandwidth = masterVrfBandwidth;
	}
	public String getMasterFlexiqos() {
		return masterFlexiqos;
	}
	public void setMasterFlexiqos(String masterFlexiqos) {
		this.masterFlexiqos = masterFlexiqos;
	}
	public String getBillingType() {
		return billingType;
	}
	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
	public String getNoOfVrfs() {
		return noOfVrfs;
	}
	public void setNoOfVrfs(String noOfVrfs) {
		this.noOfVrfs = noOfVrfs;
	}
	public String getTotalVrfBandwidth() {
		return totalVrfBandwidth;
	}
	public void setTotalVrfBandwidth(String totalVrfBandwidth) {
		this.totalVrfBandwidth = totalVrfBandwidth;
	}
	public String getMasterServiceId() {
		return masterServiceId;
	}
	public void setMasterServiceId(String masterServiceId) {
		this.masterServiceId = masterServiceId;
	}
	public List<SlaveVRFBean> getSlaveVRFDetails() {
		return slaveVRFDetails;
	}
	public void setSlaveVRFDetails(List<SlaveVRFBean> slaveVRFDetails) {
		this.slaveVRFDetails = slaveVRFDetails;
	}
	
	
	
	
	
}
