
package com.tcl.dias.pricingengine.ipc.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * This file contains the Result.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

	@JsonProperty("ILL_Port_ARC_Adjusted")
	private String iLLPortARCAdjusted;
	@JsonProperty("ILL_Port_NRC_Adjusted")
	private String iLLPortNRCAdjusted;
	@JsonProperty("site_id")
	private String siteId;
	@JsonProperty("additional_IP_ARC")
	private String additionalIPARC;
	@JsonProperty("additional_IP_MRC")
	private String additionalIPMRC;
	@JsonProperty("ILL_Port_MRC_Adjusted")
	private String iLLPortMRCAdjusted;
	

	public String getiLLPortARCAdjusted() {
		return iLLPortARCAdjusted;
	}

	public void setiLLPortARCAdjusted(String iLLPortARCAdjusted) {
		this.iLLPortARCAdjusted = iLLPortARCAdjusted;
	}

	public String getiLLPortNRCAdjusted() {
		return iLLPortNRCAdjusted;
	}

	public void setiLLPortNRCAdjusted(String iLLPortNRCAdjusted) {
		this.iLLPortNRCAdjusted = iLLPortNRCAdjusted;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getAdditionalIPARC() {
		return additionalIPARC;
	}

	public void setAdditionalIPARC(String additionalIPARC) {
		this.additionalIPARC = additionalIPARC;
	}

	public String getAdditionalIPMRC() {
		return additionalIPMRC;
	}

	public void setAdditionalIPMRC(String additionalIPMRC) {
		this.additionalIPMRC = additionalIPMRC;
	}

	public String getiLLPortMRCAdjusted() {
		return iLLPortMRCAdjusted;
	}

	public void setiLLPortMRCAdjusted(String iLLPortMRCAdjusted) {
		this.iLLPortMRCAdjusted = iLLPortMRCAdjusted;
	}

	@Override
	public String toString() {
		return "Result [iLLPortARCAdjusted=" + iLLPortARCAdjusted + ", iLLPortNRCAdjusted=" + iLLPortNRCAdjusted
				+ ", siteId=" + siteId + ", additionalIPARC=" + additionalIPARC + ", additionalIPMRC=" + additionalIPMRC
				+ ", iLLPortMRCAdjusted=" + iLLPortMRCAdjusted + "]";
	}

}
