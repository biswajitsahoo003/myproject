package com.tcl.dias.oms.ipc.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.ComponentDetail;

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

	private Integer cloudSolutionId;
	private String solutionCode;
	private String offeringName;
	private String dcCloudType;
	private String dcLocationId;
	private String image;
	private String bandwidth;
	private String bandwidthUnit;
	private String cloudCode;
	private String parentCloudCode;
	private List<ComponentDetail> components;
	private Integer isTaskTriggered;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;
	private Double ppuRate;

	public Integer getCloudSolutionId() { return cloudSolutionId; }

	public void setCloudSolutionId(Integer cloudSolutionId) { this.cloudSolutionId = cloudSolutionId; }

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

	public String getDcCloudType() {
		return dcCloudType;
	}

	public void setDcCloudType(String dcCloudType) {
		this.dcCloudType = dcCloudType;
	}

	public String getDcLocationId() {
		return dcLocationId;
	}

	public void setDcLocationId(String dcLocationId) {
		this.dcLocationId = dcLocationId;
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
	
	public Integer getIsTaskTriggered() {
		return isTaskTriggered;
	}

	public void setIsTaskTriggered(Integer isTaskTriggered) {
		this.isTaskTriggered = isTaskTriggered;
	}

	public Double getMrc() { return mrc; }

	public void setMrc(Double mrc) { this.mrc = mrc; }

	public Double getNrc() { return nrc; }

	public void setNrc(Double nrc) { this.nrc = nrc; }

	public Double getArc() { return arc; }

	public void setArc(Double arc) { this.arc = arc; }

	public Double getTcv() { return tcv; }

	public void setTcv(Double tcv) { this.tcv = tcv; }

	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getParentCloudCode() {
		return parentCloudCode;
	}

	public void setParentCloudCode(String parentCloudCode) {
		this.parentCloudCode = parentCloudCode;
	}

	@Override
	public String toString() {
		return "SolutionDetail [solutionCode=" + solutionCode + ", offeringName=" + offeringName + ", image=" + image
				+ ", bandwidth=" + bandwidth + ", bandwidthUnit=" + bandwidthUnit + ", components=" + components + "]";
	}

}
