package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * This file contains the SiteAttributeUpdateBean.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiteAttributeUpdateBean implements Serializable{
	private Integer siteId;
	private List<AttributeDetail> attributeDetails;
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public List<AttributeDetail> getAttributeDetails() {
		return attributeDetails;
	}
	public void setAttributeDetails(List<AttributeDetail> attributeDetails) {
		this.attributeDetails = attributeDetails;
	}	
	
	
}
