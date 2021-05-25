
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;
import java.util.HashMap;
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
public class Parameters implements Serializable {

	@JsonProperty("field")
	private String field;
	@JsonProperty("ref")
	private String ref;
	@JsonProperty("value")
	private String value;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "SequenceEntries [field=" + field + ", ref=" + ref + "]";
	}
	

	
}
