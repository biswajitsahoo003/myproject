package com.tcl.dias.serviceinventory.beans;

/**
 * Bean for SDWAN template
 * 
 * @author Srinivasa Raghavan
 */
public class SdwanTemplateBean {
	private Integer templateId;
	private String templateName;

	public SdwanTemplateBean() {
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public String toString() {
		return "SdwanTemplateBean{" + "templateId=" + templateId + ", templateName='" + templateName + '\'' + '}';
	}
}