package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class BhPo extends TaskDetailsBaseBean {
	
	private String  BHConnectivity;
	private String  bhLMBWFeasible;
	private String  endMuxNodeName;
	private String  endMuxNodeIp;
	private String  endMuxNodePort;
	
	public String getBHConnectivity() {
		return BHConnectivity;
	}
	public void setBHConnectivity(String bHConnectivity) {
		BHConnectivity = bHConnectivity;
	}
	public String getBhLMBWFeasible() {
		return bhLMBWFeasible;
	}
	public void setBhLMBWFeasible(String bhLMBWFeasible) {
		this.bhLMBWFeasible = bhLMBWFeasible;
	}
	public String getEndMuxNodeName() {
		return endMuxNodeName;
	}
	public void setEndMuxNodeName(String endMuxNodeName) {
		this.endMuxNodeName = endMuxNodeName;
	}
	public String getEndMuxNodeIp() {
		return endMuxNodeIp;
	}
	public void setEndMuxNodeIp(String endMuxNodeIp) {
		this.endMuxNodeIp = endMuxNodeIp;
	}
	public String getEndMuxNodePort() {
		return endMuxNodePort;
	}
	public void setEndMuxNodePort(String endMuxNodePort) {
		this.endMuxNodePort = endMuxNodePort;
	}
	
	

}
