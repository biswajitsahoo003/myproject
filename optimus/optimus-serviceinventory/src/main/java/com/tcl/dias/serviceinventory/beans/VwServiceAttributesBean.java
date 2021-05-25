package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;

/**
 * service attributes and service Bean class 
 * @author archchan
 *
 */
public class VwServiceAttributesBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer sysId;
	private String serviceId;
	private Integer attributeId;
	private String attributeName;
	private String attributeValue;
	private String displayValue;
	private String izoSdwanSrvcId;
	private Integer underlaySysId;
	private String underlayServiceId;
	private Integer underlayAttributeId;
	private String underlayAttributeName;
	private String underlayAttributeValue;
	private String underlayDisplayValue;
	
	public Integer getSysId() {
		return sysId;
	}
	public void setSysId(Integer sysId) {
		this.sysId = sysId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
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
	public String getDisplayValue() {
		return displayValue;
	}
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	public String getIzoSdwanSrvcId() {
		return izoSdwanSrvcId;
	}
	public void setIzoSdwanSrvcId(String izoSdwanSrvcId) {
		this.izoSdwanSrvcId = izoSdwanSrvcId;
	}
	public Integer getUnderlaySysId() {
		return underlaySysId;
	}
	public void setUnderlaySysId(Integer underlaySysId) {
		this.underlaySysId = underlaySysId;
	}
	public String getUnderlayServiceId() {
		return underlayServiceId;
	}
	public void setUnderlayServiceId(String underlayServiceId) {
		this.underlayServiceId = underlayServiceId;
	}
	public Integer getUnderlayAttributeId() {
		return underlayAttributeId;
	}
	public void setUnderlayAttributeId(Integer underlayAttributeId) {
		this.underlayAttributeId = underlayAttributeId;
	}
	public String getUnderlayAttributeName() {
		return underlayAttributeName;
	}
	public void setUnderlayAttributeName(String underlayAttributeName) {
		this.underlayAttributeName = underlayAttributeName;
	}
	public String getUnderlayAttributeValue() {
		return underlayAttributeValue;
	}
	public void setUnderlayAttributeValue(String underlayAttributeValue) {
		this.underlayAttributeValue = underlayAttributeValue;
	}
	public String getUnderlayDisplayValue() {
		return underlayDisplayValue;
	}
	public void setUnderlayDisplayValue(String underlayDisplayValue) {
		this.underlayDisplayValue = underlayDisplayValue;
	}
	@Override
	public String toString() {
		return "VwServiceAttributesBean [sysId=" + sysId + ", serviceId=" + serviceId + ", attributeId=" + attributeId
				+ ", attributeName=" + attributeName + ", attributeValue=" + attributeValue + ", displayValue="
				+ displayValue + ", izoSdwanSrvcId=" + izoSdwanSrvcId + ", underlaySysId=" + underlaySysId
				+ ", underlayServiceId=" + underlayServiceId + ", underlayAttributeId=" + underlayAttributeId
				+ ", underlayAttributeName=" + underlayAttributeName + ", underlayAttributeValue="
				+ underlayAttributeValue + ", underlayDisplayValue=" + underlayDisplayValue + "]";
	}
	

}
