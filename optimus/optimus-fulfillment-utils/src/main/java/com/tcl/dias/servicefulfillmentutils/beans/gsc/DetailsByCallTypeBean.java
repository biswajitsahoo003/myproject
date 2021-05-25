package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailsByCallTypeBean {
	
	private String callType;
	private String blocked;
	private String custOutpulsedDigits;
	private String accessType;
	private Integer directConnectionId;
	private Integer pstnRoutingCMS;
	
    private List<SwitchingDetailsBean> switchingDetails;
	
	private List<RoutingDetailsBean> routingDetails;
	
	private String status;
	
	private String statusMsg;
	
	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getBlocked() {
		return blocked;
	}

	public void setBlocked(String blocked) {
		this.blocked = blocked;
	}

	public String getCustOutpulsedDigits() {
		return custOutpulsedDigits;
	}

	public void setCustOutpulsedDigits(String custOutpulsedDigits) {
		this.custOutpulsedDigits = custOutpulsedDigits;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public Integer getDirectConnectionId() {
		return directConnectionId;
	}

	public void setDirectConnectionId(Integer directConnectionId) {
		this.directConnectionId = directConnectionId;
	}

	public Integer getPstnRoutingCMS() {
		return pstnRoutingCMS;
	}

	public void setPstnRoutingCMS(Integer pstnRoutingCMS) {
		this.pstnRoutingCMS = pstnRoutingCMS;
	}

	public List<SwitchingDetailsBean> getSwitchingDetails() {
		return switchingDetails;
	}

	public void setSwitchingDetails(List<SwitchingDetailsBean> switchingDetails) {
		this.switchingDetails = switchingDetails;
	}

	public List<RoutingDetailsBean> getRoutingDetails() {
		return routingDetails;
	}

	public void setRoutingDetails(List<RoutingDetailsBean> routingDetails) {
		this.routingDetails = routingDetails;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
}
