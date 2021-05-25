package com.tcl.dias.oms.beans;

/**
 * This file contains the CofRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CofRequest {

	private String refUuid;
	private String cofPayload;

	public String getRefUuid() {
		return refUuid;
	}

	public void setRefUuid(String refUuid) {
		this.refUuid = refUuid;
	}

	public String getCofPayload() {
		return cofPayload;
	}

	public void setCofPayload(String cofPayload) {
		this.cofPayload = cofPayload;
	}

	@Override
	public String toString() {
		return "CofRequest [refUuid=" + refUuid + ", cofPayload=" + cofPayload + "]";
	}

}
