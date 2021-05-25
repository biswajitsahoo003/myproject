package com.tcl.dias.oms.beans;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Status;

/**
 * Bean class
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class CnResponse {

	private String message=CommonConstants.EMPTY;
	private Status status = Status.SUCCESS;
	
	public CnResponse() {
		//DO NOTHING
	}
	public CnResponse(String message) {
		this.message=message;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}