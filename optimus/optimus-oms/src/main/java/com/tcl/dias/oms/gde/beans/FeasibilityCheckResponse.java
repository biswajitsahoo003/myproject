package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean class used to notify BOD feasibility response from MDSO
 * @author archchan
 *
 */
public class FeasibilityCheckResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("mdso_id")
	private String mdsoId;
	
	@JsonProperty("schedule_id")
	private String scheduleId;
	
	@JsonProperty("req_bandwidth")
	private String reqBandwidth;
	
	@JsonProperty("req_start_date")
	private Timestamp reqStartDate;
	
	@JsonProperty("req_end_date")
	private Timestamp reqEndDate;
	
	@JsonProperty("actual_start_time")
	private Timestamp actualStartTime;
	
	@JsonProperty("actual_end_time")
	private Timestamp actualEndTime;
	
	@JsonProperty("operation_id")
	private String operationId;
	
	private String status;
	
	private String reason;

	public String getMdsoId() {
		return mdsoId;
	}

	public void setMdsoId(String mdsoId) {
		this.mdsoId = mdsoId;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getReqBandwidth() {
		return reqBandwidth;
	}

	public void setReqBandwidth(String reqBandwidth) {
		this.reqBandwidth = reqBandwidth;
	}

	public Timestamp getReqStartDate() {
		return reqStartDate;
	}

	public void setReqStartDate(Timestamp reqStartDate) {
		this.reqStartDate = reqStartDate;
	}

	public Timestamp getReqEndDate() {
		return reqEndDate;
	}

	public void setReqEndDate(Timestamp reqEndDate) {
		this.reqEndDate = reqEndDate;
	}

	public Timestamp getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(Timestamp actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Timestamp getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(Timestamp actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "FeasibilityCheckResponse [mdsoId=" + mdsoId + ", scheduleId=" + scheduleId + ", reqBandwidth="
				+ reqBandwidth + ", reqStartDate=" + reqStartDate + ", reqEndDate=" + reqEndDate + ", actualStartTime="
				+ actualStartTime + ", actualEndTime=" + actualEndTime + ", operationId=" + operationId + ", status="
				+ status + ", reason=" + reason + "]";
	}
	

}
