package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class ConfigurationCpeInfo {
	
	private String cpetype;
	
	private List<BandWidthSummaryCpeBean> cpeDetails;

	public String getCpetype() {
		return cpetype;
	}

	public void setCpetype(String cpetype) {
		this.cpetype = cpetype;
	}

	public List<BandWidthSummaryCpeBean> getCpeDetails() {
		return cpeDetails;
	}

	public void setCpeDetails(List<BandWidthSummaryCpeBean> cpeDetails) {
		this.cpeDetails = cpeDetails;
	}
	
	

}
