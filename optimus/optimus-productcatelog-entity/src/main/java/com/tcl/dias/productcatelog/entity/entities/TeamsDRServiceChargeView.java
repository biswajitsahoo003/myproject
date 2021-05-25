package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_mstmdr_ratecard database table.
 * 
 * @author Srinivasa Raghavan
 */
@Entity
@Table(name = "vw_mstmdr_ratecard")
@NamedQuery(name = "TeamsDRServiceChargeView.findAll", query = "SELECT v FROM TeamsDRServiceChargeView v")
public class TeamsDRServiceChargeView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "charge_cost")
	private Double chargeCost;

	@Column(name = "charge_cost_uom")
	private String chargeCostUom;

	@Column(name = "charge_currency")
	private String chargeCurrency;

	@Column(name = "charge_nm")
	private String chargeNm;

	@Column(name = "charge_type")
	private String chargeType;

	@Id
	@Column(name = "component_sub_varient")
	private String componentSubVarient;

	@Column(name = "component_varient")
	private String componentVarient;

	@Column(name = "geneva_charge_nm")
	private String genevaChargeNm;

	public TeamsDRServiceChargeView() {
	}

	public Double getChargeCost() {
		return this.chargeCost;
	}

	public void setChargeCost(Double chargeCost) {
		this.chargeCost = chargeCost;
	}

	public String getChargeCostUom() {
		return this.chargeCostUom;
	}

	public void setChargeCostUom(String chargeCostUom) {
		this.chargeCostUom = chargeCostUom;
	}

	public String getChargeCurrency() {
		return this.chargeCurrency;
	}

	public void setChargeCurrency(String chargeCurrency) {
		this.chargeCurrency = chargeCurrency;
	}

	public String getChargeNm() {
		return this.chargeNm;
	}

	public void setChargeNm(String chargeNm) {
		this.chargeNm = chargeNm;
	}

	public String getChargeType() {
		return this.chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getComponentSubVarient() {
		return this.componentSubVarient;
	}

	public void setComponentSubVarient(String componentSubVarient) {
		this.componentSubVarient = componentSubVarient;
	}

	public String getComponentVarient() {
		return this.componentVarient;
	}

	public void setComponentVarient(String componentVarient) {
		this.componentVarient = componentVarient;
	}

	public String getGenevaChargeNm() {
		return this.genevaChargeNm;
	}

	public void setGenevaChargeNm(String genevaChargeNm) {
		this.genevaChargeNm = genevaChargeNm;
	}

}