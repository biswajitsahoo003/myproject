package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the vw_mstmdr_ratecard_hsn_code database table.
 * 
 * @author Syed Ali.
 * @createdAt 23/12/2020, Wednesday, 11:37
 */
@Entity
@Table(name = "vw_mstmdr_ratecard_hsn_code")
@NamedQuery(name = "TeamsDRRateCardHSNCode.findAll", query = "SELECT v FROM TeamsDRRateCardHSNCode v")
public class TeamsDRRateCardHSNCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "component_varient")
	private String componentVarient;

	@Id
	@Column(name = "component_sub_varient")
	private String componentSubVarient;

	@Column(name = "charge_nm")
	private String chargeNm;

	@Column(name = "hsn_code")
	private String hsnCode;

	public TeamsDRRateCardHSNCode() {
	}

	public String getComponentVarient() {
		return componentVarient;
	}

	public void setComponentVarient(String componentVarient) {
		this.componentVarient = componentVarient;
	}

	public String getComponentSubVarient() {
		return componentSubVarient;
	}

	public void setComponentSubVarient(String componentSubVarient) {
		this.componentSubVarient = componentSubVarient;
	}

	public String getChargeNm() {
		return chargeNm;
	}

	public void setChargeNm(String chargeNm) {
		this.chargeNm = chargeNm;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
}
