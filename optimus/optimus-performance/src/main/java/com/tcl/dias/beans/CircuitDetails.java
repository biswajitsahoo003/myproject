package com.tcl.dias.beans;

/**
 * This file contains the property for cause of impact and the corresponding
 * ticket count.
 * 
 * @author KRUHTIKA S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CircuitDetails {

	private String faultMetricsTotalCircuit;

	private String faultMetricsTotalCircuitCount;

	private String faultMetricsImpactedCircuit;

	private String faultMetricsImpactedCircuitCount;

	private String faultMetricsFaultRate;

	private String faultMetricsFaultRateCount;

	public String getFaultMetricsTotalCircuit() {
		return faultMetricsTotalCircuit;
	}

	public void setFaultMetricsTotalCircuit(String faultMetricsTotalCircuit) {
		this.faultMetricsTotalCircuit = faultMetricsTotalCircuit;
	}

	public String getFaultMetricsTotalCircuitCount() {
		return faultMetricsTotalCircuitCount;
	}

	public void setFaultMetricsTotalCircuitCount(String faultMetricsTotalCircuitCount) {
		this.faultMetricsTotalCircuitCount = faultMetricsTotalCircuitCount;
	}

	public String getFaultMetricsImpactedCircuit() {
		return faultMetricsImpactedCircuit;
	}

	public void setFaultMetricsImpactedCircuit(String faultMetricsImpactedCircuit) {
		this.faultMetricsImpactedCircuit = faultMetricsImpactedCircuit;
	}

	public String getFaultMetricsImpactedCircuitCount() {
		return faultMetricsImpactedCircuitCount;
	}

	public void setFaultMetricsImpactedCircuitCount(String faultMetricsImpactedCircuitCount) {
		this.faultMetricsImpactedCircuitCount = faultMetricsImpactedCircuitCount;
	}

	public String getFaultMetricsFaultRate() {
		return faultMetricsFaultRate;
	}

	public void setFaultMetricsFaultRate(String faultMetricsFaultRate) {
		this.faultMetricsFaultRate = faultMetricsFaultRate;
	}

	public String getFaultMetricsFaultRateCount() {
		return faultMetricsFaultRateCount;
	}

	public void setFaultMetricsFaultRateCount(String faultMetricsFaultRateCount) {
		this.faultMetricsFaultRateCount = faultMetricsFaultRateCount;
	}
}
