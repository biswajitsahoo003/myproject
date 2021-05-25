package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * This file contains the SolutionDetail.java class. Bean class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolutionDetail implements Serializable {

	private static final long serialVersionUID = 5042900815778142832L;

	private String offeringName;
	private String image;
	private String bandwidth;
	private String bandwidthUnit;
	private String dcLocationId;
	private String parentCloudCode;
	private List<ComponentDetail> components;

	/**
	 * @return the offeringName
	 */
	public String getOfferingName() {
		return offeringName;
	}

	/**
	 * @param offeringName
	 *            the offeringName to set
	 */
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the bandwidth
	 */
	public String getBandwidth() {
		return bandwidth;
	}

	/**
	 * @param bandwidth
	 *            the bandwidth to set
	 */
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	/**
	 * @return the bandwidthUnit
	 */
	public String getBandwidthUnit() {
		return bandwidthUnit;
	}

	/**
	 * @param bandwidthUnit
	 *            the bandwidthUnit to set
	 */
	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

	public String getDcLocationId() { return dcLocationId; }

	public void setDcLocationId(String dcLocationId) { this.dcLocationId = dcLocationId; }

	/**
	 * @return the components
	 */
	public List<ComponentDetail> getComponents() {
		return components;
	}

	/**
	 * @param components
	 *            the components to set
	 */
	public void setComponents(List<ComponentDetail> components) {
		this.components = components;
	}
	
	public String getParentCloudCode() { 
		return parentCloudCode; 
	}
	
	public void setParentCloudCode(String parentCloudCode) { 
		this.parentCloudCode = parentCloudCode; 
	}
	
	@Override
	public String toString() {
		return "SolutionDetail [offeringName=" + offeringName + ", image=" + image + ", bandwidth=" + bandwidth
				+ ", bandwidthUnit=" + bandwidthUnit + ", components=" + components + "]";
	}

}
