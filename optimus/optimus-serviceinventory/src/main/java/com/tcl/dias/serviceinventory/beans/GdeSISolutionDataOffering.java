package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * This file is to hold the service and component level attributes for NPL service.
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GdeSISolutionDataOffering {

    private Integer serviceId;
    
    private String linkType;

    private String siteType;

    private GdeSolutionAttributes attributes;
    
    private List<AttributeDetail> attributeDetail;

    private String siteClassification;
    
	/**
	 * @return the serviceId
	 */
	public Integer getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the linkType
	 */
	public String getLinkType() {
		return linkType;
	}

	/**
	 * @param linkType the linkType to set
	 */
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	/**
	 * @return the attributeDetail
	 */
	public List<AttributeDetail> getAttributeDetail() {
		return attributeDetail;
	}

	/**
	 * @param attributeDetail the attributeDetail to set
	 */
	public void setAttributeDetail(List<AttributeDetail> attributeDetail) {
		this.attributeDetail = attributeDetail;
	}

	/**
	 * @return the attributes
	 */
	public GdeSolutionAttributes getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(GdeSolutionAttributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the siteType
	 */
	public String getSiteType() {
		return siteType;
	}

	/**
	 * @param siteType the siteType to set
	 */
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getSiteClassification() {
		return siteClassification;
	}

	public void setSiteClassification(String siteClassification) {
		this.siteClassification = siteClassification;
	}
	
	

	
}
