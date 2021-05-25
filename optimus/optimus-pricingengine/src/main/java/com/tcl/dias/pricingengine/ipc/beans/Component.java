package com.tcl.dias.pricingengine.ipc.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "mrc", "nrc" })
public class Component {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("mrc")
	private Double mrc = 0D;

	@JsonProperty("asked_mrc")
	private Double askedMrc;

	@JsonProperty("nrc")
	private Double nrc = 0D;

	public Component() {
	}

	public Component(String name) {
		this.name = name;
	}

	public Component(Integer id, String name) {
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return "Component [id=" + id + ", name=" + name + ", mrc=" + mrc + ", askedMrc=" + askedMrc + ", nrc=" + nrc
				+ "]";
	}

}
