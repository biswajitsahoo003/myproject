/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillment.beans.gsc.SiteDetailsBean;

/**
 * @author vivek
 *
 */
public class CreateSiteRepcRequest {

	private Integer customerId;
	private List<SiteDetailsBean> siteDetails;

	/**
	 * @return the siteDetails
	 */
	public List<SiteDetailsBean> getSiteDetails() {
		return siteDetails;
	}

	/**
	 * @param siteDetails the siteDetails to set
	 */
	public void setSiteDetails(List<SiteDetailsBean> siteDetails) {
		this.siteDetails = siteDetails;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
}
