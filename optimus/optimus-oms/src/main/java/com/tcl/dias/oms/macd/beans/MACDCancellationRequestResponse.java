package com.tcl.dias.oms.macd.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used as a response bean for request cancellation in MACD
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MACDCancellationRequestResponse {

	private String requestId;
	private String earlyTerminationCharges;
	private String cpeCharges;
	private String lmPriceRevision;
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the earlyTerminationCharges
	 */
	public String getEarlyTerminationCharges() {
		return earlyTerminationCharges;
	}
	/**
	 * @param earlyTerminationCharges the earlyTerminationCharges to set
	 */
	public void setEarlyTerminationCharges(String earlyTerminationCharges) {
		this.earlyTerminationCharges = earlyTerminationCharges;
	}
	/**
	 * @return the cpeCharges
	 */
	public String getCpeCharges() {
		return cpeCharges;
	}
	/**
	 * @param cpeCharges the cpeCharges to set
	 */
	public void setCpeCharges(String cpeCharges) {
		this.cpeCharges = cpeCharges;
	}
	/**
	 * @return the lmPriceRevision
	 */
	public String getLmPriceRevision() {
		return lmPriceRevision;
	}
	/**
	 * @param lmPriceRevision the lmPriceRevision to set
	 */
	public void setLmPriceRevision(String lmPriceRevision) {
		this.lmPriceRevision = lmPriceRevision;
	}
	
	
	
	
}
