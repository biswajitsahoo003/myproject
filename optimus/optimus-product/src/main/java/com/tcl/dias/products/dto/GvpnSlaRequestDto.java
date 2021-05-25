package com.tcl.dias.products.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;

/**
 * Request object for GVPN SLA API
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)

public class GvpnSlaRequestDto {
	
	@NotNull(message = Constants.SLT_VARIANT_NULL)
	private String sltVariant;
	
	private Integer sltVariantId;
	
	@NotNull(message = Constants.ACCESS_TOPOLOGY_NULL)
	private String accessTopology;
	
	private Integer accessTopologyId;

	
	@NotNull(message = Constants.POP_SRC_NULL)
	private String popRegionSourceId;
	
	private String popRegionSource;

	
	@NotNull(message = Constants.POP_DEST_NULL)
	private String popRegionDestId;
	
	private String popRegionDest;

	
	@NotNull(message = Constants.NO_OF_PORTS_NULL)
	private Integer noOfPortsId;
	
	private String noOfPorts;

	
	@NotNull(message = Constants.NO_OF_CPE_NULL)
	private Integer noOfCpe;
	
	private Integer noOfCpeId;

	
	@NotNull(message = Constants.LOCAL_LOOP_TYPE_NULL)
	private String localLoopType;
	
	private Integer localLoopTypeId;
	
	@NotNull(message = Constants.COS_VALUE_NULL)
	private Integer cosValue;
	
	@NotNull(message = Constants.COS_SCHEME_NULL)
	private String cosScheme;
	

	
	public String getSltVariant() {
		return sltVariant;
	}
	public void setSltVariant(String sltVariant) {
		this.sltVariant = sltVariant;
	}
	public String getAccessTopology() {
		return accessTopology;
	}
	public void setAccessTopology(String accessTopology) {
		this.accessTopology = accessTopology;
	}

	public Integer getNoOfCpe() {
		return noOfCpe;
	}
	public void setNoOfCpe(Integer noOfCpe) {
		this.noOfCpe = noOfCpe;
	}
	public String getLocalLoopType() {
		return localLoopType;
	}
	public void setLocalLoopType(String localLoopType) {
		this.localLoopType = localLoopType;
	}
	public Integer getSltVariantId() {
		return sltVariantId;
	}
	public void setSltVariantId(Integer sltVariantId) {
		this.sltVariantId = sltVariantId;
	}
	public Integer getAccessTopologyId() {
		return accessTopologyId;
	}
	public void setAccessTopologyId(Integer accessTopologyId) {
		this.accessTopologyId = accessTopologyId;
	}
	
	public String getPopRegionSource() {
		return popRegionSource;
	}
	public void setPopRegionSource(String popRegionSource) {
		this.popRegionSource = popRegionSource;
	}
	
	public String getPopRegionDest() {
		return popRegionDest;
	}
	public void setPopRegionDest(String popRegionDest) {
		this.popRegionDest = popRegionDest;
	}
	public Integer getNoOfPortsId() {
		return noOfPortsId;
	}
	public void setNoOfPortsId(Integer noOfPortsId) {
		this.noOfPortsId = noOfPortsId;
	}
	public String getNoOfPorts() {
		return noOfPorts;
	}
	public void setNoOfPorts(String noOfPorts) {
		this.noOfPorts = noOfPorts;
	}
	public Integer getNoOfCpeId() {
		return noOfCpeId;
	}
	public void setNoOfCpeId(Integer noOfCpeId) {
		this.noOfCpeId = noOfCpeId;
	}
	public Integer getLocalLoopTypeId() {
		return localLoopTypeId;
	}
	public void setLocalLoopTypeId(Integer localLoopTypeId) {
		this.localLoopTypeId = localLoopTypeId;
	}
	public String getPopRegionSourceId() {
		return popRegionSourceId;
	}
	public void setPopRegionSourceId(String popRegionSourceId) {
		this.popRegionSourceId = popRegionSourceId;
	}
	public String getPopRegionDestId() {
		return popRegionDestId;
	}
	public void setPopRegionDestId(String popRegionDestId) {
		this.popRegionDestId = popRegionDestId;
	}
	

	
	@Override
	public String toString() {
		return "GvpnSlaRequestDto [sltVariant=" + sltVariant + ", sltVariantId=" + sltVariantId + ", accessTopology="
				+ accessTopology + ", accessTopologyId=" + accessTopologyId + ", popRegionSourceId=" + popRegionSourceId
				+ ", popRegionSource=" + popRegionSource + ", popRegionDestId=" + popRegionDestId + ", popRegionDest="
				+ popRegionDest + ", noOfPortsId=" + noOfPortsId + ", noOfPorts=" + noOfPorts + ", noOfCpe=" + noOfCpe
				+ ", noOfCpeId=" + noOfCpeId + ", localLoopType=" + localLoopType + ", localLoopTypeId="
				+ localLoopTypeId + ", cosValue=" + cosValue + ", cosScheme=" + cosScheme + "]";
	}

	public String getCosScheme() {
		return cosScheme;
	}
	public void setCosScheme(String cosScheme) {
		this.cosScheme = cosScheme;
	}
	
	public Integer getCosValue() {
		return cosValue;
	}
	public void setCosValue(Integer cosValue) {
		this.cosValue = cosValue;
	}
	
	
	

}
