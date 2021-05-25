package com.tcl.dias.common.beans;

public class SlaveVRFBean {
	private String vrfName;
	private String VrfBandwidth;
	private String flexiqos;
    private String serviceId;
	
	
	
	
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getVrfName() {
		return vrfName;
	}
	public void setVrfName(String vrfName) {
		this.vrfName = vrfName;
	}
	public String getVrfBandwidth() {
		return VrfBandwidth;
	}
	public void setVrfBandwidth(String vrfBandwidth) {
		VrfBandwidth = vrfBandwidth;
	}
	public String getFlexiqos() {
		return flexiqos;
	}
	public void setFlexiqos(String flexiqos) {
		this.flexiqos = flexiqos;
	}

}
