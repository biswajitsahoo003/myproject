package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpcComponentDetail implements Serializable {
	private static final long serialVersionUID = 7085974668204699696L;
	
	private String name;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private List<IpcAttributeDetail> attributes = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Double getArc() {
		return arc;
	}
	public void setArc(Double arc) {
		this.arc = arc;
	}
	public List<IpcAttributeDetail> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<IpcAttributeDetail> attributes) {
		this.attributes = attributes;
	}
}
