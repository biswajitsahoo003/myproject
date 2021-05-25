package com.tcl.dias.oms.beans;

import java.util.List;

public class DiscountComponent {

	private String name;
	private String type;
	private Double arc;
	private Double nrc;
	private Double mrc;
	private int componentId;
	private Double tcv;
	
	
	
	
	
	

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public int getComponentId() {
		return componentId;
	}

	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	private List<DiscountAttribute> attributes;

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DiscountAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<DiscountAttribute> attributes) {
		this.attributes = attributes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
