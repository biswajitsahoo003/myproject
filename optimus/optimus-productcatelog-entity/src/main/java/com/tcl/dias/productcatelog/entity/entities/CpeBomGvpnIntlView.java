package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Immutable;


/**
 * The persistent class for the vw_cpe_bom_GVPN_INTL database table.
 * 
 */
@Entity
@Immutable
@Table(name="vw_cpe_bom_GVPN_INTL")
@IdClass(CpeBomGvpnIntlViewId.class)
public class CpeBomGvpnIntlView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="bom_name")
	private String bomName;

	@Id
	@Column(name="cpe_management_option")
	private String cpeManagementOption;

	@Id
	@Column(name="cpe_service_config")
	private String cpeServiceConfig;

	@Id
	@Column(name="license_type")
	private String licenseType;
	
	@Id
	@Column(name="max_bw")
	private int maxBw;

	@Column(name="max_bw_uom_cd")
	private String maxBwUomCd;
	
	@Id
	@Column(name="min_bw")
	private int minBw;
	
	@Id
	@Column(name="min_bw_uom_cd")
	private String minBwUomCd;

	@Id
	@Column(name="port_interface")
	private String portInterface;

	@Id
	@Column(name="routing_protocol")
	private String routingProtocol;

	@Id
	@Column(name="min_bw_in_mbps")
	private Double minBwInMbps;
	
	@Id
	@Column(name="max_bw_in_mbps")
	private Double maxBwInMbps;
	
	
    public CpeBomGvpnIntlView() {
    }

	public String getBomName() {
		return this.bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getCpeManagementOption() {
		return this.cpeManagementOption;
	}

	public void setCpeManagementOption(String cpeManagementOption) {
		this.cpeManagementOption = cpeManagementOption;
	}

	public String getCpeServiceConfig() {
		return this.cpeServiceConfig;
	}

	public void setCpeServiceConfig(String cpeServiceConfig) {
		this.cpeServiceConfig = cpeServiceConfig;
	}

	public String getLicenseType() {
		return this.licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public int getMaxBw() {
		return this.maxBw;
	}

	public void setMaxBw(int maxBw) {
		this.maxBw = maxBw;
	}

	public String getMaxBwUomCd() {
		return this.maxBwUomCd;
	}

	public void setMaxBwUomCd(String maxBwUomCd) {
		this.maxBwUomCd = maxBwUomCd;
	}

	public int getMinBw() {
		return this.minBw;
	}

	public void setMinBw(int minBw) {
		this.minBw = minBw;
	}

	public String getMinBwUomCd() {
		return this.minBwUomCd;
	}

	public void setMinBwUomCd(String minBwUomCd) {
		this.minBwUomCd = minBwUomCd;
	}

	public String getPortInterface() {
		return this.portInterface;
	}

	public void setPortInterface(String portInterface) {
		this.portInterface = portInterface;
	}

	public String getRoutingProtocol() {
		return this.routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public Double getMinBwInMbps() {
		return minBwInMbps;
	}

	public void setMinBwInMbps(Double minBwInMbps) {
		this.minBwInMbps = minBwInMbps;
	}

	public Double getMaxBwInMbps() {
		return maxBwInMbps;
	}

	public void setMaxBwInMbps(Double maxBwInMbps) {
		this.maxBwInMbps = maxBwInMbps;
	}

}