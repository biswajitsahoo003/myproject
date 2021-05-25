package com.tcl.dias.common.beans;

public class MfSiteBDetailsBean {
	private Integer siteIdB;

	public Integer getSiteIdB() {
		return siteIdB;
	}

	public void setSiteIdB(Integer siteIdB) {
		this.siteIdB = siteIdB;
	}

	private String siteBEndType;
	private String siteBFeasibilityStatus;
	private String systemFeasibleEnd;
	private String systemEndJson;
	private String ProviderForSiteB;
	private String feasibilityTypeSiteB;
	private String feasibilityModeSiteB;

	private boolean isReturned;

	public boolean isReturned() {
		return isReturned;
	}

	public void setReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}

	public String getSiteBEndType() {
		return siteBEndType;
	}

	public void setSiteBEndType(String siteBEndType) {
		this.siteBEndType = siteBEndType;
	}

	public String getSiteBFeasibilityStatus() {
		return siteBFeasibilityStatus;
	}

	public void setSiteBFeasibilityStatus(String siteBFeasibilityStatus) {
		this.siteBFeasibilityStatus = siteBFeasibilityStatus;
	}

	public String getSystemFeasibleEnd() {
		return systemFeasibleEnd;
	}

	public void setSystemFeasibleEnd(String systemFeasibleEnd) {
		this.systemFeasibleEnd = systemFeasibleEnd;
	}

	public String getSystemEndJson() {
		return systemEndJson;
	}

	public void setSystemEndJson(String systemEndJson) {
		this.systemEndJson = systemEndJson;
	}

	public String getProviderForSiteB() {
		return ProviderForSiteB;
	}

	public void setProviderForSiteB(String providerForSiteB) {
		ProviderForSiteB = providerForSiteB;
	}

	public String getFeasibilityTypeSiteB() {
		return feasibilityTypeSiteB;
	}

	public void setFeasibilityTypeSiteB(String feasibilityTypeSiteB) {
		this.feasibilityTypeSiteB = feasibilityTypeSiteB;
	}

	public String getFeasibilityModeSiteB() {
		return feasibilityModeSiteB;
	}

	public void setFeasibilityModeSiteB(String feasibilityModeSiteB) {
		this.feasibilityModeSiteB = feasibilityModeSiteB;
	}

}
