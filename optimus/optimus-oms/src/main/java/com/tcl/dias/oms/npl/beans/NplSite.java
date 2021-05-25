package com.tcl.dias.oms.npl.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.npl.constants.SiteTypeConstants;

/**
 *
 * This file contains the SolutionDetail.java class. Bean class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class NplSite {
	private String offeringName;
	private String image;
	private String bandwidth;
	private String bandwidthUnit;
	private List<NplSiteDetail> site = new ArrayList<>();
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

	public List<NplSiteDetail> getSite() {
		return site;
	}

	public void setSite(List<NplSiteDetail> site) {
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
		return "NplSite{" + "offeringName='" + offeringName + '\'' + ", image='" + image + '\'' + ", bandwidth='"
				+ bandwidth + '\'' + ", bandwidthUnit='" + bandwidthUnit + '\'' + ", site=" + site + ", type=" + type
				+ '}';
	}
}
