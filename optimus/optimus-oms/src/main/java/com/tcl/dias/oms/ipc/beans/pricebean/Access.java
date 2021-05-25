
package com.tcl.dias.oms.ipc.beans.pricebean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "itemId", "type", "limit", "nrc", "mrc", "accessTypeComponentId" })
public class Access {

	@JsonProperty("itemId")
	private String itemId;
	@JsonProperty("type")
	private String type;
	@JsonProperty("limit")
	private String limit;
	@JsonProperty("nrc")
	private Double nrc;
	@JsonProperty("mrc")
	private Double mrc;
	@JsonProperty("asked_mrc")
	private Double askedMrc;
	@JsonProperty("accessTypeComponentId")
	private Integer acccessTypeComponentId;

	@JsonProperty("itemId")
	public String getItemId() {
		return itemId;
	}

	@JsonProperty("itemId")
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("limit")
	public String getLimit() {
		return limit;
	}

	@JsonProperty("limit")
	public void setLimit(String limit) {
		this.limit = limit;
	}

	@JsonProperty("nrc")
	public Double getNrc() {
		return nrc;
	}

	@JsonProperty("nrc")
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	@JsonProperty("mrc")
	public Double getMrc() {
		return mrc;
	}

	@JsonProperty("mrc")
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getAskedMrc() {
		return askedMrc;
	}

	public void setAskedMrc(Double askedMrc) {
		this.askedMrc = askedMrc;
	}

	@JsonProperty("accessTypeComponentId")
	public Integer getAcccessTypeComponentId() {
		return acccessTypeComponentId;
	}

	@JsonProperty("accessTypeComponentId")
	public void setAcccessTypeComponentId(Integer acccessTypeComponentId) {
		this.acccessTypeComponentId = acccessTypeComponentId;
	}

	@Override
	public String toString() {
		return "Access [itemId=" + itemId + ", type=" + type + ", limit=" + limit + ", nrc=" + nrc + ", mrc=" + mrc
				+ ", askedMrc=" + askedMrc + "]";
	}

}
