package com.tcl.dias.servicefulfillmentutils.beans;

public class ComponentPdfBean {

	private String name;
	private String attributes;
	private Double mrc = Double.valueOf(0);
	private Double nrc = Double.valueOf(0);
	private Double arc = Double.valueOf(0);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
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

	@Override
	public String toString() {
		return "ComponentPdfBean [name=" + name + ", attributes=" + attributes + ", mrc=" + mrc + ", nrc=" + nrc
				+ ", arc=" + arc + "]";
	}

}
