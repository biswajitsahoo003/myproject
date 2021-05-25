package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ExcelBean implements Serializable {
	private String lrSection;

	private Integer siteId;
	
	private Integer siteAId;
	
	private Integer siteBId;

	
	private String attributeName;

	private String attributeValue;

	private Integer order;
	
	private Integer linkId;
	
	private Integer gscQuoteId;
	
	private Integer gscQuoteDetailId;
	
	private String actionType;
	

	
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public String getLrSection() {
		return lrSection;
	}

	public void setLrSection(String lrSection) {
		this.lrSection = lrSection;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Integer getSiteAId() {
		return siteAId;
	}

	public void setSiteAId(Integer siteAId) {
		this.siteAId = siteAId;
	}

	public Integer getSiteBId() {
		return siteBId;
	}

	public void setSiteBId(Integer siteBId) {
		this.siteBId = siteBId;
	}
	
	public Integer getGscQuoteId() {
		return gscQuoteId;
	}

	public void setGscQuoteId(Integer gscQuoteId) {
		this.gscQuoteId = gscQuoteId;
	}

	public Integer getGscQuoteDetailId() {
		return gscQuoteDetailId;
	}

	public void setGscQuoteDetailId(Integer gscQuoteDetailId) {
		this.gscQuoteDetailId = gscQuoteDetailId;
	}


	public ExcelBean() {

	}

	public ExcelBean(String lrSection, String attributeName, String attributeValue) {
		this.lrSection = lrSection;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}
	
	public ExcelBean(String lrSection, String attributeName, String attributeValue,String actionType) {
		this.lrSection = lrSection;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
		this.actionType = actionType;
	}

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
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	
	

	/**
	 * hashCode
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
		return result;
	}

	/**
	 * equals
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExcelBean other = (ExcelBean) obj;
		if (attributeName == null) {
			if (other.attributeName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		return true;
	}
	
	

}
