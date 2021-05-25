package com.tcl.dias.oms.npl.pdf.beans;

public class NplSpecificAttribute {
	
	private String interfaceType;
	private String localLoopBandwidth;
	private String lastMile;
	private Integer isNotDc=1;
	
	
	
	
	public String getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	public String getLocalLoopBandwidth() {
		return localLoopBandwidth;
	}
	public void setLocalLoopBandwidth(String localLoopBandwidth) {
		this.localLoopBandwidth = localLoopBandwidth;
	}
	public String getLastMile() {
		return lastMile;
	}
	public void setLastMile(String lastMile) {
		this.lastMile = lastMile;
	}

	public Integer getIsNotDc() {
		return isNotDc;
	}
	public void setIsNotDc(Integer isNotDc) {
		this.isNotDc = isNotDc;
	}
	@Override
	public String toString() {
		return "NplSpecificAttribute [interfaceType=" + interfaceType + ", localLoopBandwidth=" + localLoopBandwidth
				+ ", lastMile=" + lastMile + ", isNotDc=" + isNotDc + "]";
	}
	

}
