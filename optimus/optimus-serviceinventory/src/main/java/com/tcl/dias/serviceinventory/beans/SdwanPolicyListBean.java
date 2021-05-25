package com.tcl.dias.serviceinventory.beans;

/**
 * Response Bean for policy List API
 * 
 * @author Srinivasa Raghavan
 */
public class SdwanPolicyListBean {

	private String policyType;
	private String policyName;
	private SdwanTemplateBean associatedTemplate;
	private String organisationName;
	private String policyAlias;
	private String directorRegion;
	private Integer associatedAppCount;
	private Integer associatedUrlCount;
	private String accessPolicyName;
	
	public String getAccessPolicyName() {
		return accessPolicyName;
	}

	public void setAccessPolicyName(String accessPolicyName) {
		this.accessPolicyName = accessPolicyName;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public SdwanTemplateBean getAssociatedTemplate() {
		return associatedTemplate;
	}

	public void setAssociatedTemplate(SdwanTemplateBean associatedTemplate) {
		this.associatedTemplate = associatedTemplate;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getPolicyAlias() {
		return policyAlias;
	}

	public void setPolicyAlias(String policyAlias) {
		this.policyAlias = policyAlias;
	}

	public String getDirectorRegion() {
		return directorRegion;
	}

	public void setDirectorRegion(String directorRegion) {
		this.directorRegion = directorRegion;
	}

	public Integer getAssociatedAppCount() {
		return associatedAppCount;
	}

	public void setAssociatedAppCount(Integer associatedAppCount) {
		this.associatedAppCount = associatedAppCount;
	}

	public Integer getAssociatedUrlCount() {
		return associatedUrlCount;
	}

	public void setAssociatedUrlCount(Integer associatedUrlCount) {
		this.associatedUrlCount = associatedUrlCount;
	}

	@Override
	public String toString() {
		return "SdwanPolicyListBean{" + "policyType='" + policyType + '\'' + ", policyName='" + policyName + '\''
				+ ", associatedTemplate=" + associatedTemplate + ", organisationName='" + organisationName + '\''
				+ ", policyAlias='" + policyAlias + '\'' + ", directorRegion='" + directorRegion + '\''
				+ ", associatedAppCount=" + associatedAppCount + ", associatedUrlCount=" + associatedUrlCount + '}';
	}
}
