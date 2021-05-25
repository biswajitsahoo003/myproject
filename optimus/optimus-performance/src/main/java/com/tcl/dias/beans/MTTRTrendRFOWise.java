package com.tcl.dias.beans;

import java.util.Date;

/**
 * This file contains the month wise - customer & tata end MTTR data.
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MTTRTrendRFOWise {

	private String cuid;
	private String monthYear;

	private String rFOResponsible;
	private String impact;
	private String avgMttr;
	private Date monthYrInDate;

	public Date getMonthYrInDate() {
		return monthYrInDate;
	}

	public void setMonthYrInDate(Date monthYrInDate) {
		this.monthYrInDate = monthYrInDate;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public String getrFOResponsible() {
		return rFOResponsible;
	}

	public void setrFOResponsible(String rFOResponsible) {
		this.rFOResponsible = rFOResponsible;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getAvgMttr() {
		return avgMttr;
	}

	public void setAvgMttr(String avgMttr) {
		this.avgMttr = avgMttr;
	}

}
