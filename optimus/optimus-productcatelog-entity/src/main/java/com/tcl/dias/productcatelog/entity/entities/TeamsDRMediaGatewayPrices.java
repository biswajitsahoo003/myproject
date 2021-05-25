package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_mstmdr_pri database table.
 *
 * @author Srinivasa Raghavan
 */
@Entity
@Table(name = "vw_mstmdr_pri")
@NamedQuery(name = "TeamsDRMediaGatewayPrices.findAll", query = "SELECT v FROM TeamsDRMediaGatewayPrices v")
public class TeamsDRMediaGatewayPrices implements Serializable {
	private static final long serialVersionUID = 1L;

	private Double arc;

	@Column(name = "arc_curr")
	private String arcCurr;

	@Column(name = "max_usr")
	private Integer maxUsr;

	@Column(name = "min_usr")
	private Integer minUsr;

	@Id
	@Column(name = "model_cd")
	private String modelCd;

	@Column(name = "model_nm")
	private String modelNm;

	private Double nrc;

	@Column(name = "nrc_curr")
	private String nrcCurr;

	@Column(name = "`pri/tdm`")
	private String pri_tdm;

	@Column(name = "product_component_price_cd")
	private String productComponentPriceCd;

	private String vendor;

	public TeamsDRMediaGatewayPrices() {
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getArcCurr() {
		return this.arcCurr;
	}

	public void setArcCurr(String arcCurr) {
		this.arcCurr = arcCurr;
	}

	public Integer getMaxUsr() {
		return this.maxUsr;
	}

	public void setMaxUsr(Integer maxUsr) {
		this.maxUsr = maxUsr;
	}

	public Integer getMinUsr() {
		return this.minUsr;
	}

	public void setMinUsr(Integer minUsr) {
		this.minUsr = minUsr;
	}

	public String getModelCd() {
		return this.modelCd;
	}

	public void setModelCd(String modelCd) {
		this.modelCd = modelCd;
	}

	public String getModelNm() {
		return this.modelNm;
	}

	public void setModelNm(String modelNm) {
		this.modelNm = modelNm;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getNrcCurr() {
		return this.nrcCurr;
	}

	public void setNrcCurr(String nrcCurr) {
		this.nrcCurr = nrcCurr;
	}

	public String getPri_tdm() {
		return this.pri_tdm;
	}

	public void setPri_tdm(String pri_tdm) {
		this.pri_tdm = pri_tdm;
	}

	public String getProductComponentPriceCd() {
		return this.productComponentPriceCd;
	}

	public void setProductComponentPriceCd(String productComponentPriceCd) {
		this.productComponentPriceCd = productComponentPriceCd;
	}

	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

}
