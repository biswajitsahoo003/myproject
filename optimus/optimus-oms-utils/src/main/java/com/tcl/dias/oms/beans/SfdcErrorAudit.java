package com.tcl.dias.oms.beans;

import java.util.Date;

public class SfdcErrorAudit {
	private String refId;
	private String stage;
	private Integer tpId;
	private String errorResponse;
	private String updatedBy;
	private Date updatedTime;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Integer getTpId() {
		return tpId;
	}

	public void setTpId(Integer tpId) {
		this.tpId = tpId;
	}

	public String getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "SfdcErrorAudit [refId=" + refId + ", stage=" + stage + ", tpId=" + tpId + ", errorResponse="
				+ errorResponse + ", updatedBy=" + updatedBy + ", updatedTime=" + updatedTime + "]";
	}

}
