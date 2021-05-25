package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class EntmmValidationApprovalBean extends TaskDetailsBaseBean {
	
	private List<EntmmApprovalDocBean> entmmDetails;
	private String status;
	private String remarks;

	public List<EntmmApprovalDocBean> getEntmmDetails() {
		return entmmDetails;
	}

	public void setEntmmDetails(List<EntmmApprovalDocBean> entmmDetails) {
		this.entmmDetails = entmmDetails;
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
}
