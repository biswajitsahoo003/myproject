package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation;

import java.util.List;

/**
 * Used for retrieving information of CPE and status of the same along with
 * pagination
 * 
 *
 */
public class CiscoCPEInformationBean {
	private List<CiscoCPEBean> CPE;
	private Integer totalItems;
	private Integer totalPages;
	private Integer onlineCpeCount;
	private Integer offlineCpeCount;
	private Integer degradedCpeCount;
	private String timestamp;

	public CiscoCPEInformationBean() {
		this.totalItems = 0;
		this.totalPages = 0;
		this.onlineCpeCount = 0;
		this.offlineCpeCount = 0;
		this.degradedCpeCount = 0;
	}

	public List<CiscoCPEBean> getCPE() {
		return CPE;
	}

	public void setCPE(List<CiscoCPEBean> CPE) {
		this.CPE = CPE;
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

	public Integer getOnlineCpeCount() {
		return onlineCpeCount;
	}

	public void setOnlineCpeCount(Integer onlineCpeCount) {
		this.onlineCpeCount = onlineCpeCount;
	}

	public Integer getOfflineCpeCount() {
		return offlineCpeCount;
	}

	public void setOfflineCpeCount(Integer offlineCpeCount) {
		this.offlineCpeCount = offlineCpeCount;
	}

	public Integer getDegradedCpeCount() {
		return degradedCpeCount;
	}

	public void setDegradedCpeCount(Integer degradedCpeCount) {
		this.degradedCpeCount = degradedCpeCount;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "CiscoCPEInformationBean [CPE=" + CPE + ", totalItems=" + totalItems + ", totalPages=" + totalPages
				+ ", onlineCpeCount=" + onlineCpeCount + ", offlineCpeCount=" + offlineCpeCount + ", degradedCpeCount="
				+ degradedCpeCount + ", timestamp=" + timestamp + "]";
	}

	
}