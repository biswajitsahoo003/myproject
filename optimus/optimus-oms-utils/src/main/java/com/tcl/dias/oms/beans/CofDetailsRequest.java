package com.tcl.dias.oms.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the CofDetailsRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CofDetailsRequest {

	@JsonProperty("moduleName")
	private String moduleName;
	@JsonProperty("dataJson")
	private List<CofDataJson> dataJson;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<CofDataJson> getDataJson() {
		return dataJson;
	}

	public void setDataJson(List<CofDataJson> dataJson) {
		this.dataJson = dataJson;
	}

}
