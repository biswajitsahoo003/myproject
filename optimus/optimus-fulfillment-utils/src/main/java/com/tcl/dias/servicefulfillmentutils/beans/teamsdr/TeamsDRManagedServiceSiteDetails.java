package com.tcl.dias.servicefulfillmentutils.beans.teamsdr;

import com.tcl.dias.common.serviceinventory.bean.ScComponentAttributeBean;

import java.util.List;
import java.util.Map;

/**
 * Bean to store managed services site details
 */
public class TeamsDRManagedServiceSiteDetails {
	private Integer siteCompId;
	private String siteName;
	private String componentName;
	private Map<String, Object> siteAttributes;
	private List<TeamsDRBatchDetails> teamsDRBatchDetails;

	public TeamsDRManagedServiceSiteDetails() {
	}

	public Integer getSiteCompId() {
		return siteCompId;
	}

	public void setSiteCompId(Integer siteCompId) {
		this.siteCompId = siteCompId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Map<String, Object> getSiteAttributes() {
		return siteAttributes;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public void setSiteAttributes(Map<String, Object> siteAttributes) {
		this.siteAttributes = siteAttributes;
	}

	public List<TeamsDRBatchDetails> getTeamsDRBatchDetails() {
		return teamsDRBatchDetails;
	}

	public void setTeamsDRBatchDetails(List<TeamsDRBatchDetails> teamsDRBatchDetails) {
		this.teamsDRBatchDetails = teamsDRBatchDetails;
	}
}
