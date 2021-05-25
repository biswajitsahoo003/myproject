package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class BulkActiveBillingProfileResponse extends TigerServiceResponse {
	private Integer startIndex;
	private Integer endIndex;
	private Integer totalCount;
	private List<BillingProfile> billingProfiles;

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<BillingProfile> getBillingProfiles() {
		return billingProfiles;
	}

	public void setBillingProfiles(List<BillingProfile> billingProfiles) {
		this.billingProfiles = billingProfiles;
	}
}
