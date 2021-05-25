package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;

public class SdwanTemplateDetails  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String templateName;
	private String sdwanServiceId;
	private Integer attributeId;
	private String underlayServiceId;
	
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getSdwanServiceId() {
		return sdwanServiceId;
	}
	public void setSdwanServiceId(String sdwanServiceId) {
		this.sdwanServiceId = sdwanServiceId;
	}
	public Integer getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}
	public String getUnderlayServiceId() {
		return underlayServiceId;
	}
	public void setUnderlayServiceId(String underlayServiceId) {
		this.underlayServiceId = underlayServiceId;
	}
	@Override
	public String toString() {
		return "SdwanTemplateDetails [templateName=" + templateName + ", sdwanServiceId=" + sdwanServiceId
				+ ", attributeId=" + attributeId + ", underlayServiceId=" + underlayServiceId + "]";
	}
	
}

