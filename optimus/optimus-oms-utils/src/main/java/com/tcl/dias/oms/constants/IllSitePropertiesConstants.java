package com.tcl.dias.oms.constants;

/**
 * this class used for ill site specific constants
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum IllSitePropertiesConstants {

	SITE_PROPERTIES("SITE_PROPERTIES"),LOCATION_IT_CONTACT("LOCAL_IT_CONTACT"),TAX_EXEMPTED_FILENAME("TAX_EXEMPTED_FILENAME"),TAX_EXEMPTED_ATTACHMENTID("TAX_EXEMPTED_ATTACHMENTID"),APPROVAL_LEVEL("Approval Level"),SFDC_ORDER_TYPE("SFDC_ORDER_TYPE");
	private String siteProperties;

	private IllSitePropertiesConstants(String siteProperties) {
		this.siteProperties = siteProperties;
	}

	public String getSiteProperties() {
		return siteProperties;
	}

}
