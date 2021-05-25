package com.tcl.dias.common.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MuxDetailsItem {

	@JsonProperty("mux")
	private String mux;

	@JsonProperty("mux_ip")
	private String muxIp;

	@JsonProperty("mux_port")
	private String muxPort;

	@JsonProperty("mux_access_rings")
	private List<AccessRingInfo> muxAccessRings;

	public void setMux(String mux) {
		this.mux = mux;
	}

	public String getMux() {
		return mux;
	}

	public void setMuxIp(String muxIp) {
		this.muxIp = muxIp;
	}

	public String getMuxIp() {
		return muxIp;
	}

	public void setMuxPort(String muxPort) {
		this.muxPort = muxPort;
	}

	public String getMuxPort() {
		return muxPort;
	}

	public void setMuxAccessRings(List<AccessRingInfo> muxAccessRings) {
		this.muxAccessRings = muxAccessRings;
	}

	public List<AccessRingInfo> getMuxAccessRings() {
		return muxAccessRings;
	}

	@Override
	public String toString() {
		return "MuxDetailsItem{" + "mux = '" + mux + '\'' + ",mux_ip = '" + muxIp + '\'' + ",mux_port = '" + muxPort
				+ '\'' + ",mux_access_rings = '" + muxAccessRings + '\'' + "}";
	}
}