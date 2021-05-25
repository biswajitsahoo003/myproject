package com.tcl.dias.common.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Cpe surcharges bean
 * 
 * @author srraghav
 */
public class CpeSubchargesBean {

	@JsonProperty("component_type")
	private String componentType;

	@JsonProperty("component_name")
	private String componentName;

	@JsonProperty("cost_mrc")
	private Double costMrc;

	@JsonProperty("cost_nrc")
	private Double costNrc;

	@JsonProperty("cost_arc")
	private Double costArc;

	@JsonProperty("cost_tcv")
	private Double costTcv;

	@JsonProperty("discount_to_tata")
	private Double discountToTata;

	@JsonProperty("after_discount_mrc")
	private Double afterDiscountMrc;

	@JsonProperty("after_discount_nrc")
	private Double afterDiscountNrc;

	@JsonProperty("after_discount_arc")
	private Double afterDiscountArc;

	@JsonProperty("after_discount_tcv")
	private Double afterDiscountTcv;

	@JsonProperty("unit_mrc_sp")
	private Double unitMrcSp;

	@JsonProperty("unit_nrc_sp")
	private Double unitNrcSp;

	@JsonProperty("unit_arc_sp")
	private Double unitArcSp;

	@JsonProperty("unit_tcv_sp")
	private Double unitTcvSp;

	@JsonProperty("mrc")
	private Double mrc;

	@JsonProperty("nrc")
	private Double nrc;

	@JsonProperty("arc")
	private Double arc;

	@JsonProperty("tcv")
	private Double tcv;

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public Double getCostMrc() {
		return costMrc;
	}

	public void setCostMrc(Double costMrc) {
		this.costMrc = costMrc;
	}

	public Double getCostNrc() {
		return costNrc;
	}

	public void setCostNrc(Double costNrc) {
		this.costNrc = costNrc;
	}

	public Double getCostArc() {
		return costArc;
	}

	public void setCostArc(Double costArc) {
		this.costArc = costArc;
	}

	public Double getCostTcv() {
		return costTcv;
	}

	public void setCostTcv(Double costTcv) {
		this.costTcv = costTcv;
	}

	public Double getDiscountToTata() {
		return discountToTata;
	}

	public void setDiscountToTata(Double discountToTata) {
		this.discountToTata = discountToTata;
	}

	public Double getAfterDiscountMrc() {
		return afterDiscountMrc;
	}

	public void setAfterDiscountMrc(Double afterDiscountMrc) {
		this.afterDiscountMrc = afterDiscountMrc;
	}

	public Double getAfterDiscountNrc() {
		return afterDiscountNrc;
	}

	public void setAfterDiscountNrc(Double afterDiscountNrc) {
		this.afterDiscountNrc = afterDiscountNrc;
	}

	public Double getAfterDiscountArc() {
		return afterDiscountArc;
	}

	public void setAfterDiscountArc(Double afterDiscountArc) {
		this.afterDiscountArc = afterDiscountArc;
	}

	public Double getAfterDiscountTcv() {
		return afterDiscountTcv;
	}

	public void setAfterDiscountTcv(Double afterDiscountTcv) {
		this.afterDiscountTcv = afterDiscountTcv;
	}

	public Double getUnitMrcSp() {
		return unitMrcSp;
	}

	public void setUnitMrcSp(Double unitMrcSp) {
		this.unitMrcSp = unitMrcSp;
	}

	public Double getUnitNrcSp() {
		return unitNrcSp;
	}

	public void setUnitNrcSp(Double unitNrcSp) {
		this.unitNrcSp = unitNrcSp;
	}

	public Double getUnitArcSp() {
		return unitArcSp;
	}

	public void setUnitArcSp(Double unitArcSp) {
		this.unitArcSp = unitArcSp;
	}

	public Double getUnitTcvSp() {
		return unitTcvSp;
	}

	public void setUnitTcvSp(Double unitTcvSp) {
		this.unitTcvSp = unitTcvSp;
	}

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
}
