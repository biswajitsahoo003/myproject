package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwitchingUnitsBean {
	
	private String switchUnitCd;

	public String getSwitchUnitCd() {
		return switchUnitCd;
	}

	public void setSwitchUnitCd(String switchUnitCd) {
		this.switchUnitCd = switchUnitCd;
	}

}
