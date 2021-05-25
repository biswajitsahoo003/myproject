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
public class MACDCancellationRequest {
	
	private String serviceId;
	private String reason;
	private String cancellationDate;
	//private String cpeRecovery;
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCancellationDate() {
		return cancellationDate;
	}
	public void setCancellationDate(String cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
//	public String getCpeRecovery() {
//		return cpeRecovery;
//	}
//	public void setCpeRecovery(String cpeRecovery) {
//		this.cpeRecovery = cpeRecovery;
//	}
	
	

}
