package com.tcl.dias.oms.beans;

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
	private String dcCloudType;
	private String parentCloudCode;
	private List<ComponentDetail> components;
	private String macdAdditionalIpFlag;
	private String macdIpv4PoolSize;
	private String macdIpv6PoolSize;
	private Integer macdIpv4Count;
	private Integer macdIpv6Count;
	private String additionalIpFlag;

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

	public String getDcCloudType() {
		return dcCloudType;
	}

	public void setDcCloudType(String dcCloudType) {
		this.dcCloudType = dcCloudType;
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
	
	public String getParentCloudCode() { 
		return parentCloudCode; 
	}
	
	public void setParentCloudCode(String parentCloudCode) { 
		this.parentCloudCode = parentCloudCode; 
	}

	public String getMacdAdditionalIpFlag() {
		return macdAdditionalIpFlag;
	}

	public void setMacdAdditionalIpFlag(String macdAdditionalIpFlag) {
		this.macdAdditionalIpFlag = macdAdditionalIpFlag;
	}
	
	public String getMacdIpv4PoolSize() {
		return macdIpv4PoolSize;
	}

	public void setMacdIpv4PoolSize(String macdIpv4PoolSize) {
		this.macdIpv4PoolSize = macdIpv4PoolSize;
	}

	public String getMacdIpv6PoolSize() {
		return macdIpv6PoolSize;
	}

	public void setMacdIpv6PoolSize(String macdIpv6PoolSize) {
		this.macdIpv6PoolSize = macdIpv6PoolSize;
	}

	public Integer getMacdIpv4Count() {
		return macdIpv4Count;
	}

	public void setMacdIpv4Count(Integer macdIpv4Count) {
		this.macdIpv4Count = macdIpv4Count;
	}

	public Integer getMacdIpv6Count() {
		return macdIpv6Count;
	}

	public void setMacdIpv6Count(Integer macdIpv6Count) {
		this.macdIpv6Count = macdIpv6Count;
	}

	public String getAdditionalIpFlag() {
		return additionalIpFlag;
	}

	public void setAdditionalIpFlag(String additionalIpFlag) {
		this.additionalIpFlag = additionalIpFlag;
	}

	@Override
	public String toString() {
		return "SolutionDetail [offeringName=" + offeringName + ", image=" + image + ", bandwidth=" + bandwidth
				+ ", bandwidthUnit=" + bandwidthUnit + ", dcLocationId=" + dcLocationId + ", parentCloudCode="
				+ parentCloudCode + ", components=" + components + ", macdAdditionalIpFlag=" + macdAdditionalIpFlag
				+ ", macdIpv4PoolSize=" + macdIpv4PoolSize + ", macdIpv6PoolSize=" + macdIpv6PoolSize
				+ ", macdIpv4Count=" + macdIpv4Count + ", macdIpv6Count=" + macdIpv6Count + ", additionalIpFlag="
				+ additionalIpFlag + "]";
	}

}
