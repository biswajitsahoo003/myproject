package com.tcl.dias.products.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.BomMaster;
import com.tcl.dias.productcatelog.entity.entities.BomPhysicalResourceAssoc;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlDetailView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomView;

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

	public CpeBomDto() {

	}

	public CpeBomDto(BomMaster bomMaster) {
		if (bomMaster != null) {
			this.setId(bomMaster.getId());
			this.setUniCode(bomMaster.getBomName());
			this.setBomName(bomMaster.getBomName());
			List<BomPhysicalResourceAssoc> mappings = bomMaster.getBomPhysicalResourceAssocList();
			if (mappings != null && !mappings.isEmpty())
				this.setResources(mappings.stream().map(BomPhysicalResourceAssoc::getPhysicalResource)
						.map(ResourceDto::new).collect(Collectors.toList()));
		}
	}

	public CpeBomDto(CpeBomView cpeBomView) {
		if (cpeBomView != null) {
			this.setId(cpeBomView.getBomId());
			this.setUniCode(cpeBomView.getBomName());
			this.setBomName(cpeBomView.getBomName());
			this.setMaxBandwidth(cpeBomView.getMaxBandwidth());
			this.setMinBandwidth(cpeBomView.getMinBandwidth());
		}
	}

	public CpeBomDto(CpeBomGvpnView cpeBomView) {
		if (cpeBomView != null) {
			this.setUniCode(cpeBomView.getBomName());
			this.setBomName(cpeBomView.getBomName());
			this.setMaxBandwidthGvpn(cpeBomView.getMaxBwInMbps());
			this.setMinBandwidthGvpn(cpeBomView.getMinBwInMbps());
		}
	}
	
	public CpeBomDto(CpeBomGvpnIntlView cpeBomGvpnIntlView) {
		if (cpeBomGvpnIntlView != null) {
			this.setUniCode(cpeBomGvpnIntlView.getBomName());
			this.setBomName(cpeBomGvpnIntlView.getBomName());
			this.setMaxBandwidthGvpn(cpeBomGvpnIntlView.getMaxBwInMbps());
			this.setMinBandwidthGvpn(cpeBomGvpnIntlView.getMinBwInMbps());
		}
	}
	
	
	
	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ResourceDto> getResources() {
		return resources;
	}

	public void setResources(List<ResourceDto> resources) {
		this.resources = resources;
	}

	/**
	 * @return the uniCode
	 */
	public String getUniCode() {
		return uniCode;
	}

	/**
	 * @param uniCode
	 *            the uniCode to set
	 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bomName == null) ? 0 : bomName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CpeBomDto other = (CpeBomDto) obj;
		if (bomName == null) {
			if (other.bomName != null)
				return false;
		} else if (!bomName.equals(other.bomName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		return true;
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

	@Override
	public String toString() {
		return "CpeBomDto [id=" + id + ", bomName=" + bomName + ", uniCode=" + uniCode + ", minBandwidth="
				+ minBandwidth + ", maxBandwidth=" + maxBandwidth + ", minBandwidthGvpn=" + minBandwidthGvpn
				+ ", maxBandwidthGvpn=" + maxBandwidthGvpn + ", resources=" + resources + "]";
	}
	
	

}
