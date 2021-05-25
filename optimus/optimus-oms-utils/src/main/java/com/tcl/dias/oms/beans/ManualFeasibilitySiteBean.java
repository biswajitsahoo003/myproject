package com.tcl.dias.oms.beans;

public class ManualFeasibilitySiteBean {

	private Integer siteId;
	private String siteType;
	private boolean retriggerTaskForFeasibleSites;
	
	// For PIPF -58
	private boolean mfTaskRequested;
	
	public boolean isMfTaskRequested() {
		return mfTaskRequested;
	}
	public void setMfTaskRequested(boolean mfTaskRequested) {
		this.mfTaskRequested = mfTaskRequested;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	// PIPF -55
	public boolean isRetriggerTaskForFeasibleSites() {
		return retriggerTaskForFeasibleSites;
	}

	public void setRetriggerTaskForFeasibleSites(boolean retriggerTaskForFeasibleSites) {
		this.retriggerTaskForFeasibleSites = retriggerTaskForFeasibleSites;
	}
	
	
}
