package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the CofTotalAmount.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CofTotalAmount {

	private Double totalTcv;
	private Double finalArc;
	private Double finalMrc;
	private Double finalNrc;

	public Double getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(Double totalTcv) {
		this.totalTcv = totalTcv;
	}

	public Double getFinalArc() {
		return finalArc;
	}

	public void setFinalArc(Double finalArc) {
		this.finalArc = finalArc;
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

	@Override
	public String toString() {
		return "CofTotalAmount [totalTcv=" + totalTcv + ", finalArc=" + finalArc + ", finalMrc=" + finalMrc
				+ ", finalNrc=" + finalNrc + "]";
	}

}
