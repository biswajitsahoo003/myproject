
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
@JsonPropertyOrder({ "columnNames", "rows" })
public class CpeHealth implements Serializable {

	@JsonProperty("columnNames")
	private List<String> columnNames = null;
	@JsonProperty("rows")
	private List<Row_> rows = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = -4268870867956079276L;

	@JsonProperty("columnNames")
	public List<String> getColumnNames() {
		return columnNames;
	}

	@JsonProperty("columnNames")
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	@JsonProperty("rows")
	public List<Row_> getRows() {
		return rows;
	}

	@JsonProperty("rows")
	public void setRows(List<Row_> rows) {
		this.rows = rows;
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
