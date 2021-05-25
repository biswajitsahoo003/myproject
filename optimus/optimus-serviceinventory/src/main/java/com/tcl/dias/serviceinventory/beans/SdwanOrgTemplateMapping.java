package com.tcl.dias.serviceinventory.beans;

import java.util.Objects;
import java.util.Set;

/**
 * Bean for sdwan organisation-instance region-template attributes mapping
 * 
 * @author Srinivasa Raghavan
 */
public class SdwanOrgTemplateMapping {
	private String organisationName;
	private String instanceRegion;
	private String templateName;

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getInstanceRegion() {
		return instanceRegion;
	}

	public void setInstanceRegion(String instanceRegion) {
		this.instanceRegion = instanceRegion;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SdwanOrgTemplateMapping) {
			SdwanOrgTemplateMapping other = (SdwanOrgTemplateMapping) obj;
			return Objects.equals(this.organisationName, other.organisationName)
					&& Objects.equals(this.instanceRegion, other.instanceRegion)
					&& Objects.equals(this.templateName, other.templateName);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return this.organisationName.hashCode() + this.instanceRegion.hashCode() + this.templateName.hashCode();
	}
}
