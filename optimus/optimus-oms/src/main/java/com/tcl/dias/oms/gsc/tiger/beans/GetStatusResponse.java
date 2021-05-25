package com.tcl.dias.oms.gsc.tiger.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetStatusResponse extends TigerServiceResponse {
	private Integer startIndex;
	private Integer endIndex;
	private Integer totalCount;
	private List<GetOrderDetail> orderDetails;

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

	public List<GetOrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<GetOrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
}
