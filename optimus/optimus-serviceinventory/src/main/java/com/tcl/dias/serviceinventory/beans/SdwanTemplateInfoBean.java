package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * Bean for listing template information in template tab SDWAN
 */
public class SdwanTemplateInfoBean {

	private String templateName;
	private List<SdwanCpeInfoBean> sdwanCpes;
	private Integer policiesCount;
	private Integer sitesCount;
	private String templateAlias;

	public SdwanTemplateInfoBean() {
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<SdwanCpeInfoBean> getSdwanCpes() {
		return sdwanCpes;
	}

	public void setSdwanCpes(List<SdwanCpeInfoBean> sdwanCpes) {
		this.sdwanCpes = sdwanCpes;
	}

	public Integer getPoliciesCount() {
		return policiesCount;
	}

	public void setPoliciesCount(Integer policiesCount) {
		this.policiesCount = policiesCount;
	}

	public Integer getSitesCount() {
		return sitesCount;
	}

	public void setSitesCount(Integer sitesCount) {
		this.sitesCount = sitesCount;
	}

	public String getTemplateAlias() {
		return templateAlias;
	}

	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}

	@Override
	public String toString() {
		return "SdwanTemplateInfoBean{" + "templateName='" + templateName + '\'' + ", sdwanCpes=" + sdwanCpes
				+ ", policiesCount=" + policiesCount + ", sitesCount=" + sitesCount + ", templateAlias='"
				+ templateAlias + '\'' + '}';
	}
}
