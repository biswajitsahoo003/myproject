package com.tcl.dias.beans;

/**
 * A method to hold fault date details
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class FaultRateBean {

	private String CustomerServiceFaultMetricsServiceType;
	private String CustomerServiceFaultMetricsMonthYear;
	private String CustomerServiceFaultMetricsTotalCircuitCount;
	private String CustomerServiceFaultMetricsImpactedCircuitCount;
	private String CustomerServiceFaultMetricsFaultRate;
	private String CustomerServiceFaultMetricsCUID;

	public String getCustomerServiceFaultMetricsServiceType() {
		return CustomerServiceFaultMetricsServiceType;
	}

	public void setCustomerServiceFaultMetricsServiceType(String customerServiceFaultMetricsServiceType) {
		CustomerServiceFaultMetricsServiceType = customerServiceFaultMetricsServiceType;
	}

	public String getCustomerServiceFaultMetricsMonthYear() {
		return CustomerServiceFaultMetricsMonthYear;
	}

	public void setCustomerServiceFaultMetricsMonthYear(String customerServiceFaultMetricsMonthYear) {
		CustomerServiceFaultMetricsMonthYear = customerServiceFaultMetricsMonthYear;
	}

	public String getCustomerServiceFaultMetricsTotalCircuitCount() {
		return CustomerServiceFaultMetricsTotalCircuitCount;
	}

	public void setCustomerServiceFaultMetricsTotalCircuitCount(String customerServiceFaultMetricsTotalCircuitCount) {
		CustomerServiceFaultMetricsTotalCircuitCount = customerServiceFaultMetricsTotalCircuitCount;
	}

	public String getCustomerServiceFaultMetricsImpactedCircuitCount() {
		return CustomerServiceFaultMetricsImpactedCircuitCount;
	}

	public void setCustomerServiceFaultMetricsImpactedCircuitCount(
			String customerServiceFaultMetricsImpactedCircuitCount) {
		CustomerServiceFaultMetricsImpactedCircuitCount = customerServiceFaultMetricsImpactedCircuitCount;
	}

	public String getCustomerServiceFaultMetricsCUID() {
		return CustomerServiceFaultMetricsCUID;
	}

	public void setCustomerServiceFaultMetricsCUID(String customerServiceFaultMetricsCUID) {
		CustomerServiceFaultMetricsCUID = customerServiceFaultMetricsCUID;
	}

	public String getCustomerServiceFaultMetricsFaultRate() {
		return CustomerServiceFaultMetricsFaultRate;
	}

	public void setCustomerServiceFaultMetricsFaultRate(String customerServiceFaultMetricsFaultRate) {
		CustomerServiceFaultMetricsFaultRate = customerServiceFaultMetricsFaultRate;
	}

}
