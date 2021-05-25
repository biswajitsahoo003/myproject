package com.tcl.dias.common.gsc.beans;

/**
 * Bean for GscSlaView Entity
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscSlaBean {

	private String pdtName;

	private String slaName;

	private String accessTopology;

	private String defaultValue;

	public String getPdtName() {
		return pdtName;
	}

	public void setPdtName(String pdtName) {
		this.pdtName = pdtName;
	}

	public String getSlaName() {
		return slaName;
	}

	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	public String getAccessTopology() {
		return accessTopology;
	}

	public void setAccessTopology(String accessTopology) {
		this.accessTopology = accessTopology;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
