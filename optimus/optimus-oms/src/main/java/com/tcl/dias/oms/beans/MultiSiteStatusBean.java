package com.tcl.dias.oms.beans;


/**
 * @author Nithya S
 *This bean class is used to get status of multisites
 */
public class MultiSiteStatusBean {

	private String siteInProgress;
	private String siteComplete;
	private String overAllStatus;
	
	public String getSiteInProgress() {
		return siteInProgress;
	}
	public void setSiteInProgress(String siteInProgress) {
		this.siteInProgress = siteInProgress;
	}
	public String getSiteComplete() {
		return siteComplete;
	}
	public void setSiteComplete(String siteComplete) {
		this.siteComplete = siteComplete;
	}
	public String getOverAllStatus() {
		return overAllStatus;
	}
	public void setOverAllStatus(String overAllStatus) {
		this.overAllStatus = overAllStatus;
	}
	
	
}
