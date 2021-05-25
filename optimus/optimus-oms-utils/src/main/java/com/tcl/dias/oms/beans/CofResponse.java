package com.tcl.dias.oms.beans;

/**
 * This file contains the CofRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CofResponse {

	private Integer cofDetailsId;
	private String refUuid;
	private String cofPayload;
	private String createdBy;

	public Integer getCofDetailsId() {
		return cofDetailsId;
	}

	public void setCofDetailsId(Integer cofDetailsId) {
		this.cofDetailsId = cofDetailsId;
	}

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "CofResponse [cofDetailsId=" + cofDetailsId + ", refUuid=" + refUuid + ", cofPayload=" + cofPayload
				+ ", createdBy=" + createdBy + "]";
	}

}
