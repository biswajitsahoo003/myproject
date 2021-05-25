package com.tcl.dias.oms.ipc.beans.pricebean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "mrc", "nrc", "ppuRate", "asked_mrc", "asked_nrc", "asked_ppu_rate" })
public class Component {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("mrc")
	private Double mrc = 0D;

	@JsonProperty("nrc")
	private Double nrc = 0D;
	
	@JsonProperty("ppuRate")
	private Double ppuRate = 0D;

	@JsonProperty("asked_mrc")
	private Double askedMrc;
	
	@JsonProperty("asked_nrc")
	private Double askedNrc;
	
	@JsonProperty("asked_ppu_rate")
	private Double askedPpuRate;

	public Component() {
	}

	public Component(String name) {
		this.name = name;
	}

	public Component(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Component(Integer id, String name,Double askedMrc) {
		this.id = id;
		this.name = name;
		this.askedMrc = askedMrc;
	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("mrc")
	public Double getMrc() {
		return mrc;
	}

	@JsonProperty("mrc")
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	@JsonProperty("nrc")
	public Double getNrc() {
		return nrc;
	}

	@JsonProperty("nrc")
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getAskedMrc() {
		return askedMrc;
	}

	public void setAskedMrc(Double askedMrc) {
		this.askedMrc = askedMrc;
	}

	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

	public Double getAskedNrc() {
		return askedNrc;
	}

	public void setAskedNrc(Double askedNrc) {
		this.askedNrc = askedNrc;
	}

	public Double getAskedPpuRate() {
		return askedPpuRate;
	}

	public void setAskedPpuRate(Double askedPpuRate) {
		this.askedPpuRate = askedPpuRate;
	}

	@Override
	public String toString() {
		return "Component [id=" + id + ", name=" + name + ", mrc=" + mrc + ", nrc=" + nrc + ", ppuRate=" + ppuRate
				+ ", askedMrc=" + askedMrc + ", askedNrc=" + askedNrc + ", askedPpuRate=" + askedPpuRate + "]";
	}

}
