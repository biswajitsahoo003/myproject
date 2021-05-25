package com.tcl.dias.serviceinventory.beans;


import java.io.Serializable;
import java.util.List;

/**
 * @author archchan
 *
 */
public class SdwanSiteDetailsBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SdwanSiteDetails> siteDetails;
	private Integer onlineCount = 0;
	private Integer offlineCount = 0;
	private Integer degradedCount = 0;
	private Integer totalItems;
	private Integer totalPages;
	private String timestamp;	
	
	public List<SdwanSiteDetails> getSiteDetails() {
		return siteDetails;
	}
	public void setSiteDetails(List<SdwanSiteDetails> siteDetails) {
		this.siteDetails = siteDetails;
	}
	public Integer getOnlineCount() {
		return onlineCount;
	}
	public void setOnlineCount(Integer onlineCount) {
		this.onlineCount = onlineCount;
	}
	public Integer getOfflineCount() {
		return offlineCount;
	}
	public void setOfflineCount(Integer offlineCount) {
		this.offlineCount = offlineCount;
	}
	public Integer getDegradedCount() {
		return degradedCount;
	}
	public void setDegradedCount(Integer degradedCount) {
		this.degradedCount = degradedCount;
	}
	public Integer getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
