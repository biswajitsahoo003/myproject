package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_bom_material_detail")
@Immutable
@IdClass(BomViewId.class)
public class VwBomMaterialDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "bom_code")
	private String bomCode;

	@Column(name = "bom_name")
	private String bomName;

	@Column(name = "bom_type")
	private String bomType;

	@Column(name = "ll_bw_in_mbps")
	private String llBwInMbps;
	
	@Column(name = "make")
	private String make;

	@Id
	@Column(name = "material_code")
	private String materialCode;

	@Column(name = "material_description")
	private String materialDescription;

	@Column(name = "material_quantity")
	private String materialQuantity;

	@Column(name = "scenario_type")
	private String scenarioType;

	public VwBomMaterialDetail() {
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

	public String getLlBwInMbps() {
		return this.llBwInMbps;
	}

	public void setLlBwInMbps(String llBwInMbps) {
		this.llBwInMbps = llBwInMbps;
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

	public String getScenarioType() {
		return this.scenarioType;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	
}