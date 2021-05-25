package com.tcl.dias.common.serviceinventory.bean;


/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MuxDetailsBean {

	private String endMuxNodeIp;

	private String endMuxNodePort;

	private String muxMake;

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

	public String getMuxMake() {
		return muxMake;
	}

	public void setMuxMake(String muxMake) {
		this.muxMake = muxMake;
	}

}
