package com.tcl.dias.oms.izopc.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.SiteDetail;

/**
 * 
 * Bean class to hold solution information for IZOPC
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolutionDetail implements Serializable {

	private static final long serialVersionUID = 5042900815778142832L;

	private String solutionCode;
	private String offeringName;
	private String image;
	private String bandwidth;
	private String bandwidthUnit;
	private List<ComponentDetail> components;
	private List<SiteDetail> siteDetail;

	
	

	/**
	 * @return
	 */
	public String getSolutionCode() {
		return solutionCode;
	}

	/**
	 * @param solutionCode
	 */
	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

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
	

	/**
	 * @return the siteDetail
	 */
	public List<SiteDetail> getSiteDetail() {
		return siteDetail;
	}

	/**
	 * @param siteDetail the siteDetail to set
	 */
	public void setSiteDetail(List<SiteDetail> siteDetail) {
		this.siteDetail = siteDetail;
	}

	@Override
	public String toString() {
		return "SolutionDetail [solutionCode=" + solutionCode + ", offeringName=" + offeringName + ", image=" + image
				+ ", bandwidth=" + bandwidth + ", bandwidthUnit=" + bandwidthUnit + ", components=" + components + "]";
	}

}
