package com.tcl.dias.serviceinventory.renewals;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class RenewalsSite {
	private String offeringName;
	private String image;
	private String bandwidth;
	private String bandwidthUnit;
	private List<RenewalsSiteDetail> site = new ArrayList<>();
	// Adding for cross connect link count
	private Integer linkCount;
	private String serviceId;;

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

	public List<RenewalsSiteDetail> getSite() {
		return site;
	}

	public void setSite(List<RenewalsSiteDetail> site) {
		this.site = site;
	}

	public Integer getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(Integer linkCount) {
		this.linkCount = linkCount;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "Site [offeringName=" + offeringName + ", image=" + image + ", bandwidth=" + bandwidth
				+ ", bandwidthUnit=" + bandwidthUnit + ", site=" + site + ", linkCount=" + linkCount + ", serviceId="
				+ serviceId + "]";
	}



}
