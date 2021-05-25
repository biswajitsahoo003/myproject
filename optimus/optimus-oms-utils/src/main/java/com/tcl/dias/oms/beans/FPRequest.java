package com.tcl.dias.oms.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This file contains the FPRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FPRequest {

	private FRequest feasiblility;
	private List<PRequest> pricings;
	private Double effectiveNrc;
	private Double effectiveMrc;
	private Double effectiveArc;
	private Double tcv;
	private Double ppuRate;

	public FRequest getFeasiblility() {
		return feasiblility;
	}

	public void setFeasiblility(FRequest feasiblility) {
		this.feasiblility = feasiblility;
	}

	public List<PRequest> getPricings() {
		return pricings;
	}

	public void setPricings(List<PRequest> pricings) {
		this.pricings = pricings;
	}

	public Double getEffectiveNrc() {
		return effectiveNrc;
	}

	public void setEffectiveNrc(Double effectiveNrc) {
		this.effectiveNrc = effectiveNrc;
	}

	public Double getEffectiveMrc() {
		return effectiveMrc;
	}

	public void setEffectiveMrc(Double effectiveMrc) {
		this.effectiveMrc = effectiveMrc;
	}

	public Double getEffectiveArc() {
		return effectiveArc;
	}

	public void setEffectiveArc(Double effectiveArc) {
		this.effectiveArc = effectiveArc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

	@Override
	public String toString() {
		return "FPRequest [feasiblility=" + feasiblility + ", pricings=" + pricings + "]";
	}

}
