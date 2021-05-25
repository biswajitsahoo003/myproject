package com.tcl.dias.oms.teamsdr.beans;

/**
 * TeamsDR Unit price bean for manual price update
 *
 * @author Srinivasa Raghavan
 */
public class TeamsDRUnitPriceBean {

	private Double unitMrc;
	private Double unitNrc;
	private Integer quantity;
	private Double finalMrc;
	private Double finalNrc;
	private Double subTotalUnitMrc;
	private Double subTotalUnitNrc;
	private Double subTotalFinalMrc;
	private Double subTotalFinalNrc;
	private Double quoteTeamsMrc;
	private Double quoteTeamsNrc;

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

	public Double getFinalMrc() {
		return finalMrc;
	}

	public void setFinalMrc(Double finalMrc) {
		this.finalMrc = finalMrc;
	}

	public Double getFinalNrc() {
		return finalNrc;
	}

	public void setFinalNrc(Double finalNrc) {
		this.finalNrc = finalNrc;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getSubTotalUnitMrc() {
		return subTotalUnitMrc;
	}

	public void setSubTotalUnitMrc(Double subTotalUnitMrc) {
		this.subTotalUnitMrc = subTotalUnitMrc;
	}

	public Double getSubTotalUnitNrc() {
		return subTotalUnitNrc;
	}

	public void setSubTotalUnitNrc(Double subTotalUnitNrc) {
		this.subTotalUnitNrc = subTotalUnitNrc;
	}

	public Double getSubTotalFinalMrc() {
		return subTotalFinalMrc;
	}

	public void setSubTotalFinalMrc(Double subTotalFinalMrc) {
		this.subTotalFinalMrc = subTotalFinalMrc;
	}

	public Double getSubTotalFinalNrc() {
		return subTotalFinalNrc;
	}

	public void setSubTotalFinalNrc(Double subTotalFinalNrc) {
		this.subTotalFinalNrc = subTotalFinalNrc;
	}

	public Double getQuoteTeamsMrc() {
		return quoteTeamsMrc;
	}

	public void setQuoteTeamsMrc(Double quoteTeamsMrc) {
		this.quoteTeamsMrc = quoteTeamsMrc;
	}

	public Double getQuoteTeamsNrc() {
		return quoteTeamsNrc;
	}

	public void setQuoteTeamsNrc(Double quoteTeamsNrc) {
		this.quoteTeamsNrc = quoteTeamsNrc;
	}
}
