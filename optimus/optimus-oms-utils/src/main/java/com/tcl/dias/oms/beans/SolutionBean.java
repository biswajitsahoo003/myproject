package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.List;

public class SolutionBean implements Serializable{
	
	private String serviceVariant;
	
	private String lastMileText;
	
	private String resiliency;
	
	private String cpeText;
	
	private String portBandwidth;

	private List<CofSiteBean> singleInternetAccessSiteDetailsList;

	public List<CofSiteBean> getSingleInternetAccessSiteDetailsList() {
		return singleInternetAccessSiteDetailsList;
	}

	public void setSingleInternetAccessSiteDetailsList(List<CofSiteBean> singleInternetAccessSiteDetailsList) {
		this.singleInternetAccessSiteDetailsList = singleInternetAccessSiteDetailsList;
	}

	public String getServiceVariant() {
		return serviceVariant;
	}

	public void setServiceVariant(String serviceVariant) {
		this.serviceVariant = serviceVariant;
	}

	public String getLastMileText() {
		return lastMileText;
	}

	public void setLastMileText(String lastMileText) {
		this.lastMileText = lastMileText;
	}

	public String getResiliency() {
		return resiliency;
	}

	public void setResiliency(String resiliency) {
		this.resiliency = resiliency;
	}

	public String getCpeText() {
		return cpeText;
	}

	public void setCpeText(String cpeText) {
		this.cpeText = cpeText;
	}

	public String getPortBandwidth() {
		return portBandwidth;
	}

	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
	
	

}
