package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * Bean to fetch total bandwidth utilized by apps and list of individual
 * bandwidth utilization
 *
 * @author Srinivasa Raghavan
 */
public class AppLinkUtilizationBean {
	private List<AppBWUtilizationBean> appBWUtilizations;
	private Double totalBw;
	private String bwUnit;

	public AppLinkUtilizationBean() {
		this.totalBw = 0D;
	}

	public List<AppBWUtilizationBean> getAppBWUtilizations() {
		return appBWUtilizations;
	}

	public void setAppBWUtilizations(List<AppBWUtilizationBean> appBWUtilizations) {
		this.appBWUtilizations = appBWUtilizations;
	}

	public Double getTotalBw() {
		return totalBw;
	}

	public void setTotalBw(Double totalBw) {
		this.totalBw = totalBw;
	}

	public String getBwUnit() {
		return bwUnit;
	}

	public void setBwUnit(String bwUnit) {
		this.bwUnit = bwUnit;
	}
}
