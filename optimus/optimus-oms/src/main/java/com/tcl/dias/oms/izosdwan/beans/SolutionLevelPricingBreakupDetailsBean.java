package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;

public class SolutionLevelPricingBreakupDetailsBean implements Serializable{
	private String name;
	private String actionType;
	private String bandwidth;
	private Double arc;
	private Double nrc;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public Double getArc() {
		return arc;
	}
	public void setArc(Double arc) {
		this.arc = arc;
	}
	public Double getNrc() {
		return nrc;
	}
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}
	
}
