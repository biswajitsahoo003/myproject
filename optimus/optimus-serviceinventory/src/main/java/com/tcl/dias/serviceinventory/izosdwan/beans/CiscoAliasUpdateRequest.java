package com.tcl.dias.serviceinventory.izosdwan.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Bean class to get request for CPE & template alias
 * 
 *
 */
public class CiscoAliasUpdateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String siteListAlias;
	private String siteListName;
	private List<Integer> siteListServiceIds;
	
	private String cpeAlias;
	private String cpeName;
	private List<Integer> cpeAssetIds;
	
	public String getSiteListAlias() {
		return siteListAlias;
	}
	public void setSiteListAlias(String siteListAlias) {
		this.siteListAlias = siteListAlias;
	}
	public String getSiteListName() {
		return siteListName;
	}
	public void setSiteListName(String siteListName) {
		this.siteListName = siteListName;
	}
	public List<Integer> getSiteListServiceIds() {
		return siteListServiceIds;
	}
	public void setSiteListServiceIds(List<Integer> siteListServiceIds) {
		this.siteListServiceIds = siteListServiceIds;
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
	@Override
	public String toString() {
		return "CiscoAliasUpdateRequest [siteListAlias=" + siteListAlias + ", siteListName=" + siteListName
				+ ", siteListServiceIds=" + siteListServiceIds + ", cpeAlias=" + cpeAlias + ", cpeName=" + cpeName
				+ ", cpeAssetIds=" + cpeAssetIds + "]";
	}
	
	
}
