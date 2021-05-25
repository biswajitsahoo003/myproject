/**
 * 
 */
package com.tcl.dias.nso.constants;

/**
 * @author KarMani
 *
 */
public enum IllSitePropertiesConstants {

	
	SITE_PROPERTIES("SITE_PROPERTIES"),LOCATION_IT_CONTACT("LOCAL_IT_CONTACT"),TAX_EXEMPTED_FILENAME("TAX_EXEMPTED_FILENAME"),TAX_EXEMPTED_ATTACHMENTID("TAX_EXEMPTED_ATTACHMENTID"),APPROVAL_LEVEL("Approval Level");
	private String siteProperties;

	private IllSitePropertiesConstants(String siteProperties) {
		this.siteProperties = siteProperties;
	}

	public String getSiteProperties() {
		return siteProperties;
	}
}
