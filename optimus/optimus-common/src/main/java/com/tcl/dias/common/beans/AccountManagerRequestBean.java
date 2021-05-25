package com.tcl.dias.common.beans;

import java.io.Serializable;

public class AccountManagerRequestBean implements Serializable{
	private Integer userId;
	private Integer customerId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
}
