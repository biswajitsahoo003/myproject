package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;


import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 *
 *
 */
public class SiteListConfigDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String siteListId;
	private String siteListName;
	private Set<String> sdwanServiceId;
	private String siteListAlias;
	private List<Integer> attributeId;
	private Integer associatedSitesCount = 0;
	private Set<AssosciatedSiteDetails> siteDetails;
	
	

	public Set<String> getSdwanServiceId() {
		return sdwanServiceId;
	}

	public String getSiteListId() {
		return siteListId;
	}

	public void setSiteListId(String siteListId) {
		this.siteListId = siteListId;
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
//	public Integer getAssociatedRulesCount() {
//		return associatedRulesCount;
//	}
//	public void setAssociatedRulesCount(Integer associatedRulesCount) {
//		this.associatedRulesCount = associatedRulesCount;
//	}
//	public List<TemplateSiteDetails> getTemplateSiteDetails() {
//		return templateSiteDetails;
//	}
//	public void setTemplateSiteDetails(List<TemplateSiteDetails> templateSiteDetails) {
//		this.templateSiteDetails = templateSiteDetails;
//	}
//
//	public Set<SdwanCpeDetails> getSdwanCpeDetails() {
//		return sdwanCpeDetails;
//	}
//
//	public void setSdwanCpeDetails(Set<SdwanCpeDetails> sdwanCpeDetails) {
//		this.sdwanCpeDetails = sdwanCpeDetails;
//	}

	
	public String getSiteListName() {
		return siteListName;
	}
	public String getSiteListAlias() {
		return siteListAlias;
	}

	public void setSiteListAlias(String siteListAlias) {
		this.siteListAlias = siteListAlias;
	}

	public void setSiteListName(String siteListName) {
		this.siteListName = siteListName;
	}

	public Set<AssosciatedSiteDetails> getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(Set<AssosciatedSiteDetails> siteDetails) {
		this.siteDetails = siteDetails;
	}

	
	
}
