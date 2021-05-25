package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation;

import java.util.Set;

/**
 * Bean for SDWAN template
 * 
 * 
 */
public class CiscoSiteListBean {
	private Integer attributeId;
	private String listName;
	private String listId;
	private Set<String> siteId;

	public CiscoSiteListBean() {
	}

	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}
	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}

	public Set<String> getSiteId() {
		return siteId;
	}

	public void setSiteId(Set<String> siteId) {
		this.siteId = siteId;
	}

	@Override
	public String toString() {
		return "CiscoSiteListBean [attributeId=" + attributeId + ", listName=" + listName + ", listId=" + listId
				+ ", siteId=" + siteId + "]";
	}


	}