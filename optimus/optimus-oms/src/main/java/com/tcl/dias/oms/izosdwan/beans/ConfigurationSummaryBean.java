package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

/**
 * 
 * @author vpachava
 *
 */
public class ConfigurationSummaryBean {

	private String siteTypeName;

	private Integer noOfSites;

	private List<CpeTypes> cpeTypes;

	private List<ConfigurationCpeInfo> cpeInfo;
	
	

	public List<ConfigurationCpeInfo> getCpeInfo() {
		return cpeInfo;
	}

	public void setCpeInfo(List<ConfigurationCpeInfo> cpeInfo) {
		this.cpeInfo = cpeInfo;
	}

	public String getSiteTypeName() {
		return siteTypeName;
	}

	public void setSiteTypeName(String siteTypeName) {
		this.siteTypeName = siteTypeName;
	}

	public Integer getNoOfSites() {
		return noOfSites;
	}

	public void setNoOfSites(Integer noOfSites) {
		this.noOfSites = noOfSites;
	}

	public List<CpeTypes> getCpeTypes() {
		return cpeTypes;
	}

	public void setCpeTypes(List<CpeTypes> cpeTypes) {
		this.cpeTypes = cpeTypes;
	}

}
