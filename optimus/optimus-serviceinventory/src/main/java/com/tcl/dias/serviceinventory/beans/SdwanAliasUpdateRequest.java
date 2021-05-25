package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Bean class to get request for CPE & template alias
 * @author archchan
 *
 */
public class SdwanAliasUpdateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String templateAlias;
	private String templateName;
	private List<Integer> templateServiceIds;
	
	private String cpeAlias;
	private String cpeName;
	private List<Integer> cpeAssetIds;
	public String getTemplateAlias() {
		return templateAlias;
	}
	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public List<Integer> getTemplateServiceIds() {
		return templateServiceIds;
	}
	public void setTemplateServiceIds(List<Integer> templateServiceIds) {
		this.templateServiceIds = templateServiceIds;
	}
	public String getCpeAlias() {
		return cpeAlias;
	}
	public void setCpeAlias(String cpeAlias) {
		this.cpeAlias = cpeAlias;
	}
	public String getCpeName() {
		return cpeName;
	}
	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}
	public List<Integer> getCpeAssetIds() {
		return cpeAssetIds;
	}
	public void setCpeAssetIds(List<Integer> cpeAssetIds) {
		this.cpeAssetIds = cpeAssetIds;
	}
	
	
}
