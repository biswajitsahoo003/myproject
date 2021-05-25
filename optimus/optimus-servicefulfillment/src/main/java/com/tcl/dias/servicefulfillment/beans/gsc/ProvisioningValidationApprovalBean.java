package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class ProvisioningValidationApprovalBean {
	
	private List<Integer> id;
	private String status;
	private String remarks;

	private String temprorayOutpulseConfig;


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

	public String getTemprorayOutpulseConfig() {
		return temprorayOutpulseConfig;
	}

	public void setTemprorayOutpulseConfig(String temprorayOutpulseConfig) {
		this.temprorayOutpulseConfig = temprorayOutpulseConfig;
	}

	@Override
	public String toString() {
		return "DocumentRemarksProvisioningValidation [id=" + id + ", status=" + status + ", remarks=" + remarks + "]";
	}

}
