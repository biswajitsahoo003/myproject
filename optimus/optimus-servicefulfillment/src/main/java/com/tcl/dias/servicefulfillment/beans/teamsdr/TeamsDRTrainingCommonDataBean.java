package com.tcl.dias.servicefulfillment.beans.teamsdr;

/**
 * TeamsDR Training common data bean
 *
 * @author srraghav
 */
public class TeamsDRTrainingCommonDataBean {
	private String tataProjectManagerName;
	private String tataProjectManagedID;
	private String customerSuccessManagerName;
	private String customerSuccessManagerID;

	public String getTataProjectManagerName() {
		return tataProjectManagerName;
	}

	public void setTataProjectManagerName(String tataProjectManagerName) {
		this.tataProjectManagerName = tataProjectManagerName;
	}

	public String getTataProjectManagedID() {
		return tataProjectManagedID;
	}

	public void setTataProjectManagedID(String tataProjectManagedID) {
		this.tataProjectManagedID = tataProjectManagedID;
	}

	public String getCustomerSuccessManagerName() {
		return customerSuccessManagerName;
	}

	public void setCustomerSuccessManagerName(String customerSuccessManagerName) {
		this.customerSuccessManagerName = customerSuccessManagerName;
	}

	public String getCustomerSuccessManagerID() {
		return customerSuccessManagerID;
	}

	public void setCustomerSuccessManagerID(String customerSuccessManagerID) {
		this.customerSuccessManagerID = customerSuccessManagerID;
	}
}
