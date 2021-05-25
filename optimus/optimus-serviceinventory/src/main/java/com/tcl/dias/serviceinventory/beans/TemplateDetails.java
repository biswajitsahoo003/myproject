package com.tcl.dias.serviceinventory.beans;


import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author archchan
 *
 */
public class TemplateDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String templateName;
	private Set<String> sdwanServiceId;
	private String templateAlias;
	private List<Integer> attributeId;
	private Integer associatedSitesCount = 0;
	private Integer associatedRulesCount = 0;
	private List<TemplateSiteDetails> templateSiteDetails;
	private Set<SdwanCpeDetails> sdwanCpeDetails;
	
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Set<String> getSdwanServiceId() {
		return sdwanServiceId;
	}

	public void setSdwanServiceId(Set<String> sdwanServiceId) {
		this.sdwanServiceId = sdwanServiceId;
	}

	public List<Integer> getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(List<Integer> attributeId) {
		this.attributeId = attributeId;
	}
	public Integer getAssociatedSitesCount() {
		return associatedSitesCount;
	}
	public void setAssociatedSitesCount(Integer associatedSitesCount) {
		this.associatedSitesCount = associatedSitesCount;
	}
	public Integer getAssociatedRulesCount() {
		return associatedRulesCount;
	}
	public void setAssociatedRulesCount(Integer associatedRulesCount) {
		this.associatedRulesCount = associatedRulesCount;
	}
	public List<TemplateSiteDetails> getTemplateSiteDetails() {
		return templateSiteDetails;
	}
	public void setTemplateSiteDetails(List<TemplateSiteDetails> templateSiteDetails) {
		this.templateSiteDetails = templateSiteDetails;
	}

	public Set<SdwanCpeDetails> getSdwanCpeDetails() {
		return sdwanCpeDetails;
	}

	public void setSdwanCpeDetails(Set<SdwanCpeDetails> sdwanCpeDetails) {
		this.sdwanCpeDetails = sdwanCpeDetails;
	}

	public String getTemplateAlias() {
		return templateAlias;
	}

	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}
}
