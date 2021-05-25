
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
@JsonPropertyOrder({ "tableId", "tableName", "monitorType", "columnNames", "rows" })
public class AlarmSummary implements Serializable {

	@JsonProperty("tableId")
	private String tableId;
	@JsonProperty("tableName")
	private String tableName;
	@JsonProperty("monitorType")
	private String monitorType;
	@JsonProperty("columnNames")
	private List<String> columnNames = null;
	@JsonProperty("rows")
	private List<Row> rows = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 5315039083218816698L;

	@JsonProperty("tableId")
	public String getTableId() {
		return tableId;
	}

	@JsonProperty("tableId")
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	@JsonProperty("tableName")
	public String getTableName() {
		return tableName;
	}

	@JsonProperty("tableName")
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@JsonProperty("monitorType")
	public String getMonitorType() {
		return monitorType;
	}

	@JsonProperty("monitorType")
	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	@JsonProperty("columnNames")
	public List<String> getColumnNames() {
		return columnNames;
	}

	@JsonProperty("columnNames")
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	@JsonProperty("rows")
	public List<Row> getRows() {
		return rows;
	}

	@JsonProperty("rows")
	public void setRows(List<Row> rows) {
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
