package com.tcl.dias.l2oworkflowutils.beans;
import java.util.List;

import com.tcl.dias.common.beans.Mf3DTaskResponse;
import com.tcl.dias.l2oworkflow.entity.entities.PreMfResponse;

public class PreMfResponseBean {
	private String customerName;
	private String siteAddress;
	List<Mf3DTaskResponse> taskResponse;
	private String feasibilityCode;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public List<Mf3DTaskResponse> getTaskResponse() {
		return taskResponse;
	}
	public void setTaskResponse(List<Mf3DTaskResponse> taskResponse) {
		this.taskResponse = taskResponse;
	}
	public String getFeasibilityCode() {
		return feasibilityCode;
	}
	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}
	
	
	
	
	
	
	
	
	
	
	

}