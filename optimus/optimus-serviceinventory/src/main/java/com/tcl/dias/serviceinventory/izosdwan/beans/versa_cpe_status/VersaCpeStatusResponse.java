package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Bean for receiving and mapping CPE status response from Versa REST API
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "versanms.ApplianceStatusResult" })
public class VersaCpeStatusResponse {
	@JsonProperty("versanms.ApplianceStatusResult")
	VersanmsApplianceStatusResult applianceStatusResult;

	@JsonProperty("versanms.ApplianceStatusResult")
	public VersanmsApplianceStatusResult getApplianceStatusResult() {
		return applianceStatusResult;
	}

	@JsonProperty("versanms.ApplianceStatusResult")
	public void setApplianceStatusResult(VersanmsApplianceStatusResult applianceStatusResult) {
		this.applianceStatusResult = applianceStatusResult;
	}
}
