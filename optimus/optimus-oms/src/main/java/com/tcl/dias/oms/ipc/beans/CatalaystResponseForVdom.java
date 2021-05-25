package com.tcl.dias.oms.ipc.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalaystResponseForVdom {

	@JsonProperty("vdomDetails")
	private List<VdomDetail> vdomDetails;

	public List<VdomDetail> getVdomDetails() {
		return vdomDetails;
	}

	public void setVdomDetails(List<VdomDetail> vdomDetails) {
		this.vdomDetails = vdomDetails;
	}
	
}
