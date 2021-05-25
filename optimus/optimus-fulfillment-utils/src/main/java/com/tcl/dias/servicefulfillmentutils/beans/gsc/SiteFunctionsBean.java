package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteFunctionsBean {
	
	private String siteFunctionCd;

	public String getSiteFunctionCd() {
		return siteFunctionCd;
	}

	public void setSiteFunctionCd(String siteFunctionCd) {
		this.siteFunctionCd = siteFunctionCd;
	}
	
	

}
