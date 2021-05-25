package com.tcl.dias.oms.gde.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.npl.constants.SiteTypeConstants;

@JsonInclude(Include.NON_NULL)
public class GdeSite {
	
	private String offeringName;
	private String image;
	private String bandwidth;
	private String bandwidthUnit;
	private List<GdeSiteDetail> site = new ArrayList<>();
	private SiteTypeConstants type;
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getBandwidthUnit() {
		return bandwidthUnit;
	}
	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}
	public List<GdeSiteDetail> getSite() {
		return site;
	}
	public void setSite(List<GdeSiteDetail> site) {
		this.site = site;
	}
	public SiteTypeConstants getType() {
		return type;
	}
	public void setType(SiteTypeConstants type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "GdeSite [offeringName=" + offeringName + ", image=" + image + ", bandwidth=" + bandwidth
				+ ", bandwidthUnit=" + bandwidthUnit + ", site=" + site + ", type=" + type + ", getOfferingName()="
				+ getOfferingName() + ", getImage()=" + getImage() + ", getBandwidth()=" + getBandwidth()
				+ ", getBandwidthUnit()=" + getBandwidthUnit() + ", getSite()=" + getSite() + ", getType()=" + getType()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
