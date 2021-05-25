
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstColumnValue", "columnValues" })
public class Row implements Serializable {

	@JsonProperty("firstColumnValue")
	private String firstColumnValue;
	@JsonProperty("columnValues")
	private List<Integer> columnValues = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 8399709198662642797L;

	@JsonProperty("firstColumnValue")
	public String getFirstColumnValue() {
		return firstColumnValue;
	}

	@JsonProperty("firstColumnValue")
	public void setFirstColumnValue(String firstColumnValue) {
		this.firstColumnValue = firstColumnValue;
	}

	@JsonProperty("columnValues")
	public List<Integer> getColumnValues() {
		return columnValues;
	}

	@JsonProperty("columnValues")
	public void setColumnValues(List<Integer> columnValues) {
		this.columnValues = columnValues;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
