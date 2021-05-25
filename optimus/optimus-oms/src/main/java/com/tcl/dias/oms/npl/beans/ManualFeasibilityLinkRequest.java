package com.tcl.dias.oms.npl.beans;

/**
 * @author krutsrin
 *
 *A POJO to handle MF request raised for NPL
 */
public class ManualFeasibilityLinkRequest {
	
	private Integer linkId;
	private String linkType;
	private Integer siteA;
	private Integer siteB;
	private boolean retriggerTaskForFeasibleSites;
	
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	public Integer getSiteA() {
		return siteA;
	}
	public void setSiteA(Integer siteA) {
		this.siteA = siteA;
	}
	public Integer getSiteB() {
		return siteB;
	}
	public void setSiteB(Integer siteB) {
		this.siteB = siteB;
	}
	
// PIPF -55
	public boolean isRetriggerTaskForFeasibleSites() {
		return retriggerTaskForFeasibleSites;
	}
	public void setRetriggerTaskForFeasibleSites(boolean retriggerTaskForFeasibleSites) {
		this.retriggerTaskForFeasibleSites = retriggerTaskForFeasibleSites;
	}
}
