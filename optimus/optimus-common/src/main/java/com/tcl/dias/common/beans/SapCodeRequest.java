package com.tcl.dias.common.beans;

import java.util.List;

public class SapCodeRequest {
	
	private List<Integer> customerLeIds;
	
	private String type;

	private List<String> sapCodes;

	private String leType;

	public SapCodeRequest() {
	}

	public SapCodeRequest(List<Integer> customerLeIds, String type) {
		this.customerLeIds = customerLeIds;
		this.type = type;
	}

	public SapCodeRequest(List<Integer> customerLeIds, String type, String leType) {
		this.customerLeIds = customerLeIds;
		this.type = type;
		this.leType = leType;
	}

	public List<Integer> getCustomerLeIds() {
		return customerLeIds;
	}

	public void setCustomerLeIds(List<Integer> customerLeIds) {
		this.customerLeIds = customerLeIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getSapCodes() {
		return sapCodes;
	}

	public void setSapCodes(List<String> sapCodes) {
		this.sapCodes = sapCodes;
	}

	public String getLeType() {
		return leType;
	}

	public void setLeType(String leType) {
		this.leType = leType;
	}
}
