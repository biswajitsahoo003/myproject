
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Bean for receiving and mapping Traffic steering policy/rule response from
 * Versa REST API
 * 
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "definitionId" })
public class DataPolicyActions implements Serializable {

	
	@JsonProperty("type")
	private String type;
	@JsonProperty("parameter")
	private Object parameter;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
//	public String getParameter() {
//		return parameter;
//	}
//	public void setParameter(String parameter) {
//		this.parameter = parameter;
//	}
//	@Override
//	public String toString() {
//		return "DataPolicyActions [type=" + type + ", parameter=" + parameter + "]";
//	}
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	
		
}
