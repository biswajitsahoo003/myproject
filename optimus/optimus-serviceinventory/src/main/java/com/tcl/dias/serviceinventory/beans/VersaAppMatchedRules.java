package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VersaAppMatchedIps bean class
 * @author archchan
 *
 */
public class VersaAppMatchedRules implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("rule-name")
	private String ruleName;
    @JsonProperty("host-pattern")
	private String hostPattern;
    @JsonProperty("source-prefix")
	private String sourcePrefix;
    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("destination-prefix")
	private String destinationPrefix;
	@JsonProperty("source-port")
	private VersaUsrAppSourcePort sourcePort;
	@JsonProperty("destination-port")
	private VersaUsrAppDestPort destinationPort;
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getHostPattern() {
		return hostPattern;
	}
	public void setHostPattern(String hostPattern) {
		this.hostPattern = hostPattern;
	}
	public String getSourcePrefix() {
		return sourcePrefix;
	}
	public void setSourcePrefix(String sourcePrefix) {
		this.sourcePrefix = sourcePrefix;
	}
	public String getDestinationPrefix() {
		return destinationPrefix;
	}
	public void setDestinationPrefix(String destinationPrefix) {
		this.destinationPrefix = destinationPrefix;
	}
	public VersaUsrAppSourcePort getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(VersaUsrAppSourcePort sourcePort) {
		this.sourcePort = sourcePort;
	}
	public VersaUsrAppDestPort getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(VersaUsrAppDestPort destinationPort) {
		this.destinationPort = destinationPort;
	}
	@Override
	public String toString() {
		return "VersaAppMatchedRules [ruleName=" + ruleName + ", hostPattern=" + hostPattern + ", sourcePrefix="
				+ sourcePrefix + ", protocol=" + protocol + ", destinationPrefix=" + destinationPrefix + ", sourcePort="
				+ sourcePort + ", destinationPort=" + destinationPort + "]";
	}
	
	
	
}
