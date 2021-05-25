package com.tcl.dias.beans;

import java.math.BigDecimal;

/**
 * This file contains the circuit details  uptime, total circuits, impacted circuits, fault rate - product wise.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CircuitInfoBean {

	private String uptimePercentage;

	private Integer totalCircuitCount;
	
	private BigDecimal faultRatePercentage;

	private Integer faultRateTotalCount;

	private Integer faultRateImpactedCount;

	private Integer slaBreachCount;

	private BigDecimal slaBreachPercentage;

	/**
	 * @return the totalCircuitCount
	 */
	public Integer getTotalCircuitCount() {
		return totalCircuitCount;
	}

	/**
	 * @param totalCircuitCount the totalCircuitCount to set
	 */
	public void setTotalCircuitCount(Integer totalCircuitCount) {
		this.totalCircuitCount = totalCircuitCount;
	}

	/**
	 * @return the uptimePercentage
	 */
	public String getUptimePercentage() {
		return uptimePercentage;
	}

	/**
	 * @param uptimePercentage the uptimePercentage to set
	 */
	public void setUptimePercentage(String uptimePercentage) {
		this.uptimePercentage = uptimePercentage;
	}

	/**
	 * @return the slaBreachCount
	 */
	public Integer getSlaBreachCount() {
		return slaBreachCount;
	}

	/**
	 * @param slaBreachCount the slaBreachCount to set
	 */
	public void setSlaBreachCount(Integer slaBreachCount) {
		this.slaBreachCount = slaBreachCount;
	}

	/**
	 * @return the slaBreachPercentage
	 */
	public BigDecimal getSlaBreachPercentage() {
		return slaBreachPercentage;
	}

	/**
	 * @param slaBreachPercentage the slaBreachPercentage to set
	 */
	public void setSlaBreachPercentage(BigDecimal slaBreachPercentage) {
		this.slaBreachPercentage = slaBreachPercentage;
	}

	/**
	 * @return the faultRatePercentage
	 */
	public BigDecimal getFaultRatePercentage() {
		return faultRatePercentage;
	}

	/**
	 * @param faultRatePercentage the faultRatePercentage to set
	 */
	public void setFaultRatePercentage(BigDecimal faultRatePercentage) {
		this.faultRatePercentage = faultRatePercentage;
	}

	/**
	 * @return the faultRateTotalCount
	 */
	public Integer getFaultRateTotalCount() {
		return faultRateTotalCount;
	}

	/**
	 * @param faultRateTotalCount the faultRateTotalCount to set
	 */
	public void setFaultRateTotalCount(Integer faultRateTotalCount) {
		this.faultRateTotalCount = faultRateTotalCount;
	}

	/**
	 * @return the faultRateImpactedCount
	 */
	public Integer getFaultRateImpactedCount() {
		return faultRateImpactedCount;
	}

	/**
	 * @param faultRateImpactedCount the faultRateImpactedCount to set
	 */
	public void setFaultRateImpactedCount(Integer faultRateImpactedCount) {
		this.faultRateImpactedCount = faultRateImpactedCount;
	}
	
}
