package com.tcl.dias.servicefulfillmentutils.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Status;

/**
 * @author Savanya
 *
 */
@JsonInclude(Include.NON_NULL)
public class ProcurementResponse {

	private Integer procurementId;
	
	private Integer scOrderId;
	
	private Status status;
	
	public Integer getProcurementId() {
		return procurementId;
	}

	public void setProcurementId(Integer procurementId) {
		this.procurementId = procurementId;
	}

	public Integer getScOrderId() {
		return scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ProcurementResponse [procurementId=" + procurementId + ", scOrderId=" + scOrderId + "]";
	}
	
	
}
