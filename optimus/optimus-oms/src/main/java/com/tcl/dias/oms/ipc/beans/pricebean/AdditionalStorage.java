
package com.tcl.dias.oms.ipc.beans.pricebean;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "size" })
public class AdditionalStorage {

	@JsonProperty("type")
	private String type;

	@JsonProperty("size")
	private String size;

	//IOPS
	private Integer performance;

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("size")
	public String getSize() {
		return size;
	}

	@JsonProperty("size")
	public void setSize(String size) {
		this.size = size;
	}

	public Integer getPerformance() {
		return performance;
	}

	public void setPerformance(Integer performance) {
		this.performance = performance;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("type", type).append("size", size).append("performance", performance).toString();
	}

}
