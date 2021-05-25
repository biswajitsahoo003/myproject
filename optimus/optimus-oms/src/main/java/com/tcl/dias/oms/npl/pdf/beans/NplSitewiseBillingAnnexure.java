package com.tcl.dias.oms.npl.pdf.beans;

/**
 * 
 * This file contains the NplSitewiseBillingAnnexure.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class NplSitewiseBillingAnnexure {

	private SiteABean siteABean;
	private SiteBBean siteBBean;
	private ChargeableItems chargeableItems;
	private Integer rowSpanCount;
	private String portBandwidth;
	private Integer rowSpanForSiteLevel;
	
	public NplSitewiseBillingAnnexure() {
		this.siteABean = new SiteABean();
		this.siteBBean = new SiteBBean();
		this.chargeableItems = new ChargeableItems();
	}
	public SiteABean getSiteABean() {
		return siteABean;
	}
	public void setSiteABean(SiteABean siteABean) {
		this.siteABean = siteABean;
	}
	public SiteBBean getSiteBBean() {
		return siteBBean;
	}
	public void setSiteBBean(SiteBBean siteBBean) {
		this.siteBBean = siteBBean;
	}
	public ChargeableItems getChargeableItems() {
		return chargeableItems;
	}
	public void setChargeableItems(ChargeableItems chargeableItems) {
		this.chargeableItems = chargeableItems;
	}
	public Integer getRowSpanCount() {
		return rowSpanCount;
	}
	public void setRowSpanCount(Integer rowSpanCount) {
		this.rowSpanCount = rowSpanCount;
	}
	public String getPortBandwidth() {
		return portBandwidth;
	}
	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
	public Integer getRowSpanForSiteLevel() {
		return rowSpanForSiteLevel;
	}
	public void setRowSpanForSiteLevel(Integer rowSpanForSiteLevel) {
		this.rowSpanForSiteLevel = rowSpanForSiteLevel;
	}
	
	
}
