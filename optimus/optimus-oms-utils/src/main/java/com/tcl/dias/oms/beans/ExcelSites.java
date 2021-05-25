package com.tcl.dias.oms.beans;

import java.util.List;

/**
 * This file contains the ExcelSites.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ExcelSites {

	private Integer siteId;


	private List<ExcelSiteAttributes> siteAttributes;
	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 *            the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the siteAttributes
	 */
	public List<ExcelSiteAttributes> getSiteAttributes() {
		return siteAttributes;
	}

	/**
	 * @param siteAttributes
	 *            the siteAttributes to set
	 */
	public void setSiteAttributes(List<ExcelSiteAttributes> siteAttributes) {
		this.siteAttributes = siteAttributes;
	}

}
