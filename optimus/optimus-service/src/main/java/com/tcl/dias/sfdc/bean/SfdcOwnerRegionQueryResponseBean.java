package com.tcl.dias.sfdc.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This class is used as a response bean in owner region
 * 
 *
 * @author Madhumiethaa Palanisamy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "status", "message", "data" })
public class SfdcOwnerRegionQueryResponseBean {
	@JsonProperty("status")
	private String status;

	@JsonProperty("message")
	private String message;

	@JsonProperty("data")
	private List<SfdcOwnerRegionQueryResponse> sfdcOwnerRegionQueryBean;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<SfdcOwnerRegionQueryResponse> getSfdcOwnerRegionQueryBean() {
		return sfdcOwnerRegionQueryBean;
	}

	public void setSfdcOwnerRegionQueryBean(List<SfdcOwnerRegionQueryResponse> sfdcOwnerRegionQueryBean) {
		this.sfdcOwnerRegionQueryBean = sfdcOwnerRegionQueryBean;
	}

}
