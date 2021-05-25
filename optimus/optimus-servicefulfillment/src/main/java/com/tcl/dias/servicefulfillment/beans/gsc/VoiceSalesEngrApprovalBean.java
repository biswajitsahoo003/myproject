package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.gsc.TrunkBean;

public class VoiceSalesEngrApprovalBean {

	private List<Integer> id;
	private String status;
	private String remarks;
	private List<TrunkBean> trunks;

	public List<TrunkBean> getTrunks() {
		return trunks;
	}

	public void setTrunks(List<TrunkBean> trunks) {
		this.trunks = trunks;
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

}
