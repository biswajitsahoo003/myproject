package com.tcl.dias.common.beans;

import java.io.Serializable;

public class CustomerLeListBean implements Serializable{
	private Integer customerId;
	private Integer leId;
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getLeId() {
		return leId;
	}
	public void setLeId(Integer leId) {
		this.leId = leId;
	}
	
}
