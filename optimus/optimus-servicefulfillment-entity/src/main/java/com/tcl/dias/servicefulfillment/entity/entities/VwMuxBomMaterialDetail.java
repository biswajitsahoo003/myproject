package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Entity
@Table(name = "vw_mux_bom_material_detail")
@Immutable
@IdClass(BomViewId.class)
public class VwMuxBomMaterialDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "bom_code")
	private String bomCode;

	@Column(name = "bom_name")
	private String bomName;

	@Column(name = "bom_type")
	private String bomType;

	@Column(name = "cable_type")
	private String cableType;

	@Column(name = "mux_interface")
	private String muxInterface;

	@Column(name = "line_rate")
	private String lineRate;

	private String make;

	@Id
	@Column(name = "material_code")
	private String materialCode;

	@Column(name = "material_description")
	private String materialDescription;

	@Column(name = "material_quantity")
	private String materialQuantity;

	@Column(name = "max_ll_bw_in_mbps")
	private int maxLlBwInMbps;

	@Column(name = "min_ll_bw_in_mbps")
	private int minLlBwInMbps;

	private String model;

	@Column(name = "topology_type")
	private String topologyType;


	public VwMuxBomMaterialDetail() {
	}

	public String getBomCode() {
		return this.bomCode;
	}

	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}

	public String getBomName() {
		return this.bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getBomType() {
		return this.bomType;
	}

	public void setBomType(String bomType) {
		this.bomType = bomType;
	}

	public String getCableType() {
		return this.cableType;
	}

	public void setCableType(String cableType) {
		this.cableType = cableType;
	}

	public String getLineRate() {
		return this.lineRate;
	}

	public void setLineRate(String lineRate) {
		this.lineRate = lineRate;
	}

	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getMaterialCode() {
		return this.materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialDescription() {
		return this.materialDescription;
	}

	public void setMaterialDescription(String materialDescription) {
		this.materialDescription = materialDescription;
	}

	public String getMaterialQuantity() {
		return this.materialQuantity;
	}

	public void setMaterialQuantity(String materialQuantity) {
		this.materialQuantity = materialQuantity;
	}

	public int getMaxLlBwInMbps() {
		return this.maxLlBwInMbps;
	}

	public void setMaxLlBwInMbps(int maxLlBwInMbps) {
		this.maxLlBwInMbps = maxLlBwInMbps;
	}

	public int getMinLlBwInMbps() {
		return this.minLlBwInMbps;
	}

	public void setMinLlBwInMbps(int minLlBwInMbps) {
		this.minLlBwInMbps = minLlBwInMbps;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMuxInterface() {
		return muxInterface;
	}

	public void setMuxInterface(String muxInterface) {
		this.muxInterface = muxInterface;
	}

	public String getTopologyType() {
		return topologyType;
	}

	public void setTopologyType(String topologyType) {
		this.topologyType = topologyType;
	}
}
