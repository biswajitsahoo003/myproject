package com.tcl.dias.common.servicefulfillment.beans;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;

/**
 * Data transfer object for CPE BOM details
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class CpeBomDto {

	@NotNull(message = Constants.ID_NULL)
	private Integer id;

	private String bomName;

	private String uniCode;

	private Integer minBandwidth;

	private Integer maxBandwidth;
	
	private Double minBandwidthGvpn;

	private Double maxBandwidthGvpn;

	@NotNull(message = Constants.BOM_RESOURCES_EMPTY)
	private List<ResourceDto> resources;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getUniCode() {
		return uniCode;
	}

	public void setUniCode(String uniCode) {
		this.uniCode = uniCode;
	}

	public Integer getMinBandwidth() {
		return minBandwidth;
	}

	public void setMinBandwidth(Integer minBandwidth) {
		this.minBandwidth = minBandwidth;
	}

	public Integer getMaxBandwidth() {
		return maxBandwidth;
	}

	public void setMaxBandwidth(Integer maxBandwidth) {
		this.maxBandwidth = maxBandwidth;
	}

	public Double getMinBandwidthGvpn() {
		return minBandwidthGvpn;
	}

	public void setMinBandwidthGvpn(Double minBandwidthGvpn) {
		this.minBandwidthGvpn = minBandwidthGvpn;
	}

	public Double getMaxBandwidthGvpn() {
		return maxBandwidthGvpn;
	}

	public void setMaxBandwidthGvpn(Double maxBandwidthGvpn) {
		this.maxBandwidthGvpn = maxBandwidthGvpn;
	}

	public List<ResourceDto> getResources() {
		return resources;
	}

	public void setResources(List<ResourceDto> resources) {
		this.resources = resources;
	}

	
	
	

}
