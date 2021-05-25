package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class GetInterConnectResponse extends TigerServiceResponse {
	private Integer totalCount;
	private Integer startIndex;
	private Integer endIndex;
	private List<InterConnectDetail> interconnectDetails;

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

	public List<InterConnectDetail> getInterconnectDetails() {
		return interconnectDetails;
	}

	public void setInterconnectDetails(List<InterConnectDetail> interconnectDetails) {
		this.interconnectDetails = interconnectDetails;
	}
}
