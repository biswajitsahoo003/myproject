
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Bean for receiving and mapping Traffic steering policy/rule response from
 * Versa REST API
 * 
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "definitionId" })
public class Actions implements Serializable {

	
	@JsonProperty("type")
	private String type;
	@JsonProperty("parameter")
    private Object parameter ;
//	@JsonProperty("parameter")
//	private List<Parameters> parameter;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
//	public List<Parameters> getParameter() {
//		return parameter;
//	}
//	public void setParameter(List<Parameters> parameter) {
//		this.parameter = parameter;
//	}
//	@Override
//	public String toString() {
//		return "Actions [type=" + type + ", parameter=" + parameter + "]";
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actions other = (Actions) obj;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
}
