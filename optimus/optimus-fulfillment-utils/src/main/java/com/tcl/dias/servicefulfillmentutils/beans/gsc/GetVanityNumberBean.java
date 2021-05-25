package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class GetVanityNumberBean {
	
	private String status;
	private String message;
	private Integer totalCount;
	private Integer startIndex;
	private Integer endIndex;
	private List<NumInvBean> numInv;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

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

	public List<NumInvBean> getNumInv() {
		return numInv;
	}

	public void setNumInv(List<NumInvBean> numInv) {
		this.numInv = numInv;
	}
}
