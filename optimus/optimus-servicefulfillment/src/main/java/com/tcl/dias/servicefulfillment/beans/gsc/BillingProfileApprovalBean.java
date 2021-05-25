package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class BillingProfileApprovalBean {
	
	private List<Integer> id;
	private String status;
	private String remarks;
	private List<String> physicalAddressId;
	
	public List<String> getPhysicalAddressId() {
		return physicalAddressId;
	}

	public void setPhysicalAddressId(List<String> physicalAddressId) {
		this.physicalAddressId = physicalAddressId;
	}

	public List<Integer> getId() {
		return id;
	}

	public void setId(List<Integer> id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "BillingProfileApprovalBean [id=" + id + ", status=" + status + ", remarks=" + remarks + "]";
	}
	
}
