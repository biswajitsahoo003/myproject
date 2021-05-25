package com.tcl.dias.auth.beans;

import java.util.List;

/**
 * 
 * @author Manojkumar R
 *
 */
public class CustomerLe {

	private Integer customerLeId;
	private String customerLeName;
	private List<EngagementBean> enagements;

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	public List<EngagementBean> getEnagements() {
		return enagements;
	}

	public void setEnagements(List<EngagementBean> enagements) {
		this.enagements = enagements;
	}

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

}
