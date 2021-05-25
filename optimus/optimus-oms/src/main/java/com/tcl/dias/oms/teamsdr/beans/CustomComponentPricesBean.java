package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TeamsDR Custom Component Prices
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomComponentPricesBean {

	@JsonProperty("name")
	private String name;

	@JsonProperty("unit_mrc")
	private Double unitMrc;

	@JsonProperty("unit_nrc")
	private Double unitNrc;

	@JsonProperty("mrc")
	private Double mrc;

	@JsonProperty("nrc")
	private Double nrc;

	@JsonProperty("usage")
	private Double usage;

	@JsonProperty("overage")
	private Double overage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getUnitMrc() {
		return unitMrc;
	}

	public void setUnitMrc(Double unitMrc) {
		this.unitMrc = unitMrc;
	}

	public Double getUnitNrc() {
		return unitNrc;
	}

	public void setUnitNrc(Double unitNrc) {
		this.unitNrc = unitNrc;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getUsage() {
		return usage;
	}

	public void setUsage(Double usage) {
		this.usage = usage;
	}

	public Double getOverage() {
		return overage;
	}

	public void setOverage(Double overage) {
		this.overage = overage;
	}
}
