package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.Objects;
import java.util.Set;

/**
 * Bean for CiscoSiteListInstanceMapping
 * 
 * 
 */
public class CiscoSiteListInstanceMapping {
	private String instanceRegion;
	private String siteListId;

	

	public String getInstanceRegion() {
		return instanceRegion;
	}

	public void setInstanceRegion(String instanceRegion) {
		this.instanceRegion = instanceRegion;
	}

	

	public String getSiteListId() {
		return siteListId;
	}

	public void setSiteListId(String siteListId) {
		this.siteListId = siteListId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CiscoSiteListInstanceMapping) {
			CiscoSiteListInstanceMapping other = (CiscoSiteListInstanceMapping) obj;
			return  Objects.equals(this.instanceRegion, other.instanceRegion)
					&& Objects.equals(this.siteListId, other.siteListId);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return  this.instanceRegion.hashCode() + this.siteListId.hashCode();
	}
	
}
