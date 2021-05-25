package com.tcl.dias.common.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Cpe charges bean
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CpeChargesBean {

	@JsonProperty("mrc")
	private Double mrc;

	@JsonProperty("nrc")
	private Double nrc;

	@JsonProperty("arc")
	private Double arc;

	@JsonProperty("tcv")
	Double tcv;

	@JsonProperty("unit_mrc")
	private Double unitMrc;

	@JsonProperty("unit_nrc")
	private Double unitNrc;

	@JsonProperty("list_of_cpe_sub_charges")
	private List<CpeSubchargesBean> cpeSubcharges;

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Double getUnitMrc() {
		return unitMrc;
	}

	public void setUnitMrc(Double unitMrc) {
		this.unitMrc = unitMrc;
	}

	public Double getUnitNrc() {
		return unitNrc;
	}

	public void setUnitNrc(Double unitNrc) {
		this.unitNrc = unitNrc;
	}

	public List<CpeSubchargesBean> getCpeSubcharges() {
		return cpeSubcharges;
	}

	public void setCpeSubcharges(List<CpeSubchargesBean> cpeSubcharges) {
		this.cpeSubcharges = cpeSubcharges;
	}
}
