package com.tcl.dias.common.beans;

import java.io.Serializable;

public class UserGroupCustomerLegalEntityBean implements Serializable{
	private Integer legalEntityId;
	private Integer customerId;
	public Integer getLegalEntityId() {
		return legalEntityId;
	}
	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
}
