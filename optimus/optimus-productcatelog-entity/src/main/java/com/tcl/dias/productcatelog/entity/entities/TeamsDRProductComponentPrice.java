package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_msdr_product_component_price database table.
 * 
 * @author Srinivasa Raghavan
 */
@Entity
@Table(name = "vw_mstmdr_product_component_price")
@NamedQuery(name = "TeamsDRProductComponentPrice.findAll", query = "SELECT v FROM TeamsDRProductComponentPrice v")
public class TeamsDRProductComponentPrice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "charge_nm")
	private String chargeNm;

	@Column(name = "max_usr_range")
	private Integer maxUsrRange;

	@Column(name = "min_usr_range")
	private Integer minUsrRange;

	private Double mrc;

	@Column(name = "mrc_charge_type")
	private String mrcChargeType;

	@Column(name = "mrc_curr")
	private String mrcCurr;

	private Double nrc;

	@Column(name = "nrc_charge_type")
	private String nrcChargeType;

	@Column(name = "nrc_curr")
	private String nrcCurr;

	@Column(name = "offering_cd")
	private String offeringCd;

	@Column(name = "offering_nm")
	private String offeringNm;

	public TeamsDRProductComponentPrice() {
	}

	public String getChargeNm() {
		return this.chargeNm;
	}

	public void setChargeNm(String chargeNm) {
		this.chargeNm = chargeNm;
	}

	public Integer getMaxUsrRange() {
		return this.maxUsrRange;
	}

	public void setMaxUsrRange(Integer maxUsrRange) {
		this.maxUsrRange = maxUsrRange;
	}

	public Integer getMinUsrRange() {
		return this.minUsrRange;
	}

	public void setMinUsrRange(Integer minUsrRange) {
		this.minUsrRange = minUsrRange;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public String getMrcChargeType() {
		return this.mrcChargeType;
	}

	public void setMrcChargeType(String mrcChargeType) {
		this.mrcChargeType = mrcChargeType;
	}

	public String getMrcCurr() {
		return this.mrcCurr;
	}

	public void setMrcCurr(String mrcCurr) {
		this.mrcCurr = mrcCurr;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getNrcChargeType() {
		return this.nrcChargeType;
	}

	public void setNrcChargeType(String nrcChargeType) {
		this.nrcChargeType = nrcChargeType;
	}

	public String getNrcCurr() {
		return this.nrcCurr;
	}

	public void setNrcCurr(String nrcCurr) {
		this.nrcCurr = nrcCurr;
	}

	public String getOfferingCd() {
		return this.offeringCd;
	}

	public void setOfferingCd(String offeringCd) {
		this.offeringCd = offeringCd;
	}

	public String getOfferingNm() {
		return this.offeringNm;
	}

	public void setOfferingNm(String offeringNm) {
		this.offeringNm = offeringNm;
	}

}