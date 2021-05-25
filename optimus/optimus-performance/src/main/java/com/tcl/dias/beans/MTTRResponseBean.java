package com.tcl.dias.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file contains the MTTRResponseBean.java class.
 * 
 *
 * @author Deepika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MTTRResponseBean {

	private Map<String, List<Double>> customerEndMTTR = new HashMap<>();
	
	private Map<String, List<Double>> otherEndMTTR = new HashMap<>();

	private Map<String, List<MTTRReportBean>> mttrSeverityMap = new HashMap<>();


	/**
	 * @return the customerEndMTTR
	 */
	public Map<String, List<Double>> getCustomerEndMTTR() {
		return customerEndMTTR;
	}


	/**
	 * @param customerEndMTTR the customerEndMTTR to set
	 */
	public void setCustomerEndMTTR(Map<String, List<Double>> customerEndMTTR) {
		this.customerEndMTTR = customerEndMTTR;
	}


	/**
	 * @return the otherEndMTTR
	 */
	public Map<String, List<Double>> getOtherEndMTTR() {
		return otherEndMTTR;
	}


	/**
	 * @param otherEndMTTR the otherEndMTTR to set
	 */
	public void setOtherEndMTTR(Map<String, List<Double>> otherEndMTTR) {
		this.otherEndMTTR = otherEndMTTR;
	}


	/**
	 * @return the mttrSeverityMap
	 */
	public Map<String, List<MTTRReportBean>> getMttrSeverityMap() {
		return mttrSeverityMap;
	}


	/**
	 * @param mttrSeverityMap the mttrSeverityMap to set
	 */
	public void setMttrSeverityMap(Map<String, List<MTTRReportBean>> mttrSeverityMap) {
		this.mttrSeverityMap = mttrSeverityMap;
	}
	
	
}
