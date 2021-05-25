package com.tcl.dias.oms.ipc.beans;

public class CatalystResponse {

	private String status;
	
	private SecurityGroupCatalystResponse data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SecurityGroupCatalystResponse getData() {
		return data;
	}

	public void setData(SecurityGroupCatalystResponse data) {
		this.data = data;
	}
	
	
}
